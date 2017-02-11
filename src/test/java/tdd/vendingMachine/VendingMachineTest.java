package tdd.vendingMachine;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import tdd.vendingMachine.enumeration.CoinsEnum;
import tdd.vendingMachine.enumeration.ProductsEnum;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.services.CoinsRegisterMapFactory;

public class VendingMachineTest {

	private VendingMachine vendingMachine;
	
	@Before
	public void setUp() {
		vendingMachine = new VendingMachine(5, 20, 1);
		vendingMachine.restockProduct(ProductsEnum.CANDY.getProduct(), 5);
    	vendingMachine.restockProduct(ProductsEnum.CHIPS.getProduct(), 5);
	}
    
    @Test
    public void canAddProductsTest() {
    	Set<Product> expected = new HashSet<>();
    	expected.add(ProductsEnum.CANDY.getProduct());
    	expected.add(ProductsEnum.CHIPS.getProduct());
    	Set<Product> products = vendingMachine.getProductSet();
    	Assertions.assertThat(products).containsAll(expected);
    	
    	vendingMachine.restockProduct(ProductsEnum.JUICE.getProduct(), 5);
    	expected.add(ProductsEnum.JUICE.getProduct());
    	products = vendingMachine.getProductSet();
    	Assertions.assertThat(products).containsAll(expected);
    }
    
    @Test
    public void canInsertCoinsTest() {
    	Map<CoinsEnum, Integer> change = vendingMachine.takeChange();
    	Map<CoinsEnum, Integer> expected = CoinsRegisterMapFactory.getEmptytCoinRegister();
    	Assertions.assertThat(change).isEqualTo(expected);
    	
    	vendingMachine.insertCoin(CoinsEnum.HALF);
    	vendingMachine.insertCoin(CoinsEnum.POINT_ONE);
    	change = vendingMachine.takeChange();
    	expected.put(CoinsEnum.HALF, 1);
    	expected.put(CoinsEnum.POINT_ONE, 1);
    	Assertions.assertThat(change).isEqualTo(expected);
    }
    
    @Test
    public void canBuyProductTest() {
    	vendingMachine.insertCoin(CoinsEnum.ONE);
    	Optional<Product> product = vendingMachine.buy(ProductsEnum.CANDY.getProduct());
    	Assertions.assertThat(product).contains(ProductsEnum.CANDY.getProduct());
    }
    
    @Test
    public void canTakeChangeTest() {
    	vendingMachine.insertCoin(CoinsEnum.FIVE);
    	vendingMachine.buy(ProductsEnum.CHIPS.getProduct());
    	Map<CoinsEnum, Integer> change = vendingMachine.takeChange();
    	
    	Map<CoinsEnum, Integer> expected = CoinsRegisterMapFactory.getEmptytCoinRegister();
    	expected.put(CoinsEnum.TWO, 1);
    	expected.put(CoinsEnum.HALF, 1);
    	Assertions.assertThat(change).isEqualTo(expected);
    }
    
    @Test
    public void cantBuyProductIfNoChangeTest() {
    	vendingMachine = new VendingMachine(5, 20, 0); // need empty register
    	vendingMachine.restockProduct(ProductsEnum.CANDY.getProduct(), 5);
    	vendingMachine.restockProduct(ProductsEnum.CHIPS.getProduct(), 5);
    	
    	vendingMachine.insertCoin(CoinsEnum.TWO);
    	vendingMachine.insertCoin(CoinsEnum.ONE); //total 3.0
    	Optional<Product> product = vendingMachine.buy(ProductsEnum.CHIPS.getProduct());
    	Map<CoinsEnum, Integer> change = vendingMachine.takeChange();
    	System.out.println(change);
    	
    	Map<CoinsEnum, Integer> expected = CoinsRegisterMapFactory.getEmptytCoinRegister();
    	expected.put(CoinsEnum.TWO, 1);
    	expected.put(CoinsEnum.ONE, 1);
    	
    	Assertions.assertThat(product).isEmpty();
    	Assertions.assertThat(change).isEqualTo(expected);
    }
    
    @Test
    public void cantBuyProductIfNotEnoughMoneyTest() {
    	vendingMachine.insertCoin(CoinsEnum.HALF);
    	Optional<Product> product = vendingMachine.buy(ProductsEnum.CANDY.getProduct());
    	Assertions.assertThat(product).isEmpty();
    }
}
