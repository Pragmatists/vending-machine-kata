package tdd.vendingMachine;

import info.solidsoft.mockito.java8.api.WithBDDMockito;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.assertj.core.api.WithAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tdd.vendingMachine.io.Display;
import tdd.vendingMachine.io.Keyboard;
import tdd.vendingMachine.state.VendingMachineState;

import java.util.Optional;

@RunWith(JUnitParamsRunner.class)
public class VendingMachineTest implements WithBDDMockito, WithAssertions {

    private Keyboard keyboardMock;
    private VendingMachine vendingMachine;

    @Before
    public void setUp() throws Exception {
        keyboardMock = mock(Keyboard.class);
        vendingMachine = new VendingMachine(20, keyboardMock, mock(Display.class));
    }

    @Test
    public void should_throw_when_trying_to_put_product_on_shelf_that_does_not_exist() {
        // expect
        assertThatThrownBy(() -> vendingMachine.putProductsOnShelf(22, Product.DIET_COKE, 2))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(String.valueOf(22));
    }

    @Test
    public void should_return_selected_shelf_number() {
        // given
        vendingMachine.putProductsOnShelf(1, Product.DIET_COKE, 2);
        given(keyboardMock.readNumber()).willReturn(1);
        // when
        int shelfNumber = vendingMachine.selectProductShelf();
        // then
        assertThat(shelfNumber).isEqualTo(1);
    }

    @Test
    @Parameters({"-1, input not a number", "1, product stack is empty", "10, shelf is empty"})
    @TestCaseName("when {1}")
    public void should_ask_for_shelf_number_again(int input, String desc) {
        // given
        vendingMachine
            .putProductsOnShelf(1, Product.DIET_COKE, 0)
            .putProductsOnShelf(2, Product.KITKAT, 2);
        given(keyboardMock.readNumber()).willReturn(input, 2);
        // when
        int shelfNumber = vendingMachine.selectProductShelf();
        // then
        assertThat(shelfNumber).isEqualTo(2);
    }

    @Test
    public void should_return_empty_optional_when_product_does_not_exist() throws Exception {
        // given
        vendingMachine.putProductsOnShelf(1, Product.DIET_COKE, 3);
        // when
        Optional<Product> product = vendingMachine.getProductInfo(3);
        // then
        assertThat(product).isEmpty();
    }

    @Test
    public void should_return_product_when_getting_info() throws Exception {
        // given
        vendingMachine.putProductsOnShelf(1, Product.DIET_COKE, 3);
        // when
        Optional<Product> product = vendingMachine.getProductInfo(1);
        // then
        assertThat(product).contains(Product.DIET_COKE);
    }

    @Test
    public void should_call_proceed_on_state_when_starting_machine() throws Exception {
        // given
        VendingMachineState stateMock = mock(VendingMachineState.class);
        // when
        vendingMachine.start(stateMock);
        // then
        verify(stateMock).proceed(eq(vendingMachine));
    }
}
