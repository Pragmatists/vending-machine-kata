package tdd.vendingMachine.money.coin.entity;

import tdd.vendingMachine.money.factory.MoneyFactory;

public class Coin100 extends AbstractCoin {

	public Coin100() {
		super(MoneyFactory.of(1));
	}

}
