package tdd.vendingMachine.money.coin.entity;

import tdd.vendingMachine.money.factory.MoneyFactory;

public class Coin20 extends AbstractCoin {

	public Coin20() {
		super(MoneyFactory.USD(2));
	}

}
