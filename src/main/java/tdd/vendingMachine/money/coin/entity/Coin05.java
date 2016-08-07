package tdd.vendingMachine.money.coin.entity;

import tdd.vendingMachine.money.factory.MoneyFactory;

public class Coin05 extends AbstractCoin {

	public Coin05() {
		super(MoneyFactory.USD(.5));
	}

}
