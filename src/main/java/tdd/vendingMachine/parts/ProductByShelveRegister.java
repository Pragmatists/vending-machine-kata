package tdd.vendingMachine.parts;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import tdd.vendingMachine.product.Product;

/**
 * products mapped to shelves on which they are placed.
 * Register also tracks information about empty shelves.
 * Each product may be placed on one shelve only. 
 */
public class ProductByShelveRegister {
	
	private Map<Product, Shelve> productsByShelveMap;
	private List<Shelve> emptyShelves;
	
	public ProductByShelveRegister(int shelvesNumber, int shelveCapacity) {
		productsByShelveMap = new HashMap<>();
		emptyShelves = new LinkedList<>();
		for (int i = 0; i < shelvesNumber; i++) 
			emptyShelves.add(new Shelve(shelveCapacity));
	}
	
	/**
	 * Add products to shelve.
	 * If p is new product then empty shelve will be used to store products (if available)
	 * If register holds already shelve for given product, new product will be added to shelve, 
	 * but only to shelve maximum capacity, remaining product will be returned
	 * @param p {@link Product}
	 * @return returned products number (0 - means that all product where placed on shelve,
	 *         value greater than 0 is a number of products that weren't placed)
	 */
	public int addProducts(Product p, int number) {
		assert(number > 0);
		//		look for shelve for product p
		Shelve shelve = null;
		if (productsByShelveMap.containsKey(p)) shelve = productsByShelveMap.get(p);
		else if (!emptyShelves.isEmpty()) shelve = emptyShelves.remove(0);
		else return number;
		
		//		place proper number of products on shelve
		while (number > 0 && shelve.addProduct(p)) number--;
		if(!shelve.isEmpty()) productsByShelveMap.put(p, shelve);
		return number;
	}
	
	/**
	 * Check 
	 * @param p
	 * @return
	 */
	public boolean haveProduct(Product p) {
		return productsByShelveMap.containsKey(p);
	}
	
	/**
	 * Take chosen product from vending machine if possible. 
	 * @param p
	 * @return Optional<Product>
	 */
	public Optional<Product> takeProduct(Product p) {
		if (!haveProduct(p)) return Optional.empty();
		
		Shelve shelve = productsByShelveMap.get(p);
		Product result = shelve.takeProduct().get();
		if (shelve.isEmpty()) {
			//  return shelve to empty shelves register
			productsByShelveMap.remove(p);
			emptyShelves.add(shelve);
		}
		
		return Optional.of(result);
	}
	
	/**
	 * @return Set of all products inside vending machine
	 */
	public Set<Product> listProducts() {
		return productsByShelveMap.keySet();
	}

}
