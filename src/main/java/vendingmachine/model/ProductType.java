package vendingmachine.model;

import java.math.BigDecimal;

public abstract class ProductType {

	private BigDecimal price;
	private String name;

	public ProductType(BigDecimal price, String name) {
		super();
		this.price = price;
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
