package tdd.vendingMachine.product;

import java.math.BigDecimal;

/**
 * 
 * Class representing product sold by vending machine
 * Each product must have name, price and capacity. 
 * Capacity value is used to calculate if there is enough space on vending machine shelve to place there product
 *
 */
public class Product {

	protected String name;
	protected int capacity;
	protected BigDecimal price;
	
	public Product(String name, int capacity, BigDecimal price) {
		assert(capacity > 0);
		assert(price != null && price.compareTo(new BigDecimal("0.0")) == 1);
		assert(name != null && !name.isEmpty());
		
		this.name = name;
		this.price = price;
		this.capacity = capacity;
	}
	
	public String getName() {
		return name;
	}
	public int getCapacity() {
		return capacity;
	}
	public BigDecimal getPrice() {
		return price;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + capacity;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (capacity != other.capacity)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		return true;
	}
	
	
	
	
}
