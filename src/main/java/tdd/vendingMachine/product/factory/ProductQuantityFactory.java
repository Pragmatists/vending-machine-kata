package tdd.vendingMachine.product.factory;

import tdd.vendingMachine.product.quantity.ProductQuantity;
import tdd.vendingMachine.product.quantity.ProductQuantityUnit;

public class ProductQuantityFactory {

	public static ProductQuantity LITER(double amount) {
		return create(ProductQuantityUnit.LITER, amount);
	}

	public static ProductQuantity KILOGRAM(double amount) {
		return create(ProductQuantityUnit.KILOGRAM, amount);
	}

	private static ProductQuantity create(ProductQuantityUnit unit, double amount) {
		return ProductQuantity.of(unit, amount);
	}


}
