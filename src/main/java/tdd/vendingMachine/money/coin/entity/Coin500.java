package tdd.vendingMachine.money.coin.entity;

import tdd.vendingMachine.money.factory.MoneyFactory;

public class Coin500 extends AbstractCoin {

	public Coin500() {
		super(MoneyFactory.of(5));
	}

}
