package tdd.vendingMachine;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
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
import java.util.Map;

import static org.junit.Assert.*;
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
        Map<Integer, Shelve> returnedShelves = vendingMachine.getShelves();
        //then
        assertTrue(returnedShelves.values().isEmpty());
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
        Cola cola = new Cola();
        DefaultShelve<Cola> shelve = new DefaultShelve<>(Lists.newArrayList(cola), cola.getPrice());
        VendingMachine vendingMachine = new VendingMachine(Lists.newArrayList(shelve), new DefaultDisplayFactory(), new ShelveKeyMapper());
        //when
        BigDecimal selectedProductPrice = vendingMachine.selectShelve(1);
        //then
        assertNotNull(selectedProductPrice);
        assertEquals(cola.getPrice(), selectedProductPrice);
    }

    @Test
    public void shouldReturnInsertedInvalidCoin() throws Exception {
        //given
        Cola cola = new Cola();
        DefaultShelve<Cola> shelve = new DefaultShelve<>(Lists.newArrayList(cola), cola.getPrice());
        VendingMachine vendingMachine = new VendingMachine(Lists.newArrayList(shelve), new DefaultDisplayFactory(), new ShelveKeyMapper());
        //when
        vendingMachine.selectShelve(1);
        BigDecimal inputCoin = new BigDecimal("0.3");
        VendingMachineReturnItems returned = vendingMachine.insertCoin(inputCoin);
        //then
        assertNotNull(returned);
        assertNull(returned.getProduct());
        assertEquals(1, returned.getChange().size());
        assertEquals(inputCoin, returned.getChange().get(0));
    }

    @Test
    public void shouldReturnInsertedInvalidSecondCoin() throws Exception {
        //given
        Cola cola = new Cola();
        DefaultShelve<Cola> shelve = new DefaultShelve<>(Lists.newArrayList(cola), cola.getPrice());
        VendingMachine vendingMachine = new VendingMachine(Lists.newArrayList(shelve), new DefaultDisplayFactory(), new ShelveKeyMapper());
        //when
        vendingMachine.selectShelve(1);
        vendingMachine.insertCoin(new BigDecimal("1"));
        BigDecimal inputCoin = new BigDecimal("0.3");
        VendingMachineReturnItems returned = vendingMachine.insertCoin(inputCoin);
        //then
        assertNotNull(returned);
        assertNull(returned.getProduct());
        assertEquals(1, returned.getChange().size());
        assertEquals(inputCoin, returned.getChange().get(0));
    }

    @Test
    public void shouldReturnInsertedCoinAfterCancel() throws Exception {
        //given
        Cola cola = new Cola();
        DefaultShelve<Cola> shelve = new DefaultShelve<>(Lists.newArrayList(cola), cola.getPrice());
        VendingMachine vendingMachine = new VendingMachine(Lists.newArrayList(shelve), new DefaultDisplayFactory(), new ShelveKeyMapper());
        //when
        vendingMachine.selectShelve(1);
        BigDecimal inputCoin = new BigDecimal("0.2");
        vendingMachine.insertCoin(inputCoin);
        VendingMachineReturnItems returned = vendingMachine.cancel();
        //then
        assertNotNull(returned);
        assertNull(returned.getProduct());
        assertEquals(1, returned.getChange().size());
        assertEquals(inputCoin, returned.getChange().get(0));
    }

    @Test
    public void shouldReturnInsertedCoinCauseNoPossibilityToChange() throws Exception {
        //given
        Cola cola = new Cola();
        DefaultShelve<Cola> shelve = new DefaultShelve<>(Lists.newArrayList(cola), cola.getPrice());
        VendingMachine vendingMachine = new VendingMachine(Lists.newArrayList(shelve), new DefaultDisplayFactory(), new ShelveKeyMapper());
        //when
        vendingMachine.selectShelve(1);
        BigDecimal inputCoin = new BigDecimal("5");
        VendingMachineReturnItems returned = vendingMachine.insertCoin(inputCoin);
        //then
        assertNotNull(returned);
        assertNull(returned.getProduct());
        assertEquals(1, returned.getChange().size());
        assertEquals(inputCoin, returned.getChange().get(0));
    }

}
