package vendingmachine.calc;

import java.math.BigDecimal;
import java.util.Arrays;

import javax.enterprise.context.Dependent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vendingmachine.enums.CoinDenomination;
import vendingmachine.model.VendingMachineCoinsStore;
import vendingmachine.model.VendingMachineConstants;

@Dependent
public class ReturningChangePossibilityChecker {

	private static final Logger LOG = LogManager.getLogger(ReturningChangePossibilityChecker.class);

	public ReturningChangePossibilityResult check(BigDecimal moneyToReturn, VendingMachineCoinsStore currentCoinsStore) {

		LOG.info("Money to return: " + moneyToReturn);
		LOG.info("Current coins store: " + currentCoinsStore);

		int maxPossibleDenominationIndex = selectMaxPossibleDenominationIndex(moneyToReturn);
		boolean isReturningChangePossible = false;
		int[] coinsToReturn = new int[VendingMachineConstants.DENOMINATIONS_NR];
		zeroCoinsToReturn(coinsToReturn);

		FIRST_LOOP: for (int i = maxPossibleDenominationIndex; i < VendingMachineConstants.DENOMINATIONS_NR; ++i) {
			BigDecimal sum = new BigDecimal("0.0");
			zeroCoinsToReturn(coinsToReturn);

			SECOND_LOOP: for (int j = i; j < VendingMachineConstants.DENOMINATIONS_NR; ++j) {
				for (int k = 0; k < currentCoinsStore.getCoinQuantity(CoinDenomination.getByCode(j)); ++k) {
					sum = sum.add(VendingMachineConstants.COIN_DENOMINATION_VALUES[j]);
					if (sum.compareTo(moneyToReturn) == -1) {
						coinsToReturn[j] += 1;
					} else if (sum.compareTo(moneyToReturn) == 0) {
						coinsToReturn[j] += 1;
						isReturningChangePossible = true;
						break FIRST_LOOP;
					} else {
						sum = sum.subtract(VendingMachineConstants.COIN_DENOMINATION_VALUES[j]);
						coinsToReturn[j] = 0;
						continue SECOND_LOOP;
					}
				}
			}
		}

		if (!isReturningChangePossible) {
			zeroCoinsToReturn(coinsToReturn);
		}

		LOG.info("Is returning change possible: " + isReturningChangePossible);
		LOG.info("Coins to return: " + Arrays.toString(coinsToReturn));
		return new ReturningChangePossibilityResult(isReturningChangePossible, coinsToReturn);

	}

	private int selectMaxPossibleDenominationIndex(BigDecimal moneyToReturn) {
		int maxPossibleDenominationIndex = 0;
		for (int i = 0; i < VendingMachineConstants.DENOMINATIONS_NR; ++i) {
			int compareResult = moneyToReturn.compareTo(VendingMachineConstants.COIN_DENOMINATION_VALUES[i]);
			if (compareResult == 0 || compareResult == 1) {
				maxPossibleDenominationIndex = i;
				break;
			}
		}
		LOG.info("Max possible denomination index: {}", maxPossibleDenominationIndex);
		return maxPossibleDenominationIndex;
	}

	private void zeroCoinsToReturn(int[] coinsToReturn) {
		for (int i = 0; i < coinsToReturn.length; ++i) {
			coinsToReturn[i] = 0;
		}
	}

}
