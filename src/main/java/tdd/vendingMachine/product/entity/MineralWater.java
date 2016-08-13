package tdd.vendingMachine.product.entity;

import tdd.vendingMachine.money.factory.MoneyFactory;
import tdd.vendingMachine.product.factory.ProductQuantityFactory;

public class MineralWater extends AbstractProduct {

	public MineralWater() {
		super("Mineral water", MoneyFactory.of(1), ProductQuantityFactory.LITER(.33));
	}

}
