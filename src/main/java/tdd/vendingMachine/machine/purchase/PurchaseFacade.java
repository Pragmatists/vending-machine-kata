package tdd.vendingMachine.machine.purchase;

import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tdd.vendingMachine.machine.Machine;
import tdd.vendingMachine.machine.cli.util.AnsiColorDecorator;
import tdd.vendingMachine.machine.cli.util.CommandLinePrinter;
import tdd.vendingMachine.machine.purchase.enums.PurchaseStatus;
import tdd.vendingMachine.money.change.ChangeCalculator;
import tdd.vendingMachine.money.change.ChangeStorage;
import tdd.vendingMachine.money.coin.entity.Coin;
import tdd.vendingMachine.money.util.MoneyUtil;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.shelve.entity.Shelve;

import java.util.List;
import java.util.Map;

import static tdd.vendingMachine.money.coin.factory.CoinFactory.AVAILABLE_COINS;

@Service
public class PurchaseFacade {

	private Machine machine;

	private ChangeStorage changeStorage;

	private CommandLinePrinter commandLinePrinter;

	@Autowired
	public PurchaseFacade(Machine machine, ChangeStorage changeStorage, CommandLinePrinter commandLinePrinter) {
		this.machine = machine;
		this.changeStorage = changeStorage;
		this.commandLinePrinter = commandLinePrinter;
	}

	public void buy() {
		PurchaseStatus purchaseStatus = getPurchaseStatus();
		if (!PurchaseStatus.PURCHASABLE.equals(purchaseStatus)) {
			printCannotBuyMessage(purchaseStatus);
			return;
		}

		if (canChangeBeReturnedUsingInsertedCoins()) {
			returnChangeUsingInsertedCoins();
		} else {
			returnChangeUsingBothStorages();
		}

		Product product = getProduct();
		Shelve activeShelve = machine.getActiveShelve();
		activeShelve.setQuantity(activeShelve.getQuantity() - 1);
		commandLinePrinter.print(AnsiColorDecorator.green(
			"Purchased " + product.getName() + " for " + product.getPrice() + "."));
	}

	public void insertCoin(Integer index) {
		int position = 0;
		Map<Coin, Integer> ownedCoins = getOwnedCoins();
		for (Map.Entry<Coin, Integer> entry : ownedCoins.entrySet()) {
			if (position == index) {
				changeStorage.insertCoin(entry.getKey());
				commandLinePrinter.print("Inserted " + entry.getKey().getNominal());
			}
			position++;
		}
	}

	public PurchaseStatus getPurchaseStatus() {
		Money productPrice = getProduct().getPrice();
		Money sum = sumInsertedCoins();
		boolean enoughMoneyIsInserted = sum.compareTo(productPrice) >= 0;
		Integer quantity = machine.getActiveShelve().getQuantity();

		if (quantity == 0) {
			return PurchaseStatus.NO_PRODUCT;
		}

		if (!enoughMoneyIsInserted) {
			return PurchaseStatus.INSUFFICIENT_FUNDS;
		}

		boolean insertedCoinsMakeChange = canChangeBeReturnedUsingInsertedCoins();
		boolean ownedCoinsMakeChange = !insertedCoinsMakeChange && canChangeBeReturnedUsingOwnedCoins();
		boolean bothStoragesCoinsMakeChange = !ownedCoinsMakeChange && canChangeByReturnedUsingBothStorages();
		boolean swapingStoragesCoinsMakeChange = !bothStoragesCoinsMakeChange && canChangeBeReturnedOnlyBeSwapingStorages();

		if (!insertedCoinsMakeChange && !ownedCoinsMakeChange && !bothStoragesCoinsMakeChange &&
			!swapingStoragesCoinsMakeChange) {
			return PurchaseStatus.INSUFFICIENT_CHANGE;
		}

		return PurchaseStatus.PURCHASABLE;
	}

	public List<Coin> getAvailableCoin() {
		return AVAILABLE_COINS;
	}

