package tdd.vendingMachine;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.dto.Product;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

public class ProductDispenserTest {

    private ProductDispenser productDispenser;

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(new VendingMachineInjector());
        this.productDispenser = injector.getInstance(ProductDispenser.class);
    }

    @Test
    public void emptyDispenserTest() {
        assertThat(this.productDispenser.retrieveProducts(), is(empty()));
    }

    @Test
    public void singleProductDispenserTest() {
        Product product = Product.of("test", BigDecimal.ONE);
        this.productDispenser.ejectProduct(product);
        assertThat(this.productDispenser.retrieveProducts(), contains(product));
        assertThat(this.productDispenser.retrieveProducts(), is(empty()));
    }

    @Test
    public void multipleProductDispenserTest() {
        Product product1 = Product.of("test", BigDecimal.ONE);
        Product product2 = Product.of("test2", BigDecimal.TEN);
        this.productDispenser.ejectProduct(product1);
        this.productDispenser.ejectProduct(product2);
        assertThat(this.productDispenser.retrieveProducts(), contains(product1, product2));
        assertThat(this.productDispenser.retrieveProducts(), is(empty()));
    }
}
