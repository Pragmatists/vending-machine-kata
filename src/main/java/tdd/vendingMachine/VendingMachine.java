package tdd.vendingMachine;

import java.util.List;

import tdd.vendingMachine.listener.VendingMachineChangeListener;
import tdd.vendingMachine.listener.VendingMachineDispenserListener;
import tdd.vendingMachine.listener.VendingMachineDisplayListener;
import tdd.vendingMachine.money.Money;
import tdd.vendingMachine.product.VendingProduct;

public interface VendingMachine {
	
	void addProductsToStorage(List<VendingProduct> products);

	void addMoneyToStorage(List<Money> money);

	void addVendingMachineDisplayListener(VendingMachineDisplayListener listener);

	void addVendingMachineDispenserListener(VendingMachineDispenserListener listener);

	void addVendingMachineChangeListener(VendingMachineChangeListener listener);

	void chooseProductShelf(int schelfNumber);

	void putMoney(Money money);
	
	void cancelPurchase();
}
