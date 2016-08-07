package tdd.vendingMachine.money.coin.entity;

import tdd.vendingMachine.money.factory.MoneyFactory;

public class Coin02 extends AbstractCoin {

	public Coin02() {
		super(MoneyFactory.USD(.2));
	}

}
