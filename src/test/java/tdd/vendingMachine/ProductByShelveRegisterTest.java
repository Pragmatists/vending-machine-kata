package tdd.vendingMachine;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import tdd.vendingMachine.enumeration.ProductsEnum;
import tdd.vendingMachine.parts.ProductByShelveRegister;
import tdd.vendingMachine.product.Product;

public class ProductByShelveRegisterTest {

	private ProductByShelveRegister smallRegister;
	private ProductByShelveRegister register;
	
	@Before
	public void setUp() {
		smallRegister = new ProductByShelveRegister(2, 15);
		register = new ProductByShelveRegister(10, 100);
	}

	@Test
	public void registerCanTellIfItHasProductTest() {
		Product candy = ProductsEnum.CANDY.getProduct();
		Product chips = ProductsEnum.CHIPS.getProduct();
		Assertions.assertThat(register.haveProduct(candy)).isFalse();
		Assertions.assertThat(register.haveProduct(chips)).isFalse();
		
		int returnedProducts = register.addProducts(candy, 1);
		Assertions.assertThat(returnedProducts).isEqualTo(0);
		Assertions.assertThat(register.haveProduct(candy)).isTrue();
		Assertions.assertThat(register.haveProduct(chips)).isFalse();
		
		returnedProducts = register.addProducts(chips, 1);
		Assertions.assertThat(returnedProducts).isEqualTo(0);
		Assertions.assertThat(register.haveProduct(candy)).isTrue();
		Assertions.assertThat(register.haveProduct(chips)).isTrue();
	}
	
	@Test
	public void registerCanListAllAddedProductsTest() {
		Product candy = ProductsEnum.CANDY.getProduct();
		Product chips = ProductsEnum.CHIPS.getProduct();
		Assertions.assertThat(register.listProducts()).isEmpty();
		register.addProducts(candy, 1);
		Assertions.assertThat(register.listProducts()).containsSequence(candy);
		register.addProducts(chips, 1);
		Assertions.assertThat(register.listProducts()).containsSequence(candy, chips);
	}
	
	@Test
	public void registerShouldReturnProductsTest() {
		Product candy = ProductsEnum.CANDY.getProduct();
		Product chips = ProductsEnum.CHIPS.getProduct();
		register.addProducts(candy, 2);
		register.addProducts(chips, 1);
		Optional<Product> candyOptional1 = register.takeProduct(candy);
		Optional<Product> candyOptional2 = register.takeProduct(candy);
		Optional<Product> candyOptionalEmpty = register.takeProduct(candy);
		Optional<Product> chipsOptional1 = register.takeProduct(chips);
		Optional<Product> chipsOptionalEmpty = register.takeProduct(chips);
		Assertions.assertThat(candyOptional1).contains(candy);
		Assertions.assertThat(candyOptional2).contains(candy);
		Assertions.assertThat(chipsOptional1).contains(chips);
		Assertions.assertThat(candyOptionalEmpty).isEmpty();
		Assertions.assertThat(chipsOptionalEmpty).isEmpty();
	}
	
	
	@Test
	public void registerShouldBeAbleToReuseShelvesTest() {
		Product candy = ProductsEnum.CANDY.getProduct();
		Product chips = ProductsEnum.CHIPS.getProduct();
		Product juice = ProductsEnum.JUICE.getProduct();
		int candiesReturned = smallRegister.addProducts(candy, 1);
		int chipsReturned = smallRegister.addProducts(chips, 1);
		int juicesReturned = smallRegister.addProducts(juice, 1);
		Assertions.assertThat(candiesReturned).isEqualTo(0);
		Assertions.assertThat(chipsReturned).isEqualTo(0);
		//	Check if we register can take more products than shelves
		Assertions.assertThat(juicesReturned).isEqualTo(1);
		Optional<Product> optionalChips = smallRegister.takeProduct(chips);
		Assertions.assertThat(optionalChips).contains(chips);
		juicesReturned = smallRegister.addProducts(juice, 1);
		Assertions.assertThat(candiesReturned).isEqualTo(0);
		Optional<Product> optionalJuice = smallRegister.takeProduct(juice);
		Assertions.assertThat(optionalJuice).contains(juice);
	}
	
	
	@Test
	public void registerCantKeepMoreProductsThanShelveCapacityTest() {
		Product largeProduct = ProductsEnum.SOMETHING_BIG.getProduct();
		int returnedNumber = smallRegister.addProducts(largeProduct, 2);
		Assertions.assertThat(returnedNumber).isEqualTo(1);
		Assertions.assertThat(smallRegister.haveProduct(largeProduct)).isTrue();
		Optional<Product> productOptional = smallRegister.takeProduct(largeProduct);
		Assertions.assertThat(productOptional).contains(largeProduct);
		Assertions.assertThat(smallRegister.haveProduct(largeProduct)).isFalse();
	}
	
	@Test(expected = AssertionError.class)
	public void registerCanTakeOnlyPositiveNumberOfProductsTest() {
		Product candy = ProductsEnum.CANDY.getProduct();
		register.addProducts(candy, 0);
	}
	
}
