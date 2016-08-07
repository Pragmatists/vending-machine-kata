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
        new VendingMachine(null, mock(DisplayFactory.class), mock(Function.class), new ChangeCalculator());
    }

    @Test
    public void shouldReturnShelves() throws Exception {
        //given
        List<Shelve> shelves = new ArrayList<>();
        VendingMachine vendingMachine = new VendingMachine(shelves, mock(DisplayFactory.class), mock(Function.class), new ChangeCalculator());
        //when
        Map<Integer, Shelve> returnedShelves = vendingMachine.getShelves();
        //then
        assertTrue(returnedShelves.values().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenDisplayFactoryIsNull() throws Exception {
        new VendingMachine(new ArrayList<>(), null, mock(Function.class), new ChangeCalculator());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenChangeCalculatorIsNull() throws Exception {
        new VendingMachine(new ArrayList<>(), new DefaultDisplayFactory(), mock(Function.class), null);
    }

    @Test
    public void shouldReturnDisplay() throws Exception {
        //given
        DisplayFactory display = new DefaultDisplayFactory();
        VendingMachine vendingMachine = new VendingMachine(new ArrayList<>(), display, mock(Function.class), new ChangeCalculator());
        //when
        Display returnedDisplay = vendingMachine.getDisplay();
        //then
        assertNotNull(returnedDisplay);
    }

    @Test
    public void shouldReturnInsertedInvalidCoin() throws Exception {
        //given
        Cola cola = new Cola();
        DefaultShelve<Cola> shelve = new DefaultShelve<>(Lists.newArrayList(cola), cola.getPrice(), cola.getName());
        VendingMachine vendingMachine = new VendingMachine(Lists.newArrayList(shelve), new DefaultDisplayFactory(), new ShelveKeyMapper(), new ChangeCalculator());
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
        DefaultShelve<Cola> shelve = new DefaultShelve<>(Lists.newArrayList(cola), cola.getPrice(), cola.getName());
        VendingMachine vendingMachine = new VendingMachine(Lists.newArrayList(shelve), new DefaultDisplayFactory(), new ShelveKeyMapper(), new ChangeCalculator());
        //when
        vendingMachine.selectShelve(1);
        vendingMachine.insertCoin(new BigDecimal("1"));
        BigDecimal inputCoin = new BigDecimal("0.3");
        VendingMachineReturnItems returned = vendingMachine.insertCoin(inputCoin);
        //then
        assertNotNull(returned);
        assertNull(returned.getProduct());
        assertEquals(2, returned.getChange().size());
        assertEquals(inputCoin, returned.getChange().get(1));
    }

    @Test
    public void shouldReturnInsertedCoinAfterCancel() throws Exception {
        //given
        Cola cola = new Cola();
        DefaultShelve<Cola> shelve = new DefaultShelve<>(Lists.newArrayList(cola), cola.getPrice(), cola.getName());
        VendingMachine vendingMachine = new VendingMachine(Lists.newArrayList(shelve), new DefaultDisplayFactory(), new ShelveKeyMapper(), new ChangeCalculator());
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
        DefaultShelve<Cola> shelve = new DefaultShelve<>(Lists.newArrayList(cola), cola.getPrice(), cola.getName());
        VendingMachine vendingMachine = new VendingMachine(Lists.newArrayList(shelve), new DefaultDisplayFactory(), new ShelveKeyMapper(), new ChangeCalculator());
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

    @Test
    public void shouldReturnInsertedMoneysAfterInvalidCoinInsertion() throws Exception {
        //given
        Cola cola = new Cola();
        DefaultShelve<Cola> shelve = new DefaultShelve<>(Lists.newArrayList(cola, new Cola(), new Cola()), cola.getPrice(), cola.getName());
        VendingMachine vendingMachine = new VendingMachine(Lists.newArrayList(shelve), new DefaultDisplayFactory(), new ShelveKeyMapper(), new ChangeCalculator());
        //when
        vendingMachine.selectShelve(1);
        BigDecimal inputCoin = new BigDecimal("1");
        vendingMachine.insertCoin(inputCoin);
        vendingMachine.insertCoin(inputCoin);
        vendingMachine.selectShelve(1);
        vendingMachine.insertCoin(inputCoin);
        BigDecimal invalidCoin = new BigDecimal("3");
        VendingMachineReturnItems returned = vendingMachine.insertCoin(invalidCoin);
        //then
        assertNotNull(returned);
        assertNull(returned.getProduct());
        assertEquals(2, returned.getChange().size());
        assertEquals(inputCoin, returned.getChange().get(0));
        assertEquals(invalidCoin, returned.getChange().get(1));
    }

    @Test
    public void shouldReturnInsertedMoneysCauseCanNotGiveChange() throws Exception {
        //given
        Cola cola = new Cola();
        DefaultShelve<Cola> shelve = new DefaultShelve<>(Lists.newArrayList(cola, new Cola(), new Cola()), cola.getPrice(), cola.getName());
        VendingMachine vendingMachine = new VendingMachine(Lists.newArrayList(shelve), new DefaultDisplayFactory(), new ShelveKeyMapper(), new ChangeCalculator());
        //when
        vendingMachine.selectShelve(1);
        BigDecimal one = new BigDecimal("1");
        vendingMachine.insertCoin(one);
        BigDecimal two = new BigDecimal("2");
        VendingMachineReturnItems returned = vendingMachine.insertCoin(two);
        //then
        assertNotNull(returned);
        assertNull(returned.getProduct());
        assertEquals(2, returned.getChange().size());
        assertEquals(one, returned.getChange().get(0));
        assertEquals(two, returned.getChange().get(1));
    }

    @Test
    public void shouldReturnChangeWhenProductWasBought() throws Exception {
        //given
        Cola cola = new Cola();
        DefaultShelve<Cola> shelve = new DefaultShelve<>(Lists.newArrayList(cola, new Cola(), new Cola()), cola.getPrice(), cola.getName());
        VendingMachine vendingMachine = new VendingMachine(Lists.newArrayList(shelve), new DefaultDisplayFactory(), new ShelveKeyMapper(), new ChangeCalculator());
        //when
        vendingMachine.selectShelve(1);
        BigDecimal one = new BigDecimal("1");
        vendingMachine.insertCoin(one);
        vendingMachine.insertCoin(one);
        vendingMachine.selectShelve(1);
        vendingMachine.insertCoin(one);
        BigDecimal two = new BigDecimal("2");
        VendingMachineReturnItems returned = vendingMachine.insertCoin(two);
        //then
        assertNotNull(returned);
        assertNotNull(returned.getProduct());
        assertEquals(1, returned.getChange().size());
        assertEquals(one, returned.getChange().get(0));
        assertEquals(2, vendingMachine.getCoins().get(new BigDecimal("1")).size());
        assertEquals(1, vendingMachine.getCoins().get(new BigDecimal("2")).size());
    }

}
