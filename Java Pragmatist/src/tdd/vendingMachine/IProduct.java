package tdd.vendingMachine;

public interface IProduct {
	//Methods to use the description of the Product
	public String getDescription();
	public void setDescription(String description);
	
	//Methods to use the price of the Product
	public float getPrice();
	public void setPrice(float price);
	
}
