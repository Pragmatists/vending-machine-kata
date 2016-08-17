package tdd.vendingMachine.product.factory;

import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.product.entity.ChocolateBar;
import tdd.vendingMachine.product.entity.CocaCola;
import tdd.vendingMachine.product.entity.MineralWater;

public class ProductFactory {

	public static Product createChocolateBar() {
		return new ChocolateBar();
	}

	public static Product createCocaCola() {
		return new CocaCola();
	}

	public static Product createMineralWater() {
		return new MineralWater();
	}

}
