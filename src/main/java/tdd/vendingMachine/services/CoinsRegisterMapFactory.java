package tdd.vendingMachine.services;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import tdd.vendingMachine.enumeration.CoinsEnum;


/**
 * Service used to state of coin register
 * Coin register state is a map which keys are all possible coins mapped to their number
 */
public class CoinsRegisterMapFactory {
	
	/**
	 * Default method returning 10 pieces for each coin
	 * @return
	 */
	public static Map<CoinsEnum, Integer> getDefaultCoinRegister() {
		return getCoinRegister(10);
	}
	
	/**
	 * Register with random number of each coin
	 * @param minCoinQuantity - lower bound
	 * @param maxCoinQuantity - upper bound
	 * @return
	 */
	public static Map<CoinsEnum, Integer> getRandomCoinRegister(int minCoinQuantity, int maxCoinQuantity) {
		assert(minCoinQuantity >= 0);
		assert(maxCoinQuantity > minCoinQuantity);
		
		Random r = new Random();
		Map<CoinsEnum, Integer> register = new HashMap<>();
		for (CoinsEnum coin : CoinsEnum.values()) {
			int quantity = r.nextInt(maxCoinQuantity - minCoinQuantity) + minCoinQuantity;
			register.put(coin, quantity);
		}
		
		return register;
	}
	
	/**
	 * Register with random number of each coin in range 0 to maxCoinQuantity.
	 * @param maxCoinQuantity - upper bound
	 * @return
	 */
	public static Map<CoinsEnum, Integer> getRandomCoinRegister(int maxCoinQuantity) {
		return getRandomCoinRegister(0, maxCoinQuantity);
	}
	
	/**
	 * Register with random number of each coin in range 0 to 10.
	 * @return
	 */
	public static Map<CoinsEnum, Integer> getRandomCoinRegister() {
		return getRandomCoinRegister(0, 10);
	}
	
	/**
	 * Empty register. Can be used to initialize PaymentRegister.
	 * @return
	 */
	public static Map<CoinsEnum, Integer> getEmptytCoinRegister() {
		return getCoinRegister(0);
	}
	
	/**
	 * Register with equal number of each coin equal to initialCoinQuantity
	 * @param initialCoinQuantity
	 * @return
	 */
	public static Map<CoinsEnum, Integer> getCoinRegister(int initialCoinQuantity) {
		assert(initialCoinQuantity >= 0);
		Map<CoinsEnum, Integer> register = new HashMap<>();
		for (CoinsEnum coin : CoinsEnum.values()) {
			register.put(coin, initialCoinQuantity);
		}
		
		return register;
	}
	
	/**
	 * Merge 2 register maps by adding both maps appropriate values.
	 * @param that
	 * @param other
	 * @return new Map
	 */
	public static Map<CoinsEnum, Integer> mergeRegisters(final Map<CoinsEnum, Integer> that, final Map<CoinsEnum, Integer> other) {
		Map<CoinsEnum, Integer> newRegisterMap = new HashMap<>();
		that.entrySet().stream().forEach(it -> {
			newRegisterMap.merge(it.getKey(), it.getValue(), (q1, q2) -> q1+q2);
		});
		other.entrySet().stream().forEach(it -> {
			newRegisterMap.merge(it.getKey(), it.getValue(), (q1, q2) -> q1+q2);
		});
		
		return newRegisterMap;
	}
	
	/**
	 * Convert map to linked list starting from highest value coins
	 * Each list element corresponds to 1 coin in register, 
	 * so register with two 5.0 coins will put this coin twice in the list
	 * @return
	 */
	public static List<CoinsEnum> coinRegisterMapToList(Map<CoinsEnum, Integer> coinRegisterMap) {
		Comparator<CoinsEnum> comparator = (c1, c2) -> c2.getValue().compareTo(c1.getValue());
		List<CoinsEnum> coins = coinRegisterMap.entrySet().stream()
					.filter(entry -> entry.getValue() > 0 )
					.map(entry -> entry.getKey())
					.sorted(comparator)
					.collect(Collectors.toList());
		List<CoinsEnum> result = new LinkedList<>();
		for (CoinsEnum coin : coins) {
			for (int i = 0; i < coinRegisterMap.get(coin); i++)
				result.add(coin);
		}
		return result;
	}
	
	/**
	 * Subtract coins from register
	 * @return
	 */
	public static Map<CoinsEnum, Integer> subtract(Map<CoinsEnum, Integer> minuend, Map<CoinsEnum, Integer> subtrahend) {
		Map<CoinsEnum, Integer> newRegisterMap = new HashMap<>();
		minuend.entrySet().stream().forEach(it -> {
			newRegisterMap.merge(it.getKey(), it.getValue(), (q1, q2) -> q1+q2);
		});
		subtrahend.entrySet().stream().forEach(it -> {
			newRegisterMap.merge(it.getKey(), it.getValue(), (q1, q2) -> q1-q2);
		});
		
		return newRegisterMap;
	}
}
