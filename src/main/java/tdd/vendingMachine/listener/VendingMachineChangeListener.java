package tdd.vendingMachine.listener;

import tdd.vendingMachine.money.Money;

public interface VendingMachineChangeListener {
	void changeReturned(Money moneyReturned);
}
