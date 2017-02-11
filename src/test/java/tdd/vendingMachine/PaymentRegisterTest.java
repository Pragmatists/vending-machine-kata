package tdd.vendingMachine;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import tdd.vendingMachine.enumeration.CoinsEnum;
import tdd.vendingMachine.parts.PaymentRegister;
import tdd.vendingMachine.services.CoinsRegisterMapFactory;

public class PaymentRegisterTest {

	private PaymentRegister paymentRegister;
	
	
	@Before
	public void setUp() {
		paymentRegister = new PaymentRegister(CoinsRegisterMapFactory.getEmptytCoinRegister());
		paymentRegister.depositMoney(CoinsEnum.FIVE, 5);
		paymentRegister.depositMoney(CoinsEnum.ONE, 2);
		paymentRegister.depositMoney(CoinsEnum.POINT_TWO, 120);
	}
	
	@Test
	public void canDepositMoneyTest() {
		Map<CoinsEnum, Integer> coinsRegisterMap = paymentRegister.getCoinsRegisterMap();
		Assertions.assertThat(coinsRegisterMap.get(CoinsEnum.FIVE)).isEqualTo(5);
		Assertions.assertThat(coinsRegisterMap.get(CoinsEnum.ONE)).isEqualTo(2);
		Assertions.assertThat(coinsRegisterMap.get(CoinsEnum.POINT_TWO)).isEqualTo(120);
		Assertions.assertThat(coinsRegisterMap.get(CoinsEnum.POINT_ONE)).isEqualTo(0);
		Assertions.assertThat(coinsRegisterMap.get(CoinsEnum.HALF)).isEqualTo(0);
	}
	
	@Test(expected = AssertionError.class)
	public void canDepositOnlyPositiveNumberOfCoinsTest() {
		paymentRegister.depositMoney(CoinsEnum.FIVE, 0);
	}
	
	@Test
	public void canCancelTransactionTest() {
		paymentRegister.cancel();
		Map<CoinsEnum, Integer> coinsRegisterMap = paymentRegister.getCoinsRegisterMap();
		for (CoinsEnum coin : CoinsEnum.values()) {
			Assertions.assertThat(coinsRegisterMap.get(coin)).isEqualTo(0);
		}
	}
	
	@Test
	public void canFlushMoneyTest() {
		Map<CoinsEnum, Integer> coinsRegisterMap = paymentRegister.getCoinsRegisterMap();
		Map<CoinsEnum, Integer> flushedCoinsRegisterMap = paymentRegister.flush();
		Map<CoinsEnum, Integer> newCoinsRegisterMap = paymentRegister.getCoinsRegisterMap();
		Assertions.assertThat(flushedCoinsRegisterMap).isEqualTo(coinsRegisterMap);
		for (CoinsEnum coin : CoinsEnum.values()) {
			Assertions.assertThat(newCoinsRegisterMap.get(coin)).isEqualTo(0);
		}
	}
	
	
}
