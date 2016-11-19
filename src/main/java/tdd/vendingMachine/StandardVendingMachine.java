package tdd.vendingMachine;

import static tdd.vendingMachine.message.VendingMachineMessages.*;

import java.math.BigDecimal;
import java.util.*;

import tdd.vendingMachine.listener.VendingMachineChangeListener;
import tdd.vendingMachine.listener.VendingMachineDispenserListener;
import tdd.vendingMachine.listener.VendingMachineDisplayListener;
import tdd.vendingMachine.money.Money;
import tdd.vendingMachine.product.VendingProduct;

public class StandardVendingMachine implements VendingMachine {

	private List<VendingMachineDisplayListener> displayListeners = new ArrayList<>();
	private List<VendingMachineDispenserListener> dispenserListeners = new ArrayList<>();
	private List<VendingMachineChangeListener> changeListeners = new ArrayList<>();

	private List<Money> storedMoney = new ArrayList<>();
	private Map<Integer, Queue<VendingProduct>> shelves = new HashMap<>();
	private Map<String, Integer> productToSchelveMapping = new HashMap<>();

	private int currentProductSchelf = -1;
	private float currentlyPaidAmount;
	private VendingProduct currentProduct;
	private List<Money> currentlyPaidMoney = new ArrayList<>();

	@Override
	public void addVendingMachineDisplayListener(VendingMachineDisplayListener listener) {
		displayListeners.add(listener);
	}

	@Override
	public void addVendingMachineDispenserListener(VendingMachineDispenserListener listener) {
		dispenserListeners.add(listener);
	}

	@Override
	public void addVendingMachineChangeListener(VendingMachineChangeListener listener) {
		changeListeners.add(listener);
	}

	@Override
	public void addProductsToStorage(List<VendingProduct> products) {
		products.forEach((product) -> {
			String type = product.getProductType();
			if (!productToSchelveMapping.containsKey(type)) {
				// shelves.size() will always return next shelf index for new
				// type of product
				int newShelfNumber = shelves.size();
				shelves.put(newShelfNumber, new LinkedList<>());
				productToSchelveMapping.put(type, newShelfNumber);
			}
			shelves.get(productToSchelveMapping.get(type)).add(product);
		});
	}

	@Override
	public void addMoneyToStorage(List<Money> money) {
		storedMoney.addAll(money);
	}

	@Override
	public void chooseProductShelf(int schelfNumber) {
		currentProductSchelf = schelfNumber;
		currentProduct = shelves.get(schelfNumber).peek();
		displayProductPrice();
	}

	@Override
	public void putMoney(Money money) {
		currentlyPaidMoney.add(money);
		currentlyPaidAmount += money.getValue();
		if (currentlyPaidAmount >= currentProduct.getProductPrice()) {
			tryPurchaseProduct();
		} else {
			displayRemainingAmount();
		}
	}

	@Override
	public void cancelPurchase() {
		releaseMoney(currentlyPaidMoney);
		displayMessage(PURCHASE_CANCELLED);
		resetMachineState();
	}

	private void tryPurchaseProduct() {
		displayMessage(PRODUCT_PURCHASED);
		if (currentlyPaidAmount == currentProduct.getProductPrice()) {
			purchaseProduct();
		} else {
			purchaseWithChange();
		}
	}

	private void purchaseWithChange() {
		if (tryReleaseChange()) {
			purchaseProduct();
		} else {
			releaseMoney(currentlyPaidMoney);
			displayMessage(CHANGE_NOT_AVAILABLE);
			resetMachineState();
		}
	}

	private boolean tryReleaseChange() {
		ArrayList<Money> availableMoney = new ArrayList<>();
		availableMoney.addAll(storedMoney);
		availableMoney.addAll(currentlyPaidMoney);

		ArrayList<Money> changeMoney = new ArrayList<>();
		float changeAmount = round(currentlyPaidAmount - currentProduct.getProductPrice(), 1);
		
		// keep adding change until change amount will reduce to 0, or there is no available money
		while (changeAmount > 0) {
			// pick highest available money, but less or equal than change itself
			Money change = determineChangeMoney(changeAmount, availableMoney);
			if (change == null) {
				// there is no money available for change, change releasing failed
				return false;
			}
			changeAmount = round(changeAmount - change.getValue(), 1);
			availableMoney.remove(change);
			changeMoney.add(change);
		}
		releaseMoney(changeMoney);
		return true;
	}

	private Money determineChangeMoney(float changeAmount, ArrayList<Money> availableMoney) {
		Money highestValue = null;
		for (Money money : availableMoney) {
			if (money.getValue() == changeAmount) {
				return money;
			}
			if (money.getValue() < changeAmount
					&& (highestValue == null || highestValue.getValue() < money.getValue())) {
				highestValue = money;
			}
		}
		return highestValue;
	}

	private void purchaseProduct() {
		shelves.get(currentProductSchelf).remove();
		deliverProduct(currentProduct);
		resetMachineState();
	}

	private void releaseMoney(List<Money> moneyList) {
		moneyList.forEach((money) -> {
			releaseMoney(money);
		});
	}

	private void displayRemainingAmount() {
		float remaining = round(currentProduct.getProductPrice() - currentlyPaidAmount, 1);
		displayMessage(REMAINING_AMOUNT + remaining);
	}

	private void displayProductPrice() {
		displayMessage(PRODUCT_PRICE + currentProduct.getProductPrice());
	}

	private void releaseMoney(Money money) {
		changeListeners.forEach((listener) -> {
			listener.changeReturned(money);
		});
	}

	private void deliverProduct(VendingProduct currentProduct) {
		dispenserListeners.forEach((listener) -> {
			listener.productDelivered(currentProduct);
		});
	}

	private void displayMessage(String message) {
		displayListeners.forEach((listener) -> {
			listener.newMessageShown(message);
		});
	}

	private void resetMachineState() {
		currentProductSchelf = -1;
		currentlyPaidAmount = 0;
		currentProduct = null;
		currentlyPaidMoney = new ArrayList<>();
	}

	private float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}
}
