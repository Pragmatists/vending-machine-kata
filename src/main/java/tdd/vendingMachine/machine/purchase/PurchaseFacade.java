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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	public Map<Coin, Integer> returnInsertedCoins() {
		Map<Coin, Integer> insertedCoins = getInsertedCoins();
		changeStorage.returnInsertedCoins();
		return insertedCoins;
	}

	public void buy() {
		if (!PurchaseStatus.PURCHASABLE.equals(getPurchaseStatus())) {
			// TODO: more info
			commandLinePrinter.print(AnsiColorDecorator.red("Cannot buy."));
			return;
		}

		Product product = getProduct();

		commandLinePrinter.print(AnsiColorDecorator.green(
			"Purchased " + product.getName() + " for " + product.getPrice() + "."));

		if (canChangeBeReturnedUsingInsertedCoins()) {
			returnChangeUsingInsertedCoins();
		} else {
			returnChangeUsingBothStorages();
		}
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

		if (!enoughMoneyIsInserted) {
			return PurchaseStatus.INSUFFICIENT_FUNDS;
		}

		boolean insertedCoinsMakeChange = canChangeBeReturnedUsingInsertedCoins();
		boolean ownedCoinsMakeChange = !insertedCoinsMakeChange && canChangeBeReturnedUsingOwnedCoins();
		boolean bothStoragesCoinsMakeChange = !ownedCoinsMakeChange && canChangeByReturnedUsingBothStorages();

		if (!insertedCoinsMakeChange && !ownedCoinsMakeChange && !bothStoragesCoinsMakeChange) {
			return PurchaseStatus.INSUFFICIENT_CHANGE;
		}

		return PurchaseStatus.PURCHASABLE;
	}

	public List<Coin> getAvailableCoin() {
		return getOwnedCoins().keySet().stream().collect(Collectors.toList());
	}

	private void returnChangeUsingBothStorages() {
		Money productPrice = getProductPrice();
		Map<Coin, Integer> sum = getOwnedAndInsertedCoins();
		Map<Coin, Integer> change = ChangeCalculator.calculate(sum, productPrice);
		Map<Coin, Integer> sumWithoutChange = MoneyUtil.subtract(sum, change);
		Money moneyWithoutChange = MoneyUtil.sum(getInsertedCoins()).minus(productPrice);
		Map<Coin, Integer> insertedCoins = MoneyUtil.subset(sumWithoutChange, moneyWithoutChange);
		Map<Coin, Integer> ownedCoins = MoneyUtil.subtract(sumWithoutChange, insertedCoins);
		changeStorage.setInsertedCoins(insertedCoins);
		changeStorage.setOwnedCoins(ownedCoins);
	}

	private void returnChangeUsingInsertedCoins() {

		changeStorage.setInsertedCoins(MoneyUtil.subtract(getInsertedCoins(),
			ChangeCalculator.calculate(getInsertedCoins(), getProductPrice())));
	}

	private boolean canChangeBeReturnedUsingInsertedCoins() {
		return ChangeCalculator.calculate(getInsertedCoins(), getProductPrice()) != null;
	}

	private boolean canChangeBeReturnedUsingOwnedCoins() {
		return ChangeCalculator.calculate(getOwnedCoins(), getProductPrice()) != null;
	}

	private boolean canChangeByReturnedUsingBothStorages() {
		return ChangeCalculator.calculate(getOwnedAndInsertedCoins(), getProductPrice()) != null;
	}

	private Money getProductPrice() {
		return getProduct().getPrice();
	}

	private Product getProduct() {
		return  machine.getActiveShelve().getProduct();
	}

	private Money sumOwnedCoins() {
		return MoneyUtil.sum(getOwnedCoins());
	}

	private Money sumInsertedCoins() {
		return MoneyUtil.sum(getInsertedCoins());
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
