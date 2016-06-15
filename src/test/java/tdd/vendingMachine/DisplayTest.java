package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.*;
import tdd.vendingMachine.impl.BasicDisplay;
import tdd.vendingMachine.impl.BasicProduct;

import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DisplayTest {

    @Test(expected = IllegalArgumentException.class)
    public void throws_exception_if_created_without_observer() {
        new BasicDisplay(null, new Display.Input() {
            @Override
            public String readString() {
                return null;
            }

            @Override
            public int readInt() {
                return 0;
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void throws_exception_if_created_without_input() {
        new BasicDisplay(index -> null, null);
    }

    @Test
    public void test_display_actions_flow() {
        BasicProduct product = new BasicProduct(ProductName.valueOf("Product 1"), ProductPrice.valueOf("1"));

        PurchaseResult purchaseResult = mock(PurchaseResult.class);
        when(purchaseResult.getProduct()).thenReturn(product);

        Transaction transaction = mock(Transaction.class);
        when(transaction.getProductPrice()).thenReturn(product.getPrice().toCurrency());
        when(transaction.insertCoin(any(CurrencyUnit.class))).thenReturn(transaction);
        when(transaction.getShortFall()).thenReturn(CurrencyUnit.valueOf("0"));
        when(transaction.commit()).thenReturn(purchaseResult);

        Shelf shelf = mock(Shelf.class);
        when(shelf.getProductName()).thenReturn("Product 1");
        when(shelf.getProductPrice()).thenReturn(product.getPrice().toCurrency());

        Display.Input input = mock(Display.Input.class);
        when(input.readString()).thenReturn("1").thenReturn("0");
        when(input.readInt()).thenReturn(1).thenReturn(-1);

        Display display = new BasicDisplay(index -> transaction, input);
        display.run(Collections.singletonList(shelf));
    }
}
