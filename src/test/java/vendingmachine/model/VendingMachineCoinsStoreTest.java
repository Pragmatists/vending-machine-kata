package vendingmachine.model;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import vendingmachine.enums.CoinDenomination;

public class VendingMachineCoinsStoreTest {

	private VendingMachineCoinsStore vendingMachineCoinsStore = new VendingMachineCoinsStore();

	@Test
	public void test_differentMethods() throws Exception {

		vendingMachineCoinsStore.incrementCoinQuantity(CoinDenomination.DENOMINATION_5);
		vendingMachineCoinsStore.incrementCoinQuantity(CoinDenomination.DENOMINATION_2);
		vendingMachineCoinsStore.incrementCoinQuantity(CoinDenomination.DENOMINATION_1);
		vendingMachineCoinsStore.incrementCoinQuantity(CoinDenomination.DENOMINATION_05);
		vendingMachineCoinsStore.incrementCoinQuantity(CoinDenomination.DENOMINATION_02);
		vendingMachineCoinsStore.incrementCoinQuantity(CoinDenomination.DENOMINATION_01);

		Assertions.assertThat(vendingMachineCoinsStore.getCoinQuantity(CoinDenomination.DENOMINATION_5)).isEqualTo(1);
		Assertions.assertThat(vendingMachineCoinsStore.getCoinQuantity(CoinDenomination.DENOMINATION_2)).isEqualTo(1);
		Assertions.assertThat(vendingMachineCoinsStore.getCoinQuantity(CoinDenomination.DENOMINATION_1)).isEqualTo(1);
		Assertions.assertThat(vendingMachineCoinsStore.getCoinQuantity(CoinDenomination.DENOMINATION_05)).isEqualTo(1);
		Assertions.assertThat(vendingMachineCoinsStore.getCoinQuantity(CoinDenomination.DENOMINATION_02)).isEqualTo(1);
		Assertions.assertThat(vendingMachineCoinsStore.getCoinQuantity(CoinDenomination.DENOMINATION_01)).isEqualTo(1);
		Assertions.assertThat(vendingMachineCoinsStore.getCoinsQuantities()).isEqualTo(new int[] { 1, 1, 1, 1, 1, 1 });

		Assertions.assertThat(vendingMachineCoinsStore.calculateSumOfMoneyFromCurrentSessionCoinsQuantities()).isEqualTo(new BigDecimal("8.8"));

		Assertions.assertThat(vendingMachineCoinsStore.getCurrentSessionCoinsQuantities()).isEqualTo(new int[] { 1, 1, 1, 1, 1, 1 });
		vendingMachineCoinsStore.clearCurrentSessionCoinsStore();
		Assertions.assertThat(vendingMachineCoinsStore.getCoinsQuantities()).isEqualTo(new int[] { 1, 1, 1, 1, 1, 1 });
		Assertions.assertThat(vendingMachineCoinsStore.getCurrentSessionCoinsQuantities()).isEqualTo(new int[] { 0, 0, 0, 0, 0, 0 });
		Assertions.assertThat(vendingMachineCoinsStore.calculateSumOfMoneyFromCurrentSessionCoinsQuantities()).isEqualTo(new BigDecimal("0.0"));

		vendingMachineCoinsStore.incrementCoinQuantity(CoinDenomination.DENOMINATION_1);
		vendingMachineCoinsStore.incrementCoinQuantity(CoinDenomination.DENOMINATION_02);
		Assertions.assertThat(vendingMachineCoinsStore.calculateSumOfMoneyFromCurrentSessionCoinsQuantities()).isEqualTo(new BigDecimal("1.2"));
		Assertions.assertThat(vendingMachineCoinsStore.getCoinsQuantities()).isEqualTo(new int[] { 1, 1, 2, 1, 2, 1 });
		Assertions.assertThat(vendingMachineCoinsStore.getCurrentSessionCoinsQuantities()).isEqualTo(new int[] { 0, 0, 1, 0, 1, 0 });

	}

}
