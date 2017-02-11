package tdd.vendingMachine;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import tdd.vendingMachine.enumeration.CoinsEnum;
import tdd.vendingMachine.parts.ChangeRegister;
import tdd.vendingMachine.parts.PaymentRegister;
import tdd.vendingMachine.services.CoinsRegisterMapFactory;

public class ChangeRegisterTest {

	private ChangeRegister changeRegister;
	private PaymentRegister paymentRegister;
	
	@Before
	public void setUp() {
		paymentRegister = new PaymentRegister(CoinsRegisterMapFactory.getEmptytCoinRegister());
		changeRegister = new ChangeRegister(CoinsRegisterMapFactory.getCoinRegister(1), paymentRegister);
	}
	
	@Test
	public void shouldAcceptMoneyFromPaymentTest() {
		paymentRegister.depositMoney(CoinsEnum.FIVE, 1);
		paymentRegister.depositMoney(CoinsEnum.TWO, 2);
		
		changeRegister.depositMoney();
		
		Map<CoinsEnum, Integer> paymentsExpected = CoinsRegisterMapFactory.getEmptytCoinRegister();
		Map<CoinsEnum, Integer> changeExpected = CoinsRegisterMapFactory.getCoinRegister(1);
		changeExpected.put(CoinsEnum.FIVE, 2);
		changeExpected.put(CoinsEnum.TWO, 3);
		
		Assertions.assertThat(changeRegister.getCoinsRegisterMap()).isEqualTo(changeExpected);
		Assertions.assertThat(paymentRegister.getCoinsRegisterMap()).isEqualTo(paymentsExpected);
	}
	
	@Test
	public void shouldGiveChangeTest() {
		
		Map<CoinsEnum, Integer> expectedChangeMap = CoinsRegisterMapFactory.getEmptytCoinRegister();
		expectedChangeMap.put(CoinsEnum.FIVE, 1);
		expectedChangeMap.put(CoinsEnum.ONE, 1);
		
		Map<CoinsEnum, Integer> expectedChangeRegisterMap = CoinsRegisterMapFactory.getCoinRegister(1);
		expectedChangeRegisterMap.put(CoinsEnum.FIVE, 0);
		expectedChangeRegisterMap.put(CoinsEnum.ONE, 0);
		
		changeRegister.subtractChange(expectedChangeMap);
		
		Assertions.assertThat(changeRegister.getCoinsRegisterMap()).isEqualTo(expectedChangeRegisterMap);
		Assertions.assertThat(paymentRegister.getCoinsRegisterMap()).isEqualTo(expectedChangeMap);
	}
}
