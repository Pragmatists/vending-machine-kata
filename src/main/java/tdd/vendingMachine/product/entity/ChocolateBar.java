package tdd.vendingMachine.product.entity;

import tdd.vendingMachine.money.factory.MoneyFactory;
import tdd.vendingMachine.product.factory.ProductQuantityFactory;

public class ChocolateBar extends AbstractProduct {

	public ChocolateBar() {
		super("Chocolate bar", MoneyFactory.USD(2.20), ProductQuantityFactory.KILOGRAM(.1));
	}

}
