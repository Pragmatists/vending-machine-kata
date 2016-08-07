package tdd.vendingMachine.money.coin.entity;

import tdd.vendingMachine.money.factory.MoneyFactory;

public class Coin01 extends AbstractCoin {

	public Coin01() {
		super(MoneyFactory.USD(.1));
	}

}
