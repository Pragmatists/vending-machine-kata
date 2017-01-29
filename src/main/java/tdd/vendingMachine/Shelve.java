package main.java.tdd.vendingMachine;

public class Shelve implements ISection{

	IProduct product;
	int numberOfProducts;
	int numberOfSection;
	
	Shelve (IProduct product, int numberOfProducts, int numberOfSection){
		this.product=product;
		this.numberOfProducts=numberOfProducts;
		this.numberOfSection=numberOfSection;
	}
	
	@Override
	public IProduct getIProduct() {
		return product;
	}

	@Override
	public void setIProduct(IProduct iProduct) {
		product=iProduct;
	}

	@Override
	public int getNumberOfProducts() {
		return numberOfProducts;
	}

	@Override
	public void setNumberOfProducts(int numberOfProducts) {
		this.numberOfProducts=numberOfProducts;
	}

	@Override
	public int getNumberOfSection() {
		return numberOfSection;
	}

	@Override
	public void setNumberOfSection(int numberOfSection) {
		this.numberOfSection=numberOfSection;
	}

}
