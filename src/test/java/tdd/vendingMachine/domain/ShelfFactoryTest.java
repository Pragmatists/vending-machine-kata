package tdd.vendingMachine.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.InputMismatchException;

/**
 * Created by Agustin on 2/19/2017.
 */
public class ShelfFactoryTest {

    private String type;
    private String shelfId;
    private int capacity;
    private int itemCount;
    private Product product;
    private Product emptyProduct;

    @Before
    public void setup() {
        type = "type";
        product = new Product(10, type);
        emptyProduct = new Product(0.5, "");
        shelfId = "1";
        capacity = 10;
        itemCount = 5;
    }

    @After
    public void tearDown() {
        product = null;
        emptyProduct = null;
    }

    @Test
    public void should_build_empty_shelf_with_capacity() {
        Shelf shelfStub = ShelfFactory.buildShelf(shelfId, product, capacity);

        Assert.assertEquals(type, shelfStub.product.getType());
        Assert.assertEquals(capacity, shelfStub.capacity);
        Assert.assertEquals(0, shelfStub.getItemCount());
        Assert.assertTrue(shelfStub.isEmpty());
    }

    @Test
    public void should_build_shelf_with_capacity_and_initial_itemCount() {
        Shelf shelfStub = ShelfFactory.buildShelf(shelfId, product, capacity, itemCount);

        Assert.assertEquals(type, shelfStub.product.getType());
        Assert.assertEquals(capacity, shelfStub.capacity);
        Assert.assertEquals(itemCount, shelfStub.getItemCount());
        Assert.assertFalse(shelfStub.isEmpty());
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_building_shelf_id_empty(){
        ShelfFactory.buildShelf("", emptyProduct, 10);
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_building_shelf_id_null(){
        ShelfFactory.buildShelf(null, emptyProduct, 10);
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_building_shelf_type_empty(){
        String shelfId = "1";
        ShelfFactory.buildShelf(shelfId, emptyProduct, 10);
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_building_shelf_product_null(){
        String shelfId = "1";
        ShelfFactory.buildShelf(shelfId, null, 10);
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_itemCount_less_zero() {
        ShelfFactory.buildShelf(shelfId, product, capacity, -1);
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_capacity_less_zero() {
        ShelfFactory.buildShelf(shelfId, product, -1, itemCount);
    }
}
