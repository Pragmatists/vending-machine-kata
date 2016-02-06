package tdd.vendingMachine;

import com.google.common.base.Function;
import org.junit.Test;
import tdd.vendingMachine.display.DefaultDisplayFactory;
import tdd.vendingMachine.display.Display;
import tdd.vendingMachine.display.DisplayFactory;
import tdd.vendingMachine.product.Cola;
import tdd.vendingMachine.shelve.DefaultShelve;
import tdd.vendingMachine.shelve.Shelve;
import tdd.vendingMachine.shelve.ShelveKeyMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class VendingMachineTest {

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenShelvesAreNull() throws Exception{
        new VendingMachine(null, mock(DisplayFactory.class), mock(Function.class));
    }

    @Test
    public void shouldReturnShelves() throws Exception {
        //given
        List<Shelve> shelves = new ArrayList<>();
        VendingMachine vendingMachine = new VendingMachine(shelves, mock(DisplayFactory.class), mock(Function.class));
        //when
        List<Shelve> returnedShelves = vendingMachine.getShelves();
        //then
        assertEquals(shelves, returnedShelves);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenDisplayFactoryIsNull() throws Exception {
        new VendingMachine(new ArrayList<>(), null, mock(Function.class));
    }

    @Test
    public void shouldReturnDisplay() throws Exception {
        //given
        DisplayFactory display = new DefaultDisplayFactory();
        VendingMachine vendingMachine = new VendingMachine(new ArrayList<>(), display, mock(Function.class));
        //when
        Display returnedDisplay = vendingMachine.getDisplay();
        //then
        assertNotNull(returnedDisplay);
    }

    @Test
    public void shouldReturnPriceForSelectedShelve() throws Exception {
        //given
        List<Shelve> shelves = new ArrayList<>();
        List<Cola> products = new ArrayList<>();
        Cola cola = new Cola();
        products.add(cola);
        DefaultShelve<Cola> shelve = new DefaultShelve<>(products, cola.getPrice());
        shelves.add(shelve);
        VendingMachine vendingMachine = new VendingMachine(shelves, new DefaultDisplayFactory(), new ShelveKeyMapper());
        //when
        BigDecimal selectedProductPrice = vendingMachine.selectShelve(1);
        //then
        assertNotNull(selectedProductPrice);
        assertEquals(cola.getPrice(), selectedProductPrice);
    }

}
