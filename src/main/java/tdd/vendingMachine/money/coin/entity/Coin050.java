package tdd.vendingMachine.money.coin.entity;

import tdd.vendingMachine.money.factory.MoneyFactory;

public class Coin050 extends AbstractCoin {

	public Coin050() {
		super(MoneyFactory.of(.5));
	}

}
