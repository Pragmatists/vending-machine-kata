package tdd.vendingMachine.state;

import info.solidsoft.mockito.java8.api.WithBDDMockito;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.Test;
import tdd.vendingMachine.CoinCalculator;
import tdd.vendingMachine.Product;
import tdd.vendingMachine.VendingMachine;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class ChangeCheckStateTest implements WithBDDMockito {

    @Test
    public void should_return_money_when_no_enough_coins_for_change() throws Exception {
        // given
        BigDecimal insertedAmount = new BigDecimal("5");
        Product selectedProduct = Product.KITKAT;
        BigDecimal change = insertedAmount.subtract(selectedProduct.getPrice());
        int selectedShelfNumber = 1;

        // setup
        CoinCalculator coinCalculatorMock = mock(CoinCalculator.class);
        given(coinCalculatorMock.hasCoinsForValue(eq(change))).willReturn(false);

        VendingMachine vendingMachineMock = mock(VendingMachine.class);
        given(vendingMachineMock.getProductInfo(eq(selectedShelfNumber))).willReturn(Optional.of(Product.KITKAT));
        given(vendingMachineMock.calculator()).willReturn(coinCalculatorMock);
        given(vendingMachineMock.setState(isA(ReturnMoneyState.class))).willReturn(vendingMachineMock);

        VendingMachineState vendingMachineState = new ChangeCheckState(insertedAmount, selectedShelfNumber);
        // when
        vendingMachineState.proceed(vendingMachineMock);
        // then
        verify(vendingMachineMock).proceed();
    }

    @Test
    public void should_remove_change_from_machine_wallet_and_provide_selected_product() {
        // given
        BigDecimal insertedAmount = new BigDecimal("5");
        Product selectedProduct = Product.KITKAT;
        BigDecimal change = insertedAmount.subtract(selectedProduct.getPrice());
        int selectedShelfNumber = 1;

        // setup
        CoinCalculator coinCalculatorMock = mock(CoinCalculator.class);
        given(coinCalculatorMock.hasCoinsForValue(eq(change))).willReturn(true);

        VendingMachine vendingMachineMock = mock(VendingMachine.class);
        given(vendingMachineMock.getProductInfo(eq(selectedShelfNumber))).willReturn(Optional.of(Product.KITKAT));
        given(vendingMachineMock.calculator()).willReturn(coinCalculatorMock);
        given(vendingMachineMock.setState(isA(ProvideProductState.class))).willReturn(vendingMachineMock);

        VendingMachineState vendingMachineState = new ChangeCheckState(insertedAmount, selectedShelfNumber);
        // when
        vendingMachineState.proceed(vendingMachineMock);
        // then
        verify(vendingMachineMock).removeValueInCoins(eq(change));
        verify(vendingMachineMock).proceed();
    }
}
