package tdd.vendingMachine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import tdd.vendingMachine.enumeration.CoinsEnum;
import tdd.vendingMachine.parts.ChangeRegister;
import tdd.vendingMachine.parts.PaymentRegister;
import tdd.vendingMachine.parts.ProductByShelveRegister;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.services.CoinsRegisterMapFactory;
import tdd.vendingMachine.services.PaymentService;

public class VendingMachine {

	private ProductByShelveRegister productByShelveRegister;
	private ChangeRegister changeRegister;
	private PaymentRegister paymentRegister;
	private PaymentService paymentService;
	
	
	public VendingMachine(int shelvesNumber, int shelveCapacity, int initialCoinsQuantity) {
		productByShelveRegister = new ProductByShelveRegister(shelvesNumber, shelveCapacity);
		paymentRegister = new PaymentRegister(CoinsRegisterMapFactory.getEmptytCoinRegister());
		changeRegister = new ChangeRegister(CoinsRegisterMapFactory.getCoinRegister(initialCoinsQuantity), paymentRegister);
		paymentService = new PaymentService();
	}
	
	/**
	 * Resupply vending machine with different products
	 * If possible products will be placed in, if not will be returned
	 * @param products - products and their number to be placed in machine
	 * @return number of products that didn't fit machine
	 */
	public Map<Product, Integer> fillInProducts(Map<Product, Integer> products) {
		Map<Product, Integer> left = new HashMap<>();
		
		products.entrySet().forEach( entry -> {
			int returned = productByShelveRegister.addProducts(entry.getKey(), entry.getValue());
			left.put(entry.getKey(), returned);
		});
		
		return left;
	}
	
	/**
	 * Restock single product
	 * @param product
	 * @param number
	 * @return number of products that didn't fit into machine
	 */
	public Integer restockProduct(Product product, int number) {
		return productByShelveRegister.addProducts(product, number);
	}
	
	/**
	 * Add single coin to paymentRegister
	 * @param coin
	 */
	public void insertCoin(CoinsEnum coin) {
		paymentRegister.depositMoney(coin, 1);
	}
	
	/**
	 * Retrieve your money from paymentRegister
	 * @return
	 */
	public Map<CoinsEnum, Integer> cancel() {
		return paymentRegister.flush();
	}

	/**
	 * Sell product to customer if:
	 * 1) payment register holds enough money
	 * 2) it is possible to give change to client
	 * 
	 * change will be put to payment register, don't forget to take it back
	 * @param product
	 * @return selected product if preconditions are met, empty if not
	 */
	public Optional<Product> buy(Product product) {
		if (!productByShelveRegister.haveProduct(product)) {
			System.out.println("Missing product");
			return Optional.empty();
		}
		if (!paymentService.isEnoughToBuy(paymentRegister, product)) {
			System.out.println("Not enough money");
			return Optional.empty();
		}
		
		BigDecimal totalPayment = paymentService.getTotalPayment(paymentRegister);
		BigDecimal change = totalPayment.subtract(product.getPrice());
		Map<CoinsEnum, Integer> mergedCoinRegisterMap = CoinsRegisterMapFactory.mergeRegisters(changeRegister.getCoinsRegisterMap(), paymentRegister.getCoinsRegisterMap());
		Optional<Map<CoinsEnum, Integer>> changeMap = paymentService.getChange(mergedCoinRegisterMap, change);
		
		if (changeMap.isPresent()) {
			// successful transaction - now move money to change register, than put change in payment register
			changeRegister.depositMoney();
			changeRegister.subtractChange(changeMap.get());
			return productByShelveRegister.takeProduct(product);
		} else {
			System.out.println("Sorry but won't be able to give you change, cancelling transaction");
			return Optional.empty();
		}
	}
	
	/**
	 * After successfull transaction change will be put to payment register
	 * @return
	 */
	public Map<CoinsEnum, Integer> takeChange() {
		return paymentRegister.flush();
	}
	
	/**
	 * List all available products in machine
	 * @return
	 */
	public Set<Product> getProductSet() {
		return productByShelveRegister.listProducts();
	}
	
	
}
