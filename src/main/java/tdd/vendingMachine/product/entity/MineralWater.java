package tdd.vendingMachine.product.entity;

import tdd.vendingMachine.product.factory.MoneyFactory;
import tdd.vendingMachine.product.factory.ProductQuantityFactory;

public class MineralWater extends AbstractProduct {

	public MineralWater() {
		super("Mineral water", MoneyFactory.USD(1), ProductQuantityFactory.LITER(.33));
	}

}
