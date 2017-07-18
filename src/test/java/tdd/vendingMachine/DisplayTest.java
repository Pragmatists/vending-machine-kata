package tdd.vendingMachine;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class DisplayTest {

    private Display display;

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(new VendingMachineInjector());
        this.display = injector.getInstance(Display.class);
    }

    @Test
    public void defaultDisplayTest() {
        assertThat(this.display.view(), is(equalTo("Welcome! Please enter shelf number.")));
    }

    @Test
    public void writeProductPriceTest() {
        this.display.writeProductPrice(BigDecimal.ONE);
        assertThat(this.display.view(), is(equalTo("Product remaining price: 1.00\n")));
        this.display.reset();
        assertThat(this.display.view(), is(equalTo("Welcome! Please enter shelf number.")));
    }

    @Test
    public void writeWarningTest() {
        this.display.writeWarning("Test warning");
        assertThat(this.display.view(), is(equalTo("Warning: Test warning\n")));
        this.display.reset();
        assertThat(this.display.view(), is(equalTo("Welcome! Please enter shelf number.")));
    }

    @Test
    public void writeProductPriceAndWarningTest() {
        this.display.writeProductPrice(BigDecimal.ONE);
        this.display.writeWarning("Test warning");
        assertThat(this.display.view(), is(equalTo("Product remaining price: 1.00\nWarning: Test warning\n")));
        this.display.reset();
        assertThat(this.display.view(), is(equalTo("Welcome! Please enter shelf number.")));
    }
}
