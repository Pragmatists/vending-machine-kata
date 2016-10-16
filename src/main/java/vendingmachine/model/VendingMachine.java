package vendingmachine.model;

import java.math.BigDecimal;

public class VendingMachine {

	public static final int SHELVES_NR = 4;
	public static final int PRODUCTS_PER_SHELF_NR = 6;

	private Product[][] products = new Product[SHELVES_NR][PRODUCTS_PER_SHELF_NR];
	private BigDecimal moneyInVendingMachine = new BigDecimal(0.0);
	private int currentlySelectedShelf = -1;

	private VendingMachine() {
	}

	private static VendingMachine instance = new VendingMachine();

	public static VendingMachine getInstance() {
		return instance;
	}

	public Product[][] getProducts() {
		return products;
	}

	public void setProducts(Product[][] products) {
		this.products = products;
	}

	public int getCurrentlySelectedShelf() {
		return currentlySelectedShelf;
	}

	public void setCurrentlySelectedShelf(int currentlySelectedShelf) {
		this.currentlySelectedShelf = currentlySelectedShelf;
	}

	public void addMoney(BigDecimal coin) {
		this.moneyInVendingMachine = moneyInVendingMachine.add(coin);
	}

	public BigDecimal getMoneyInVendingMachine() {
		return moneyInVendingMachine;
	}

	public void setMoneyInVendingMachine(BigDecimal moneyInVendingMachine) {
		this.moneyInVendingMachine = moneyInVendingMachine;
	}

}
