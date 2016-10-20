package vendingmachine.calc;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import vendingmachine.enums.CoinDenomination;
import vendingmachine.model.VendingMachineCoinsStore;
import vendingmachine.model.VendingMachineConstants;

public class ReturningChangePossibilityCheckerTest {

	private ReturningChangePossibilityChecker returningChangePossibilityChecker = new ReturningChangePossibilityChecker();

	@Test
	public void test_check_emptyCoinsStore() throws Exception {
		ReturningChangePossibilityResult result = returningChangePossibilityChecker.check(new BigDecimal("0.1"), prepareCoinsStore(0, 0, 0, 0, 0, 0));
		Assertions.assertThat(result.isReturningChangePossible()).isFalse();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 0, 0, 0, 0, 0, 0 });
	}

	@Test
	public void test_check_oneCoinOfEveryDenominationInCoinsStore_changeFrom5() throws Exception {
		ReturningChangePossibilityResult result = returningChangePossibilityChecker.check(new BigDecimal("5.0"), prepareCoinsStore(1, 1, 1, 1, 1, 1));
		Assertions.assertThat(result.isReturningChangePossible()).isTrue();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 1, 0, 0, 0, 0, 0 });
	}

	@Test
	public void test_check_oneCoinOfEveryDenominationInCoinsStore_changeFrom7() throws Exception {
		ReturningChangePossibilityResult result = returningChangePossibilityChecker.check(new BigDecimal("7.0"), prepareCoinsStore(1, 1, 1, 1, 1, 1));
		Assertions.assertThat(result.isReturningChangePossible()).isTrue();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 1, 1, 0, 0, 0, 0 });
	}

	@Test
	public void test_check_oneCoinOfEveryDenominationInCoinsStore_changeFrom8() throws Exception {
		ReturningChangePossibilityResult result = returningChangePossibilityChecker.check(new BigDecimal("8.0"), prepareCoinsStore(1, 1, 1, 1, 1, 1));
		Assertions.assertThat(result.isReturningChangePossible()).isTrue();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 1, 1, 1, 0, 0, 0 });
	}

	@Test
	public void test_check_oneCoinOfEveryDenominationInCoinsStore_changeFrom8point5() throws Exception {
		ReturningChangePossibilityResult result = returningChangePossibilityChecker.check(new BigDecimal("8.5"), prepareCoinsStore(1, 1, 1, 1, 1, 1));
		Assertions.assertThat(result.isReturningChangePossible()).isTrue();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 1, 1, 1, 1, 0, 0 });
	}

	@Test
	public void test_check_oneCoinOfEveryDenominationInCoinsStore_changeFrom8point7() throws Exception {
		ReturningChangePossibilityResult result = returningChangePossibilityChecker.check(new BigDecimal("8.7"), prepareCoinsStore(1, 1, 1, 1, 1, 1));
		Assertions.assertThat(result.isReturningChangePossible()).isTrue();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 1, 1, 1, 1, 1, 0 });
	}

	@Test
	public void test_check_oneCoinOfEveryDenominationInCoinsStore_changeFrom8point8() throws Exception {
		ReturningChangePossibilityResult result = returningChangePossibilityChecker.check(new BigDecimal("8.8"), prepareCoinsStore(1, 1, 1, 1, 1, 1));
		Assertions.assertThat(result.isReturningChangePossible()).isTrue();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 1, 1, 1, 1, 1, 1 });
	}

	@Test
	public void test_check_severalCoinsOfOneDenominationInCoinsStore_changeFrom15() throws Exception {
		ReturningChangePossibilityResult result = returningChangePossibilityChecker.check(new BigDecimal("15"), prepareCoinsStore(3, 0, 0, 0, 0, 0));
		Assertions.assertThat(result.isReturningChangePossible()).isTrue();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 3, 0, 0, 0, 0, 0 });
	}

	@Test
	public void test_check_severalCoinsOfOneDenominationInCoinsStore_changeFrom6() throws Exception {
		ReturningChangePossibilityResult result = returningChangePossibilityChecker.check(new BigDecimal("6"), prepareCoinsStore(0, 3, 0, 0, 0, 0));
		Assertions.assertThat(result.isReturningChangePossible()).isTrue();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 0, 3, 0, 0, 0, 0 });
	}

	@Test
	public void test_check_severalCoinsOfOneDenominationInCoinsStore_changeFrom3() throws Exception {
		ReturningChangePossibilityResult result = returningChangePossibilityChecker.check(new BigDecimal("3"), prepareCoinsStore(0, 0, 3, 0, 0, 0));
		Assertions.assertThat(result.isReturningChangePossible()).isTrue();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 0, 0, 3, 0, 0, 0 });
	}

	@Test
	public void test_check_severalCoinsOfOneDenominationInCoinsStore_changeFrom1point5() throws Exception {
		ReturningChangePossibilityResult result = returningChangePossibilityChecker.check(new BigDecimal("1.5"), prepareCoinsStore(0, 0, 0, 3, 0, 0));
		Assertions.assertThat(result.isReturningChangePossible()).isTrue();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 0, 0, 0, 3, 0, 0 });
	}

	@Test
	public void test_check_severalCoinsOfOneDenominationInCoinsStore_changeFrom0point6() throws Exception {
		ReturningChangePossibilityResult result = returningChangePossibilityChecker.check(new BigDecimal("0.6"), prepareCoinsStore(0, 0, 0, 0, 3, 0));
		Assertions.assertThat(result.isReturningChangePossible()).isTrue();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 0, 0, 0, 0, 3, 0 });
	}

	@Test
	public void test_check_severalCoinsOfOneDenominationInCoinsStore_changeFrom0point3() throws Exception {
		ReturningChangePossibilityResult result = returningChangePossibilityChecker.check(new BigDecimal("0.3"), prepareCoinsStore(0, 0, 0, 0, 0, 3));
		Assertions.assertThat(result.isReturningChangePossible()).isTrue();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 0, 0, 0, 0, 0, 3 });
	}

	@Test
	public void test_check_changeConsistingOfDifferentCoinsDenominations() throws Exception {
		ReturningChangePossibilityResult result = returningChangePossibilityChecker.check(new BigDecimal("1.5"), prepareCoinsStore(0, 1, 4, 1, 0, 0));
		Assertions.assertThat(result.isReturningChangePossible()).isTrue();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 0, 0, 1, 1, 0, 0 });

		result = returningChangePossibilityChecker.check(new BigDecimal("9.7"), prepareCoinsStore(5, 3, 4, 2, 1, 0));
		Assertions.assertThat(result.isReturningChangePossible()).isTrue();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 1, 2, 0, 1, 1, 0 });

	}

	@Test
	public void test_check_noChangeFromMixedCoinsStore() throws Exception {
		ReturningChangePossibilityResult result = returningChangePossibilityChecker.check(new BigDecimal("0.3"), prepareCoinsStore(0, 0, 0, 0, 2, 0));
		Assertions.assertThat(result.isReturningChangePossible()).isFalse();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 0, 0, 0, 0, 0, 0 });

		result = returningChangePossibilityChecker.check(new BigDecimal("1.0"), prepareCoinsStore(0, 0, 0, 1, 1, 1));
		Assertions.assertThat(result.isReturningChangePossible()).isFalse();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 0, 0, 0, 0, 0, 0 });

		result = returningChangePossibilityChecker.check(new BigDecimal("10"), prepareCoinsStore(1, 1, 1, 0, 0, 3));
		Assertions.assertThat(result.isReturningChangePossible()).isFalse();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 0, 0, 0, 0, 0, 0 });

		result = returningChangePossibilityChecker.check(new BigDecimal("5"), prepareCoinsStore(0, 2, 0, 0, 0, 3));
		Assertions.assertThat(result.isReturningChangePossible()).isFalse();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 0, 0, 0, 0, 0, 0 });

		result = returningChangePossibilityChecker.check(new BigDecimal("7.5"), prepareCoinsStore(1, 1, 0, 0, 1, 1));
		Assertions.assertThat(result.isReturningChangePossible()).isFalse();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 0, 0, 0, 0, 0, 0 });

		result = returningChangePossibilityChecker.check(new BigDecimal("6"), prepareCoinsStore(0, 2, 1, 0, 0, 0));
		Assertions.assertThat(result.isReturningChangePossible()).isFalse();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 0, 0, 0, 0, 0, 0 });
	}

	@Test
	public void test_check_changeFromMixedCoinsStore() throws Exception {

		ReturningChangePossibilityResult result;

		result = returningChangePossibilityChecker.check(new BigDecimal("0.6"), prepareCoinsStore(1, 1, 1, 1, 0, 1));
		Assertions.assertThat(result.isReturningChangePossible()).isTrue();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 0, 0, 0, 1, 0, 1 });

		result = returningChangePossibilityChecker.check(new BigDecimal("0.6"), prepareCoinsStore(1, 2, 0, 2, 3, 0));
		Assertions.assertThat(result.isReturningChangePossible()).isTrue();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 0, 0, 0, 0, 3, 0 });

		result = returningChangePossibilityChecker.check(new BigDecimal("3.5"), prepareCoinsStore(5, 1, 1, 5, 0, 0));
		Assertions.assertThat(result.isReturningChangePossible()).isTrue();
		assertCoinsToReturn(result.getCoinsToReturn(), new int[] { 0, 1, 1, 1, 0, 0 });

	}

	private VendingMachineCoinsStore prepareCoinsStore(int quantityDenom5, int quantityDenom2, int quantityDenom1, int quantityDenom05, int quantityDenom02, int quantityDenom01) {
		VendingMachineCoinsStore vendingMachineCoinsStore = new VendingMachineCoinsStore();
		vendingMachineCoinsStore.setCoinQuantity(CoinDenomination.DENOMINATION_5, quantityDenom5);
		vendingMachineCoinsStore.setCoinQuantity(CoinDenomination.DENOMINATION_2, quantityDenom2);
		vendingMachineCoinsStore.setCoinQuantity(CoinDenomination.DENOMINATION_1, quantityDenom1);
		vendingMachineCoinsStore.setCoinQuantity(CoinDenomination.DENOMINATION_05, quantityDenom05);
		vendingMachineCoinsStore.setCoinQuantity(CoinDenomination.DENOMINATION_02, quantityDenom02);
		vendingMachineCoinsStore.setCoinQuantity(CoinDenomination.DENOMINATION_01, quantityDenom01);
		return vendingMachineCoinsStore;
	}

	private void assertCoinsToReturn(int[] coinsToReturnActual, int[] coinsToReturnExpected) {
		for (int i = 0; i < VendingMachineConstants.DENOMINATIONS_NR; ++i) {
			Assertions.assertThat(coinsToReturnActual[i]).isEqualTo(coinsToReturnExpected[i]);
		}
	}

}
