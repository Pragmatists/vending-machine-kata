package tdd.vendingMachine.product.factory;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import tdd.vendingMachine.product.quantity.ProductQuantity;
import tdd.vendingMachine.product.quantity.ProductQuantityUnit;

public class ProductQuantityFactoryTest {

	@Test
	public void creates_product_quantity_using_liters() {
		final ProductQuantityUnit unit = ProductQuantityUnit.LITER;
		final double amount = 2.2;

		ProductQuantity productQuantity = ProductQuantityFactory.LITER(amount);

		Assertions.assertThat(productQuantity.getUnit()).isEqualByComparingTo(unit);
		Assertions.assertThat(productQuantity.getAmount()).isEqualByComparingTo(amount);
	}

	@Test
	public void creates_product_quantity_using_kilograms() {
		final ProductQuantityUnit unit = ProductQuantityUnit.KILOGRAM;
		final double amount = 2.2;

		ProductQuantity productQuantity = ProductQuantityFactory.KILOGRAM(amount);

		Assertions.assertThat(productQuantity.getUnit()).isEqualByComparingTo(unit);
		Assertions.assertThat(productQuantity.getAmount()).isEqualByComparingTo(amount);
	}

}
