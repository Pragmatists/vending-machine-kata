package main.java.tdd.vendingMachine;

public class Fanta implements IProduct{

	String description="Fanta 0.33l";
	float price=(float)1.1;
	
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description=description;
	}

	@Override
	public float getPrice() {
		return price;
	}

	@Override
	public void setPrice(float price) {
		this.price=price;
	}

	
}
