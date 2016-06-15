package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.*;
import tdd.vendingMachine.impl.AllowedDenominations;
import tdd.vendingMachine.impl.BasicShelf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VendingMachineTest {

    @Test
    public void should_configure_shelves() {
        AllowedDenominations allowedDenominations = mock(AllowedDenominations.class);
        when(allowedDenominations.add(any(CurrencyUnit.class))).thenReturn(allowedDenominations);
        when(allowedDenominations.isAllowed(any(CurrencyUnit.class))).thenReturn(true);

        VendingMachine vendingMachine = new VendingMachine(mock(CashHandler.class), allowedDenominations);
        vendingMachine.addShelf(new BasicShelf(ProductName.valueOf("Product 1"), ProductPrice.valueOf("1")));
        vendingMachine.addShelf(new BasicShelf(ProductName.valueOf("Product 2"), ProductPrice.valueOf("2")));

        assertThat(vendingMachine.getShelves().size()).isEqualTo(2);
    }

    @Test
    public void should_start_transaction_on_shelf_selection() {
        AllowedDenominations allowedDenominations = mock(AllowedDenominations.class);
        when(allowedDenominations.add(any(CurrencyUnit.class))).thenReturn(allowedDenominations);
        when(allowedDenominations.isAllowed(any(CurrencyUnit.class))).thenReturn(true);

        VendingMachine vendingMachine = new VendingMachine(mock(CashHandler.class), allowedDenominations)
            .addShelf(new BasicShelf(ProductName.valueOf("Product 1"), ProductPrice.valueOf("1")).charge(1));

        Transaction transaction = vendingMachine.selectShelf(0);
        assertThat(transaction).isNotNull();
    }
}
