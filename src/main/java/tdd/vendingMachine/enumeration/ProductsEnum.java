package tdd.vendingMachine.enumeration;

import java.math.BigDecimal;

import tdd.vendingMachine.product.Product;

public enum ProductsEnum {
	JUICE("juice", 2, new BigDecimal("2.0")),
	CANDY("candy", 1, new BigDecimal("1.0")),
	CHIPS("chips", 3, new BigDecimal("2.5")),
	SOMETHING_BIG("something big", 10, new BigDecimal("100.0")),
	SOMETHING_HUGE("something huge", 20, new BigDecimal("1000.0")),
	;
	
	
	private Product product;
	
	private ProductsEnum(String name, int capacity, BigDecimal price) {
		this.product = new Product(name, capacity, price);
	}
	
	public Product getProduct() {
		return product;
	}
}
