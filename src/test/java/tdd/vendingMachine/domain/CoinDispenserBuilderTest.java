package tdd.vendingMachine.domain;

import org.junit.Assert;
import org.junit.Test;
import tdd.vendingMachine.dto.CashImport;
import tdd.vendingMachine.util.TestUtils.TestUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
public class CoinDispenserBuilderTest {

    @Test
    public void should_build_empty_cashDispenser_with_shelf_capacity_1() {
        final int expectedCapacity = 1;
        int expectedSize = Coin.values().length;
        Map<Coin, Shelf<Coin>> cashDispenser = new CoinDispenserBuilder(expectedCapacity).buildShelf();
        Assert.assertEquals(expectedSize, cashDispenser.size());
        cashDispenser.values().forEach(coinShelf -> {
            Assert.assertEquals(expectedCapacity, coinShelf.capacity);
            Assert.assertEquals(0, coinShelf.getItemCount());
        });

    }

    @Test
    public void should_build_full_cashDispenser_with_shelf_capacity_5() {
        final int expectedCapacity = 5;
        final int expectedSize = Coin.values().length;
        final int expectedAmount = 5;
        Collection<CashImport> stubCashImportsFull = TestUtils.getStubCashImportsFull(expectedAmount);
        Map<Coin, Shelf<Coin>> cashDispenser = new CoinDispenserBuilder(expectedCapacity)
            .withCashImport(stubCashImportsFull)
            .buildShelf();
        Assert.assertEquals(expectedSize, cashDispenser.size());
        cashDispenser.values().forEach(coinShelf -> {
            Assert.assertEquals(expectedCapacity, coinShelf.capacity);
            Assert.assertEquals(expectedAmount, coinShelf.getItemCount());
            Assert.assertTrue(coinShelf.countFreeSlots() == 0);
        });
    }

    @Test
    public void should_discard_items_since_shelf_got_full() {
        final int expectedSize = Coin.values().length;
        final int expectedCapacity = 4;
        final int givenAmount = 7;
        final int expectedDiscardedItems = givenAmount - expectedCapacity;
        Coin fiftyCents = Coin.FIFTY_CENTS;
        CashImport stubCashImport = new CashImport(fiftyCents.label, givenAmount);
        Map<Coin, Shelf<Coin>> cashDispenser = new CoinDispenserBuilder(expectedCapacity)
            .withCashImport(stubCashImport)
            .buildShelf();
        Assert.assertEquals(expectedSize, cashDispenser.size());
        Assert.assertEquals(expectedCapacity, cashDispenser.get(fiftyCents).getItemCount());
        Assert.assertEquals(expectedDiscardedItems, stubCashImport.getAmount() - cashDispenser.get(fiftyCents).getItemCount());
    }

    @Test
    public void should_build_non_full_shelf_since_import_amount_less_than_capacity() {
        final int expectedSize = Coin.values().length;
        final int expectedCapacity = 4;
        final int givenAmount = 3;
        final int expectedDiscardedItems = 0;
        Coin fiftyCents = Coin.FIFTY_CENTS;
        CashImport stubCashImport = new CashImport(fiftyCents.label, givenAmount);
        Map<Coin, Shelf<Coin>> cashDispenser = new CoinDispenserBuilder(expectedCapacity)
            .withCashImport(stubCashImport)
            .buildShelf();
        Assert.assertEquals(expectedSize, cashDispenser.size());
        Assert.assertEquals(stubCashImport.getAmount(), cashDispenser.get(fiftyCents).getItemCount());
        Assert.assertEquals(expectedDiscardedItems, stubCashImport.getAmount() - cashDispenser.get(fiftyCents).getItemCount());
        Assert.assertEquals(expectedCapacity - stubCashImport.getAmount(), cashDispenser.get(fiftyCents).countFreeSlots());
    }
}
