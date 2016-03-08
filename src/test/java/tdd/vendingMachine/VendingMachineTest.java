package tdd.vendingMachine;

import info.solidsoft.mockito.java8.api.WithBDDMockito;
import java.util.Optional;
import org.assertj.core.api.WithAssertions;
import org.junit.Before;
import org.junit.Test;

public class VendingMachineTest implements WithBDDMockito, WithAssertions {

    private Keyboard keyboardMock;
    private VendingMachine vendingMachine;

    @Before
    public void setUp() throws Exception {
        keyboardMock = mock(Keyboard.class);
        vendingMachine = new VendingMachine(20, keyboardMock);
    }

    @Test
    public void should_return_selected_shelf_number_for_correct_shelf_number() {
        // given
        given(keyboardMock.readNumber()).willReturn(20);
        // when
        Optional<Integer> readedShelf = vendingMachine.readSelectedShelfNumber();
        // then
        assertThat(readedShelf).contains(20);
    }

    @Test
    public void should_return_empty_optional_when_select_shelf_number_out_of_range() {
        // given
        given(keyboardMock.readNumber()).willReturn(23);
        // when
        Optional<Integer> readShelf = vendingMachine.readSelectedShelfNumber();
        // then
        assertThat(readShelf).isEmpty();
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
