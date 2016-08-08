package tdd.vendingMachine.machine.purchase;

import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tdd.vendingMachine.machine.Machine;
import tdd.vendingMachine.machine.cli.util.CommandLinePrinter;
import tdd.vendingMachine.machine.purchase.enums.PurchaseStatus;
import tdd.vendingMachine.money.change.ChangeCalculator;
import tdd.vendingMachine.money.change.ChangeStorage;
import tdd.vendingMachine.money.coin.entity.Coin;
import tdd.vendingMachine.money.util.MoneyUtil;

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
		Map<Coin, Integer> insertedCoins = changeStorage.getInsertedCoins();
		changeStorage.returnInsertedCoins();
		return insertedCoins;
	}

	public Map<Coin, Integer> buyAndGetChange() {
		// TODO
		return null;
	}

	public void insertCoin(Integer index) {
		int position = 0;
		Map<Coin, Integer> ownedCoins = changeStorage.getOwnedCoins();
		for (Map.Entry<Coin, Integer> entry : ownedCoins.entrySet()) {
			if (position == index) {
				changeStorage.insertCoin(entry.getKey());
				commandLinePrinter.print("Inserted " + entry.getKey().getNominal());
			}
			position++;
		}
	}

	public PurchaseStatus getPurchaseStatus() {
		Money productPrice = machine.getActiveShelve().getProduct().getPrice();
		Money sum = MoneyUtil.sum(changeStorage.getInsertedCoins());
		boolean enoughMoneyIsInserted = sum.compareTo(productPrice) >= 0;

		if (!enoughMoneyIsInserted) {
			return PurchaseStatus.INSUFFICIENT_FUNDS;
		}

		boolean insertedCoinsMakeChange = canChangeBeReturnedUsingInsertedCoins(sum.minus(productPrice));
		boolean ownedCoinsMakeChange = !insertedCoinsMakeChange && canChangeBeReturnedUsingOwnCoins(sum);

		if (!insertedCoinsMakeChange && !ownedCoinsMakeChange) {
			return PurchaseStatus.INSUFFICIENT_CHANGE;
		}

		return PurchaseStatus.PURCHASABLE;
	}

	public List<Coin> getAvailableCoin() {
		return changeStorage.getOwnedCoins().keySet().stream().collect(Collectors.toList());
	}

	private boolean canChangeBeReturnedUsingInsertedCoins(Money insertedCoinsSumWithoutProductPrice) {
		return ChangeCalculator.calculate(changeStorage.getInsertedCoins(), insertedCoinsSumWithoutProductPrice) != null;
	}

	private boolean canChangeBeReturnedUsingOwnCoins(Money ownedCoinsSum) {
		return ChangeCalculator.calculate(changeStorage.getOwnedCoins(), ownedCoinsSum) != null;
	}

}
