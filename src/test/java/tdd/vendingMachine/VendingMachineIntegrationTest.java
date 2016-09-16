package tdd.vendingMachine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.product.ProductType;
import tdd.vendingMachine.shelf.CannotChangeShelfProductsTypeException;

import java.io.OutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static tdd.vendingMachine.MachineConfiguration.aMachine;

@RunWith(MockitoJUnitRunner.class)
public class VendingMachineIntegrationTest {

    VendingMachine vendingMachine;

    String printedMessage = "";

    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(mock(OutputStream.class)) {
            @Override
            public void println(String value) {
                printedMessage = printedMessage.concat(value + "\n");
            }
        });
        vendingMachine = new VendingMachine();
    }

    @Test
    public void shouldDisplayChangeWithPrecision() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        int shelfNumber = 5;
        aMachine(vendingMachine)
            .withProduct(new Product(ProductType.CHIPS))
            .onShelf(shelfNumber)
            .withSelectedProduct(shelfNumber)
            .withCurrentRequestCoins(1.0, 0.5, 0.1, 0.1, 0.1, 0.1, 0.1)
            .configure();
        //when
        vendingMachine.insertCoinForCurrentRequest(new Coin(0.5));
        //then
        assertThat(printedMessage).doesNotContain("0.8999999999999999");
        assertThat(printedMessage).contains("1.5");
        assertThat(printedMessage).contains("1.0");
        assertThat(printedMessage).contains("0.9");
        assertThat(printedMessage).contains("0.8");
        assertThat(printedMessage).contains("0.7");
        assertThat(printedMessage).contains("0.6");
        assertThat(printedMessage).contains("0.5");
        assertThat(printedMessage).contains("Machine drop product of type: CHIPS");
    }

}
