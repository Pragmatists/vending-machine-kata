package vendingmachine.model;

import java.math.BigDecimal;

public class Product extends ProductType {

	private int quantity;

	public Product(BigDecimal price, String name, int quantity) {
		super(price, name);
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
