package vendingmachine.model;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import vendingmachine.enums.ShelfSelection;

@ApplicationScoped
public class VendingMachine {

	private Product[][] products = new Product[VendingMachineConstants.SHELVES_NR][VendingMachineConstants.PRODUCTS_PER_SHELF_NR];
	private ShelfSelection selectedShelf = ShelfSelection.NONE;

	@Inject
	private VendingMachineCoinsStore coinsStore;

	public Product[][] getProducts() {
		return products;
	}

	public void setProducts(Product[][] products) {
		this.products = products;
	}

	public ShelfSelection getSelectedShelf() {
		return selectedShelf;
	}

	public void setSelectedShelf(ShelfSelection selectedShelf) {
		this.selectedShelf = selectedShelf;
	}

	public VendingMachineCoinsStore getCoinsStore() {
		return coinsStore;
	}

	public void setCoinsStore(VendingMachineCoinsStore coinsStore) {
		this.coinsStore = coinsStore;
	}

}
