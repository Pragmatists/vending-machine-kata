package tdd.vendingMachine.parts;

import java.util.Optional;

import tdd.vendingMachine.product.Product;

/**
 * Each shelve may contain only single product.
 * Each shelve has a capacity which limits number of products that may be placed there. 
 *
 */
public class Shelve {
	
	private Optional<Product> productType;
	private int capacity;
	private int singleProductCapacity;
	private int productCount;
	
	public Shelve(int capacity) {
		this.capacity = capacity;
		this.singleProductCapacity = 0;
		this.productCount = 0;
		this.productType = Optional.empty();
	}
	
	/**
	 * adding single product to the shelve.
	 * Product must match shelve product type.
	 * If shelve is empty, placing first product will register that product type to the shelve.
	 * @return true if product was succesfully added, false otherwise
	 */
	public boolean addProduct(Product product) {
		if (!productType.isPresent()) {
			if (product.getCapacity() <= this.capacity) {
				//	set shelve properties
				productType = Optional.of(product);
				productCount++;
				singleProductCapacity = product.getCapacity();
				return true;
			} else return false; // product doesn't fit the shelve
		} 
		
		//	we can only add product of registered type if there is a place on a shelve
		if (productType.get().equals(product) && getRemainingCapacity() >= product.getCapacity()) {
			productCount++;
			return true;
		} else return false;
		
	}
	
	/**
	 * Take single product from shelve
	 * When last product is taken shelve properties will be reset to default
	 * @return
	 */
	public Optional<Product> takeProduct() {
		if (productType.isPresent()) {
			productCount--;
			if (productCount == 0) {
				Optional<Product> lastProduct = productType;
				productType = Optional.empty();
				singleProductCapacity = 0;
				return lastProduct;
			}
		}
		
		return productType;
	}
	
	public boolean isEmpty() {
		return productCount == 0;
	}
	
	/**
	 * Calculat remaing capacity in shelve
	 * If shelve has some capacity left but it is less then needed to fit single product, negative number will be returned
	 */
	private int getRemainingCapacity() {
		return capacity - singleProductCapacity*productCount;
	}
}
