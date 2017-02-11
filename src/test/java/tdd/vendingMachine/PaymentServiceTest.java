package tdd.vendingMachine;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import tdd.vendingMachine.enumeration.CoinsEnum;
import tdd.vendingMachine.enumeration.ProductsEnum;
import tdd.vendingMachine.parts.ChangeRegister;
import tdd.vendingMachine.parts.PaymentRegister;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.services.CoinsRegisterMapFactory;
import tdd.vendingMachine.services.PaymentService;

public class PaymentServiceTest {
	
	private PaymentRegister paymentRegister;
	private PaymentRegister emptyPaymentRegister;
	private ChangeRegister emptyChangeRegister;
	private static PaymentService paymentService;
	
	@BeforeClass
	public static void setUpOnce() {
		paymentService = new PaymentService();
	}
	
	@Before
	public void setUp() {
		paymentRegister = new PaymentRegister(CoinsRegisterMapFactory.getCoinRegister(10));
		emptyPaymentRegister = new PaymentRegister(CoinsRegisterMapFactory.getEmptytCoinRegister());
		emptyChangeRegister = new ChangeRegister(CoinsRegisterMapFactory.getEmptytCoinRegister(), emptyPaymentRegister);
	}
	
	@Test
	public void calculateTotalValueTest() {
		BigDecimal expected = new BigDecimal("0.0");
		for (CoinsEnum coin : CoinsEnum.values()) {
			expected = expected.add(coin.getValue().multiply(new BigDecimal(10)));
		}
		Assertions.assertThat(paymentService.getTotalPayment(paymentRegister)).isEqualByComparingTo(expected);
	}
	
	@Test
	public void calculateTotalValue2Test() {
		BigDecimal expected = new BigDecimal("2.5");
		emptyPaymentRegister.depositMoney(CoinsEnum.ONE, 1);
		emptyPaymentRegister.depositMoney(CoinsEnum.HALF, 2);
		emptyPaymentRegister.depositMoney(CoinsEnum.POINT_TWO, 2);
		emptyPaymentRegister.depositMoney(CoinsEnum.POINT_ONE, 1);
		Assertions.assertThat(paymentService.getTotalPayment(emptyPaymentRegister)).isEqualByComparingTo(expected);
	}
	
	@Test
	public void calculateTotalValueForEmptyRegisterTest() {
		Assertions.assertThat(paymentService.getTotalPayment(emptyPaymentRegister)).isEqualByComparingTo(new BigDecimal("0.0"));
	}
	
	@Test
	public void thereIsEnoughMoneyToBuyProductTest() {
		Product chips = ProductsEnum.CHIPS.getProduct();
		Assertions.assertThat(paymentService.isEnoughToBuy(paymentRegister, chips)).isTrue();
	}
	
	@Test
	public void thereIsNotEnoughMoneyToBuyProductTest() {
		Product chips = ProductsEnum.CHIPS.getProduct();
		Assertions.assertThat(paymentService.isEnoughToBuy(emptyPaymentRegister, chips)).isFalse();
	}
	
	@Test
	public void canGetChangeTest() {
		emptyPaymentRegister.depositMoney(CoinsEnum.FIVE, 2);
		emptyPaymentRegister.depositMoney(CoinsEnum.TWO, 2);
		emptyPaymentRegister.depositMoney(CoinsEnum.HALF, 1);
		emptyPaymentRegister.depositMoney(CoinsEnum.POINT_TWO, 1);
		emptyPaymentRegister.depositMoney(CoinsEnum.POINT_ONE, 1);
		
		Map<CoinsEnum, Integer> mergedRegister = CoinsRegisterMapFactory.mergeRegisters(emptyChangeRegister.getCoinsRegisterMap(), emptyPaymentRegister.getCoinsRegisterMap());
		boolean result = paymentService.canGiveChange(mergedRegister, new BigDecimal("7.8"));
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void cantGetChangeTest() {
		emptyPaymentRegister.depositMoney(CoinsEnum.FIVE, 2);
		emptyPaymentRegister.depositMoney(CoinsEnum.TWO, 2);
		emptyPaymentRegister.depositMoney(CoinsEnum.HALF, 1);
		emptyPaymentRegister.depositMoney(CoinsEnum.POINT_TWO, 1);
		emptyPaymentRegister.depositMoney(CoinsEnum.POINT_ONE, 1);
		
		Map<CoinsEnum, Integer> mergedRegister = CoinsRegisterMapFactory.mergeRegisters(emptyChangeRegister.getCoinsRegisterMap(), emptyPaymentRegister.getCoinsRegisterMap());
		boolean result = paymentService.canGiveChange(mergedRegister, new BigDecimal("6.0"));
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void changeShouldBeReturnedWithFirstCoinTest() {
		emptyPaymentRegister.depositMoney(CoinsEnum.FIVE, 2);
		emptyPaymentRegister.depositMoney(CoinsEnum.TWO, 1);
		emptyPaymentRegister.depositMoney(CoinsEnum.HALF, 1);
		
		Map<CoinsEnum, Integer> expected = CoinsRegisterMapFactory.getEmptytCoinRegister();
		expected.put(CoinsEnum.FIVE, 1);
		expected.put(CoinsEnum.TWO, 1);
		expected.put(CoinsEnum.HALF, 1);
		Map<CoinsEnum, Integer> mergedRegister = CoinsRegisterMapFactory.mergeRegisters(emptyChangeRegister.getCoinsRegisterMap(), emptyPaymentRegister.getCoinsRegisterMap());
		Optional<Map<CoinsEnum, Integer>> optionalResult = paymentService.getChange(mergedRegister, new BigDecimal("7.5"));
		Assertions.assertThat(optionalResult).contains(expected);
	}
	
	@Test
	public void changeShouldBeReturnedWithoutFirstCoinTest() {
		emptyPaymentRegister.depositMoney(CoinsEnum.FIVE, 1);
		emptyPaymentRegister.depositMoney(CoinsEnum.TWO, 3);
		emptyPaymentRegister.depositMoney(CoinsEnum.HALF, 1);
		
		Map<CoinsEnum, Integer> expected = CoinsRegisterMapFactory.getEmptytCoinRegister();
		expected.put(CoinsEnum.TWO, 3);
		Map<CoinsEnum, Integer> mergedRegister = CoinsRegisterMapFactory.mergeRegisters(emptyChangeRegister.getCoinsRegisterMap(), emptyPaymentRegister.getCoinsRegisterMap());
		Optional<Map<CoinsEnum, Integer>> optionalResult = paymentService.getChange(mergedRegister, new BigDecimal("6.0"));
		Assertions.assertThat(optionalResult).contains(expected);
	}
	
	@Test
	public void noChangeShouldBeReturnedTest() {
		emptyPaymentRegister.depositMoney(CoinsEnum.FIVE, 2);
		emptyPaymentRegister.depositMoney(CoinsEnum.TWO, 2);
		emptyPaymentRegister.depositMoney(CoinsEnum.HALF, 1);
		emptyPaymentRegister.depositMoney(CoinsEnum.POINT_TWO, 1);
		emptyPaymentRegister.depositMoney(CoinsEnum.POINT_ONE, 1);
		
		Map<CoinsEnum, Integer> mergedRegister = CoinsRegisterMapFactory.mergeRegisters(emptyChangeRegister.getCoinsRegisterMap(), emptyPaymentRegister.getCoinsRegisterMap());
		Optional<Map<CoinsEnum, Integer>> optionalResult = paymentService.getChange(mergedRegister, new BigDecimal("6.0"));
		Assertions.assertThat(optionalResult).isEmpty();
	}

}
