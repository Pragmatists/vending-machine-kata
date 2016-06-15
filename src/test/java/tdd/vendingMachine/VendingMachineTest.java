package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.CashHandler;
import tdd.vendingMachine.core.Shelf;
import tdd.vendingMachine.impl.ShelveContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VendingMachineTest {

    @Test
    public void should_start_transaction_on_shelf_selection() {
        Shelf shelf = mock(Shelf.class);
        when(shelf.hasProducts()).thenReturn(true);

        ShelveContainer shelveContainer = mock(ShelveContainer.class);
        when(shelveContainer.get(0)).thenReturn(shelf);

        assertThat(new VendingMachine(mock(CashHandler.class), shelveContainer, null)
            .selectShelf(0)
        ).isNotNull();
    }
}
