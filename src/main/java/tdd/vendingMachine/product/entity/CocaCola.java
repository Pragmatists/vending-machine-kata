package tdd.vendingMachine.product.entity;

import tdd.vendingMachine.money.factory.MoneyFactory;
import tdd.vendingMachine.product.factory.ProductQuantityFactory;

public class CocaCola extends AbstractProduct {

	public CocaCola() {
		super("Coca-Cola", MoneyFactory.of(1.50), ProductQuantityFactory.LITER(.25));
	}

}
