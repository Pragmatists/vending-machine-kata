package tdd.vendingMachine.product.entity;

import tdd.vendingMachine.product.factory.MoneyFactory;
import tdd.vendingMachine.product.factory.ProductQuantityFactory;

public class CocaCola extends AbstractProduct {

	public CocaCola() {
		super("Coca-Cola", MoneyFactory.USD(1.50), ProductQuantityFactory.LITER(.25));
	}

}
