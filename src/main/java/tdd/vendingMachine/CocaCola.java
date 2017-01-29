package main.java.tdd.vendingMachine;

public class CocaCola implements IProduct{

	String description="CocaCola 0.5l";
	float price=(float)1.5;
	
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
