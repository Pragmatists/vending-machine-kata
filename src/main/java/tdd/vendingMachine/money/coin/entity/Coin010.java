package tdd.vendingMachine.money.coin.entity;

import tdd.vendingMachine.money.factory.MoneyFactory;

public class Coin010 extends AbstractCoin {

	public Coin010() {
		super(MoneyFactory.of(.1));
	}

}
