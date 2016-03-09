package tdd.vendingMachine;

import info.solidsoft.mockito.java8.api.WithBDDMockito;
import java.util.Optional;
import org.assertj.core.api.WithAssertions;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.io.Display;
import tdd.vendingMachine.io.Keyboard;
import tdd.vendingMachine.state.VendingMachineState;

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
    public void should_return_selected_product() {
        // given
        vendingMachine.putProductsOnShelf(1, Product.DIET_COKE, 2);
        given(keyboardMock.readNumber()).willReturn(1);
        // when
        Optional<Product> selectedProduct = vendingMachine.getSelectedProduct();
        // then
        assertThat(selectedProduct).contains(Product.DIET_COKE);
    }

    @Test
    public void should_return_empty_optional_when_selected_shelf_number_is_out_of_range() {
        // given
        given(keyboardMock.readNumber()).willReturn(23);
        // when
        Optional<Product> selectedProduct = vendingMachine.getSelectedProduct();
        // then
        assertThat(selectedProduct).isEmpty();
    }

    @Test
    public void should_return_empty_optional_when_product_not_available() {
        // given
        given(keyboardMock.readNumber()).willReturn(12);
        // when
        Optional<Product> selectedProduct = vendingMachine.getSelectedProduct();
        // then
        assertThat(selectedProduct).isEmpty();
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
