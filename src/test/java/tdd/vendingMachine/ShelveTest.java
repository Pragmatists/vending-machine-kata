package tdd.vendingMachine;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import tdd.vendingMachine.enumeration.ProductsEnum;
import tdd.vendingMachine.parts.Shelve;
import tdd.vendingMachine.product.Product;

public class ShelveTest {

	private Shelve shelve;
	
	@Before
	public void setUp() {
		shelve = new Shelve(15);
	}
	
	@Test
	public void canAddManyProductsTest() {
		Product candy = ProductsEnum.CANDY.getProduct();
		Assertions.assertThat(shelve.addProduct(candy)).isTrue();
		Assertions.assertThat(shelve.addProduct(candy)).isTrue();
		Assertions.assertThat(shelve.addProduct(candy)).isTrue();
		Assertions.assertThat(shelve.isEmpty()).isFalse();
	}
	
	@Test
	public void cantAddDifferentProductTypesTest() {
		Product candy = ProductsEnum.CANDY.getProduct();
		Product chips = ProductsEnum.CHIPS.getProduct();
		Assertions.assertThat(shelve.addProduct(candy)).isTrue();
		Assertions.assertThat(shelve.addProduct(chips)).isFalse();
	}
	
	@Test
	public void cantAddProductsWhenNoPlaceIsLeftTest() {
		Product big = ProductsEnum.SOMETHING_BIG.getProduct();
		Assertions.assertThat(shelve.addProduct(big)).isTrue();
		Assertions.assertThat(shelve.addProduct(big)).isFalse();
		Assertions.assertThat(shelve.isEmpty()).isFalse();
	}
	
	@Test
	public void cantAddProductWhenIsToBigTest() {
		Product huge = ProductsEnum.SOMETHING_HUGE.getProduct();
		Assertions.assertThat(shelve.addProduct(huge)).isFalse();
		Assertions.assertThat(shelve.isEmpty()).isTrue();
	}
	
	@Test
	public void canTakeProductFromShelveTest() {
		Product candy = ProductsEnum.CANDY.getProduct();
		Assertions.assertThat(shelve.addProduct(candy)).isTrue();
		Assertions.assertThat(shelve.takeProduct()).contains(candy);
		Assertions.assertThat(shelve.isEmpty()).isTrue();
	}
	
	@Test
	public void cantTakeProductFromEmptyShelveTest() {
		Product candy = ProductsEnum.CANDY.getProduct();
		Assertions.assertThat(shelve.addProduct(candy)).isTrue();
		Assertions.assertThat(shelve.isEmpty()).isFalse();
		Assertions.assertThat(shelve.takeProduct()).contains(candy);
		Assertions.assertThat(shelve.isEmpty()).isTrue();
		Assertions.assertThat(shelve.takeProduct()).isEmpty();
	}
	
	@Test
	public void canReuseShelveTest() {
		Product candy = ProductsEnum.CANDY.getProduct();
		Product chips = ProductsEnum.CHIPS.getProduct();
		Assertions.assertThat(shelve.addProduct(candy)).isTrue();
		Assertions.assertThat(shelve.takeProduct()).contains(candy);
		Assertions.assertThat(shelve.isEmpty()).isTrue();
		Assertions.assertThat(shelve.takeProduct()).isEmpty();
		Assertions.assertThat(shelve.addProduct(chips)).isTrue();
		Assertions.assertThat(shelve.isEmpty()).isFalse();
		Assertions.assertThat(shelve.takeProduct()).contains(chips);
		Assertions.assertThat(shelve.isEmpty()).isTrue();
	}
}
