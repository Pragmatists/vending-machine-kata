package tdd.vendingMachine.product.factory;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.product.quantity.ProductQuantityUnit;

public class ProductFactoryTest {

	@Test
	public void creates_chocolate_bar() {
		Product product = ProductFactory.createChocolateBar();

		Assertions.assertThat(product.getName()).isEqualTo("Chocolate bar");
		Assertions.assertThat(product.getQuantity().getUnit()).isEqualTo(ProductQuantityUnit.KILOGRAM);
		Assertions.assertThat(product.getQuantity().getAmount()).isEqualTo(.1);
		Assertions.assertThat(product.getPrice().getCurrencyUnit().getCurrencyCode()).isEqualTo("USD");
		Assertions.assertThat(product.getPrice().getAmountMinorInt()).isEqualTo(220);
	}

	@Test
	public void creates_coca_cola() {
		Product product = ProductFactory.createCocaCola();

		Assertions.assertThat(product.getName()).isEqualTo("Coca-Cola");
		Assertions.assertThat(product.getQuantity().getUnit()).isEqualTo(ProductQuantityUnit.LITER);
		Assertions.assertThat(product.getQuantity().getAmount()).isEqualTo(.25);
		Assertions.assertThat(product.getPrice().getCurrencyUnit().getCurrencyCode()).isEqualTo("USD");
		Assertions.assertThat(product.getPrice().getAmountMinorInt()).isEqualTo(150);
	}

	@Test
	public void creates_mineral_water() {
		Product product = ProductFactory.createMineralWater();

		Assertions.assertThat(product.getName()).isEqualTo("Mineral water");
		Assertions.assertThat(product.getQuantity().getUnit()).isEqualTo(ProductQuantityUnit.LITER);
		Assertions.assertThat(product.getQuantity().getAmount()).isEqualTo(.33);
		Assertions.assertThat(product.getPrice().getCurrencyUnit().getCurrencyCode()).isEqualTo("USD");
		Assertions.assertThat(product.getPrice().getAmountMinorInt()).isEqualTo(100);
	}

}
