package vendingmachine.model;

import java.math.BigDecimal;
import java.util.Arrays;

import javax.enterprise.context.Dependent;

import vendingmachine.enums.CoinDenomination;

@Dependent
public class VendingMachineCoinsStore {
	private int[] coinsQuantities = new int[VendingMachineConstants.DENOMINATIONS_NR];
	private int[] currentSessionCoinsQuantities = new int[VendingMachineConstants.DENOMINATIONS_NR];

	public int getCoinQuantity(CoinDenomination coinDenomination) {
		return coinsQuantities[coinDenomination.getCode()];
	}

	public int setCoinQuantity(CoinDenomination coinDenomination, int coinQuantity) {
		return coinsQuantities[coinDenomination.getCode()] = coinQuantity;
	}

	public void incrementCoinQuantity(CoinDenomination coinDenomination) {
		coinsQuantities[coinDenomination.getCode()] += 1;
		currentSessionCoinsQuantities[coinDenomination.getCode()] += 1;
	}

	public void subtractCoinQuantity(CoinDenomination coinDenomination, int subtractQuantity) {
		coinsQuantities[coinDenomination.getCode()] -= subtractQuantity;
	}

	public int[] getCoinsQuantities() {
		return coinsQuantities;
	}

	public int[] getCurrentSessionCoinsQuantities() {
		return currentSessionCoinsQuantities;
	}

	public BigDecimal calculateSumOfMoneyFromCurrentSessionCoinsQuantities() {
		BigDecimal sum = new BigDecimal("0.0");
		for (int i = 0; i < currentSessionCoinsQuantities.length; ++i) {
			if (currentSessionCoinsQuantities[i] != 0) {
				sum = sum.add(VendingMachineConstants.COIN_DENOMINATION_VALUES[i].multiply(new BigDecimal(currentSessionCoinsQuantities[i])));
			}
		}
		return sum;
	}

	public void clearCurrentSessionCoinsStore() {
		for (int i = 0; i < VendingMachineConstants.DENOMINATIONS_NR; ++i) {
			currentSessionCoinsQuantities[i] = 0;
		}
	}

	public String toString() {
		return "Coins quantities in vending machine: " + Arrays.toString(coinsQuantities) + "; Coins quantities inserted in current session: "
				+ Arrays.toString(currentSessionCoinsQuantities);
	}
}
