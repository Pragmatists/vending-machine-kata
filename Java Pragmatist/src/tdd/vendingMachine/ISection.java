package tdd.vendingMachine;

public interface ISection {
	
	//Methods related to the product of the ISection
	public IProduct getIProduct();
	public void setIProduct(IProduct iProduct);
	
	//Methods related to the number of products available in the ISection
	public int getNumberOfProducts();
	public void setNumberOfProducts(int numberOfProducts);

	//Methods related to the number of ISection we are
	public int getNumberOfSection();
	public void setNumberOfSection(int numberOfSection);

}