	private void printCannotBuyMessage(PurchaseStatus purchaseStatus) {
		String message = "Cannot buy: ";
		if (PurchaseStatus.INSUFFICIENT_FUNDS.equals(purchaseStatus)) {
			message += "insufficient money inserted.";
		} else if (PurchaseStatus.NO_PRODUCT.equals(purchaseStatus)) {
			message += "no more product in machine.";
		} else {
			message += "changed cannot be given back using neither inserted coins nor coins owned by machine.";
		}
		commandLinePrinter.print(AnsiColorDecorator.red(message));
	}

	private void returnChangeUsingBothStorages() {
		Money productPrice = getProductPrice();
		if (!canChangeBeReturnedUsingInsertedCoins() && !canChangeBeReturnedUsingOwnedCoins() &&
			!canChangeByReturnedUsingBothStorages() && canChangeBeReturnedOnlyBeSwapingStorages()) {
			Map<Coin, Integer> insertedCoins = ChangeCalculator.calculateChangeDifference(getOwnedCoins(), sumInsertedCoins().minus(getProductPrice()));
			Map<Coin, Integer> ownedCoins = ChangeCalculator.calculateChangeDifference(getInsertedCoins(), sumOwnedCoins().plus(getProductPrice()));
			changeStorage.setInsertedCoins(insertedCoins);
			changeStorage.setOwnedCoins(ownedCoins);
		} else {
			Map<Coin, Integer> sum = getOwnedAndInsertedCoins();
			Map<Coin, Integer> change = ChangeCalculator.calculateChange(sum, productPrice);
			Map<Coin, Integer> insertedCoins =ChangeCalculator.calculateChangeDifference(sum, MoneyUtil.sum(change));
			Map<Coin, Integer> ownedCoins = MoneyUtil.subtract(sum, insertedCoins);
			changeStorage.setInsertedCoins(insertedCoins);
			changeStorage.setOwnedCoins(ownedCoins);
		}
	}

	private void returnChangeUsingInsertedCoins() {
		Map<Coin, Integer> payingCoins = ChangeCalculator.calculateChange(getInsertedCoins(), getProductPrice());
		changeStorage.setInsertedCoins(MoneyUtil.subtract(getInsertedCoins(), payingCoins));
		changeStorage.setOwnedCoins(MoneyUtil.add(getOwnedCoins(), payingCoins));
	}

	private boolean canChangeBeReturnedUsingInsertedCoins() {
		return ChangeCalculator.calculateChange(getInsertedCoins(), getProductPrice()) != null;
	}

	private boolean canChangeBeReturnedUsingOwnedCoins() {
		return ChangeCalculator.calculateChange(getOwnedCoins(), getProductPrice()) != null;
	}

	private boolean canChangeByReturnedUsingBothStorages() {
		return ChangeCalculator.calculateChange(getOwnedAndInsertedCoins(), getProductPrice()) != null;
	}

	private boolean canChangeBeReturnedOnlyBeSwapingStorages() {
		Map<Coin, Integer> subset = ChangeCalculator.calculateChangeDifference(getOwnedCoins(), sumInsertedCoins().minus(getProductPrice()));

		if (subset == null) {
			return false;
		}

		return !subset
			.values()
			.stream()
			.anyMatch(value -> value < 0);
	}

	private Money getProductPrice() {
		return getProduct().getPrice();
	}

	private Product getProduct() {
		return  machine.getActiveShelve().getProduct();
	}

	private Money sumInsertedCoins() {
		return MoneyUtil.sum(getInsertedCoins());
	}

	private Money sumOwnedCoins() {
		return MoneyUtil.sum(getOwnedCoins());
	}

	private Map<Coin, Integer> getOwnedCoins() {
		return changeStorage.getOwnedCoins();
	}

	private Map<Coin, Integer> getInsertedCoins() {
		return changeStorage.getInsertedCoins();
	}

	private Map<Coin, Integer> getOwnedAndInsertedCoins() {
		return MoneyUtil.add(getInsertedCoins(), getOwnedCoins());
	}

}
