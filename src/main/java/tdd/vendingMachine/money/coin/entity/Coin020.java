package tdd.vendingMachine.money.coin.entity;

import tdd.vendingMachine.money.factory.MoneyFactory;

public class Coin020 extends AbstractCoin {

	public Coin020() {
		super(MoneyFactory.of(.2));
	}

}
