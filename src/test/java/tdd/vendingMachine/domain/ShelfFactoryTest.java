package tdd.vendingMachine.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.InputMismatchException;

/**
 * Created by Agustin on 2/19/2017.
 */
public class ShelfFactoryTest {

    String type;
    String shelfId;
    int capacity;
    int itemCount;

    @Before
    public void setup() {
        type = "type";
        shelfId = "1";
        capacity = 10;
        itemCount = 5;
    }

    @Test
    public void should_build_empty_shelf_with_capacity() {
        Shelf shelfStub = ShelfFactory.buildShelf(shelfId, type, capacity);

        Assert.assertEquals(type, shelfStub.type);
        Assert.assertEquals(capacity, shelfStub.capacity);
        Assert.assertEquals(0, shelfStub.getItemCount());
        Assert.assertTrue(shelfStub.isEmpty());
    }

    @Test
    public void should_build_shelf_with_capacity_and_initial_itemCount() {
        Shelf shelfStub = ShelfFactory.buildShelf(shelfId, type, capacity, itemCount);

        Assert.assertEquals(type, shelfStub.type);
        Assert.assertEquals(capacity, shelfStub.capacity);
        Assert.assertEquals(itemCount, shelfStub.getItemCount());
        Assert.assertFalse(shelfStub.isEmpty());
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_building_shelf_id_empty(){
        ShelfFactory.buildShelf("", "", 10);
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_building_shelf_id_null(){
        ShelfFactory.buildShelf(null, "", 10);
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_building_shelf_type_empty(){
        String shelfId = "1";
        ShelfFactory.buildShelf(shelfId, "", 10);
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_building_shelf_type_null(){
        String shelfId = "1";
        ShelfFactory.buildShelf(shelfId, null, 10);
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_itemCount_less_zero() {
        ShelfFactory.buildShelf(shelfId, type, capacity, -1);
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_capacity_less_zero() {
        ShelfFactory.buildShelf(shelfId, type, -1, itemCount);
    }
}
