package tdd.vendingMachine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.Map.Entry;

import tdd.vendingMachine.enumeration.CoinsEnum;
import tdd.vendingMachine.enumeration.ProductsEnum;
import tdd.vendingMachine.product.Product;

public class VendingMachineDemo {


	public static void main(String[] args) {
		VendingMachine vm = new VendingMachine(5, 20, 50);
		Map<Product, Integer> products = new HashMap<>();
		products.put(ProductsEnum.CANDY.getProduct(), 50);
		products.put(ProductsEnum.CHIPS.getProduct(), 50);
		products.put(ProductsEnum.JUICE.getProduct(), 50);
		products.put(ProductsEnum.SOMETHING_BIG.getProduct(), 50);
		products.put(ProductsEnum.SOMETHING_HUGE.getProduct(), 50);
		
		printProductsMap(vm.fillInProducts(products));
		
		productsList(vm);
		
		System.out.println();
		Product chips = ProductsEnum.CHIPS.getProduct();
		Product candy = ProductsEnum.CANDY.getProduct();
		Product juice = ProductsEnum.JUICE.getProduct();
		Product big = ProductsEnum.SOMETHING_BIG.getProduct();
		Product huge = ProductsEnum.SOMETHING_HUGE.getProduct();
		buy(vm, chips);
		
		makePurchase(vm, chips);
		makePurchase(vm, candy);
		makePurchase(vm, juice);
		makePurchase(vm, big);
		makePurchase(vm, huge);
		
		for (int i = 0; i < 20; i++) vm.insertCoin(CoinsEnum.FIVE);
		makePurchase(vm, big);
		
		for (int i = 0; i < 20; i++) vm.insertCoin(CoinsEnum.FIVE);
		makePurchase(vm, big);
		
		for (int i = 0; i < 20; i++) vm.insertCoin(CoinsEnum.FIVE);
		makePurchase(vm, big);
		
		productsList(vm);
		printProductsMap(vm.fillInProducts(products));
		productsList(vm);
		
		//  try to use coins for change
		for (int i = 0; i < 20; i++) {
			makePurchase(vm, candy);
		}
		printProductsMap(vm.fillInProducts(products));
		for (int i = 0; i < 20; i++) {
			makePurchase(vm, candy);
		}
		printProductsMap(vm.fillInProducts(products));
		for (int i = 0; i < 6; i++) {
			makePurchase(vm, candy);
		}
	}
	
	private static void makePurchase(VendingMachine vm, Product p) {
		vm.insertCoin(CoinsEnum.FIVE);
		buy(vm, p);
		takeChange(vm);
	}
	
	private static void buy(VendingMachine vm, Product p) {
		Optional<Product> optionalProduct = vm.buy(p);
		if (optionalProduct.isPresent()) 
			System.out.printf("You bought %s for %.2f.\n", p.getName(), p.getPrice());
		else if (!vm.getProductSet().contains(p)) 
			System.out.println("We don't have this product.");
		else
			System.out.printf("You failed to make purchase for '%s'. Insert some money first, and than try again.\n", p.getName());
		System.out.println();
	}
	
	private static void takeChange(VendingMachine vm) {
		Map<CoinsEnum, Integer> change = vm.takeChange();
		StringJoiner joiner = new StringJoiner(", ", "Your change: ", ".\n");
		for (Entry<CoinsEnum, Integer> entry : change.entrySet()) {
			joiner.add(entry.getKey().name() +"="+ entry.getValue());
		}
		System.out.println(joiner.toString());
	}
	
	private static void productsList(VendingMachine vm) {
		Set<Product> availableProducts = vm.getProductSet();
		for (Product product : availableProducts) 
			System.out.println("Available product: "+ product.getName());
		System.out.println();
	}
	
	private static void printProductsMap(Map<Product, Integer> map) {
		for (Entry<Product, Integer> entry : map.entrySet()) {
			System.out.printf("returned %d pieces of %s\n", entry.getValue(), entry.getKey().getName());
		}
		
		System.out.println();
	}
}
