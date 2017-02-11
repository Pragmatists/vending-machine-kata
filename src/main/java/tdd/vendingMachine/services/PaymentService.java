package tdd.vendingMachine.services;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tdd.vendingMachine.enumeration.CoinsEnum;
import tdd.vendingMachine.parts.PaymentRegister;
import tdd.vendingMachine.product.Product;

/**
 * Method used to calculate results of payment operations, like calculate change, 
 * check if change can be given, check if enough money was deposited to buy product
 */
public class PaymentService {

	/**
	 * Collect all coins in register and return its total value
	 * @param paymentRegister
	 * @return total value of coins in payment register
	 */
	public BigDecimal getTotalPayment(PaymentRegister paymentRegister) {
		return paymentRegister.getCoinsRegisterMap().entrySet().stream()
				.map(entry -> entry.getKey().getValue().multiply( new BigDecimal(entry.getValue()) ))
				.reduce((bd1, bd2) -> bd1.add(bd2)).get();
	}
	
	/**
	 * Recursive calculation of returned change coins permutation.
	 * Change will be given from merged payment and change registers.
	 * The strategy is to first give away highest nominal coins, 
	 * so for 6.0 change, if possible returned set will be equal to {5.0, 1.0}
	 * if register miss this coins, service will try to look for other permutation, ie {2.0, 2.0, 2.0}
	 * @param paymentRegister - vending machine register holding client money
	 * @param changeRegister  - vending machine coin register
	 * @param price - product price
	 * @return map of returned change coins if it is possible to give change, otherwis empty optional
	 */
	public Optional<Map<CoinsEnum, Integer>> getChange(Map<CoinsEnum, Integer> coinRegisterMap, BigDecimal change) {
		List<CoinsEnum> coinSequence = CoinsRegisterMapFactory.coinRegisterMapToList(coinRegisterMap);
		return recursiveChange(coinSequence, CoinsRegisterMapFactory.getEmptytCoinRegister(), change, new BigDecimal("0.0"));
	}
	
	/**
	 * Test if it is possible to give change to client
	 * @param paymentRegister
	 * @param changeRegister
	 * @return
	 */
	public boolean canGiveChange(Map<CoinsEnum, Integer> coinRegisterMap, BigDecimal change) {
		
		return getChange(coinRegisterMap, change).isPresent();
	}
	
	/**
	 * Check if payment register has been deposited with enough money to buy product
	 * @param paymentRegister
	 * @param product
	 * @return
	 */
	public boolean isEnoughToBuy(PaymentRegister paymentRegister, Product product) {
		return product.getPrice().compareTo(getTotalPayment(paymentRegister)) <= 0;
	}
	
	
	/* ======================================================================================
	 * 								PRIVATE METHODS
	 * ======================================================================================*/
	/**
	 * Recursive method trying to find permutation of coins that will be equal to requested change 
	 * @param registerCoinsList
	 * @param acc
	 * @param price
	 * @param totalValueAcc
	 * @return
	 */
	private Optional<Map<CoinsEnum, Integer>> recursiveChange(List<CoinsEnum> registerCoinsList, 
													Map<CoinsEnum, Integer> acc, 
													BigDecimal change, BigDecimal totalValueAcc) {
//		System.out.printf("Initial: list = %s, acc = %s, totalValue = %s\n", registerCoinsList.toString(), acc.toString(), totalValueAcc.toString());
		// recursion termination condition
		if (totalValueAcc.compareTo(change) == 0) return Optional.of(acc);
		if (totalValueAcc.compareTo(change) > 0) return Optional.empty();
		if (registerCoinsList.isEmpty()) return Optional.empty();
		
		// recursive function
		//	need to call recursion twice - one with first element of registerCoinsList, second time without it
		CoinsEnum coin = registerCoinsList.remove(0);
		List<CoinsEnum> registerCoinsListCopy = new LinkedList<>(registerCoinsList);
		addToRegisterMap(coin, acc);
		Optional<Map<CoinsEnum, Integer>> result = recursiveChange(registerCoinsList, acc, change, totalValueAcc.add(coin.getValue()));
		
		if (result.isPresent()) return result;
		removeFromRegisterMap(coin, acc);
		result = recursiveChange(registerCoinsListCopy, acc, change, totalValueAcc);
		return result;
	}
	
	
	/**
	 * In coin register map, decrement quantity of given coin if possible
	 * @param coin
	 * @param registerMap
	 * @return
	 */
	private boolean removeFromRegisterMap(CoinsEnum coin, Map<CoinsEnum, Integer> registerMap) {
		if (registerMap.get(coin) == 0) return false;
		registerMap.compute(coin, (c, quantity) -> quantity - 1);
		return true;
	}
	
	/**
	 * In coin register map, increment quantity of given coin 
	 * @param coin
	 * @param registerMap
	 * @return
	 */
	private void addToRegisterMap(CoinsEnum coin, Map<CoinsEnum, Integer> registerMap) {
		registerMap.compute(coin, (c, quantity) -> quantity + 1);
	}
}
