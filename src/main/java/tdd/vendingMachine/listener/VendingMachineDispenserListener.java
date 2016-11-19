package tdd.vendingMachine.listener;

import tdd.vendingMachine.product.VendingProduct;

public interface VendingMachineDispenserListener {
	void productDelivered(VendingProduct product);
}
