package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.dto.Product;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class ShelfTest {

    @Test
    public void emptyShelfTest() {
        Shelf shelf = Shelf.of("test", BigDecimal.ONE, 0);
        assertThat(shelf.isEmpty(), equalTo(true));
        assertThat(shelf.dispense(), nullValue());
        assertThat(shelf.getProductName(), equalTo("test"));
        assertThat(shelf.getProductPrice(), equalTo(BigDecimal.ONE.setScale(2, BigDecimal.ROUND_HALF_EVEN)));
    }

    @Test
    public void filledShelfTest() {
        Shelf shelf = Shelf.of("test", BigDecimal.ONE, 2);
        assertThat(shelf.isEmpty(), equalTo(false));
        assertThat(shelf.dispense(), equalTo(Product.of("test", BigDecimal.ONE)));
        shelf.dispense();
        assertThat(shelf.isEmpty(), equalTo(true));
        assertThat(shelf.dispense(), nullValue());
    }
}
