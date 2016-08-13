package tdd.vendingMachine.money.coin.entity;

import tdd.vendingMachine.money.factory.MoneyFactory;

public class Coin200 extends AbstractCoin {

	public Coin200() {
		super(MoneyFactory.of(2));
	}

}
