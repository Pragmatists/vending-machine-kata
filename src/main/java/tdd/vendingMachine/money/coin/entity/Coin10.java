package tdd.vendingMachine.money.coin.entity;

import tdd.vendingMachine.money.factory.MoneyFactory;

public class Coin10 extends AbstractCoin {

	public Coin10() {
		super(MoneyFactory.USD(1));
	}

}
