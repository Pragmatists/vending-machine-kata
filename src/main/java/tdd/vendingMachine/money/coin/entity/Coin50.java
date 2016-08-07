package tdd.vendingMachine.money.coin.entity;

import tdd.vendingMachine.money.factory.MoneyFactory;

public class Coin50 extends AbstractCoin {

	public Coin50() {
		super(MoneyFactory.USD(5));
	}

}
