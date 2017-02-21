package tdd.vendingMachine.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.InputMismatchException;

/**
 * @author Agustin Cabra on 2/20/2017.
 * @since 1.0
 */
public class ShelfTest {

    private Product productMock;
    private Shelf<Product> nonEmptyShelf;
    private Shelf<Product> emptyShelf;
    private Shelf<Product> fullShelf;

    @Before
    public void setup() {
        productMock = Mockito.mock(Product.class);
        Mockito.doReturn("type").when(productMock).provideType();
        Mockito.doReturn("type").when(productMock).getType();

        emptyShelf = ShelfProductFactory.buildShelf(0, productMock, 20, 0);
        nonEmptyShelf = ShelfProductFactory.buildShelf(0, productMock, 20, 5);
        fullShelf = ShelfProductFactory.buildShelf(0, productMock, 20, 20);

        Mockito.verify(productMock, Mockito.times(3)).getType();
    }

    @After
    public void tearDown() {
        nonEmptyShelf = null;
        productMock = null;
        fullShelf = null;
    }

    @Test
    public void should_insert_one_items_to_not_empty_shelf() {
        int itemsToProvision = 1;
        Assert.assertFalse(nonEmptyShelf.isEmpty());
        int itemsBeforeProvision = nonEmptyShelf.getItemCount();
        Assert.assertEquals(itemsToProvision + itemsBeforeProvision, nonEmptyShelf.provision());
        Assert.assertEquals(itemsToProvision + itemsBeforeProvision, nonEmptyShelf.getItemCount());
    }

    @Test
    public void should_insert_n_items_to_not_empty_shelf() {
        int itemsToProvision = 4;
        Assert.assertFalse(nonEmptyShelf.isEmpty());
        int itemsBeforeProvision = nonEmptyShelf.getItemCount();
        Assert.assertEquals(itemsToProvision + itemsBeforeProvision, nonEmptyShelf.provision(itemsToProvision));
        Assert.assertEquals(itemsToProvision + itemsBeforeProvision, nonEmptyShelf.getItemCount());
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_to_provision_many_items_to_full_shelf() {
        Shelf<Product> shelf = this.fullShelf;
        Assert.assertTrue(shelf.capacity == shelf.getItemCount());
        shelf.provision(2);
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_to_provision_one_item_to_full_shelf() {
        Shelf<Product> shelf = this.fullShelf;
        Assert.assertTrue(shelf.capacity == shelf.getItemCount());
        shelf.provision();
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_to_provision_negative_items_to_full_shelf() {
        Shelf<Product> shelf = this.fullShelf;
        Assert.assertTrue(shelf.capacity == shelf.getItemCount());
        shelf.provision(-2);
    }


    @Test(expected = InputMismatchException.class)
    public void should_fail_to_provision_negative_items_to_non_empty_shelf() {
        Shelf<Product> shelf = this.nonEmptyShelf;
        Assert.assertTrue(shelf.capacity >= shelf.getItemCount());
        shelf.provision(-2);
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_to_provision_negative_items_to_empty_shelf() {
        Shelf<Product> shelf = this.emptyShelf;
        Assert.assertTrue(shelf.isEmpty());
        shelf.provision(-2);
    }

    @Test
    public void should_dispense_n_items_from_not_empty_shelf() {
        int itemsToDispense = 5;
        Shelf<Product> shelf = this.nonEmptyShelf;
        Assert.assertFalse(shelf.isEmpty());
        int itemsBeforeDispensing = shelf.getItemCount();
        Assert.assertEquals(itemsBeforeDispensing - itemsToDispense, shelf.dispense(itemsToDispense));
        Assert.assertEquals(itemsBeforeDispensing - itemsToDispense, shelf.getItemCount());
    }

    @Test
    public void should_dispense_one_items_to_not_empty_shelf() {
        int itemsToDispense = 1;
        Shelf<Product> shelf = this.nonEmptyShelf;
        Assert.assertFalse(shelf.isEmpty());
        int itemsBeforeDispensing = shelf.getItemCount();
        Assert.assertEquals(itemsBeforeDispensing - itemsToDispense, shelf.dispense());
        Assert.assertEquals(itemsBeforeDispensing - itemsToDispense, shelf.getItemCount());
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_to_dispense_one_item_from_empty_shelf() {
        Shelf<Product> shelf = this.emptyShelf;
        Assert.assertTrue(shelf.isEmpty());
        shelf.dispense();
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_to_dispense_many_item_from_empty_shelf() {
        Shelf<Product> shelf = this.emptyShelf;
        Assert.assertTrue(shelf.isEmpty());
        shelf.dispense(2);
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_to_dispense_negative_item_from_empty_shelf() {
        Shelf<Product> shelf = this.emptyShelf;
        Assert.assertTrue(shelf.isEmpty());
        shelf.dispense(-2);
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_to_dispense_negative_item_from_full_shelf() {
        Shelf<Product> shelf = this.fullShelf;
        Assert.assertTrue(shelf.getItemCount() == shelf.capacity);
        shelf.dispense(-2);
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_to_dispense_negative_item_from_non_empty_shelf() {
        Shelf<Product> shelf = this.fullShelf;
        Assert.assertTrue(shelf.capacity == shelf.getItemCount());
        shelf.dispense(-2);
    }

    @Test
    public void should_provide_type_from_any_shelf() {
        String type = "type";
        Assert.assertEquals(type, emptyShelf.getType().provideType());
        Assert.assertEquals(type, fullShelf.getType().provideType());
        Assert.assertEquals(type, nonEmptyShelf.getType().provideType());

        Mockito.verify(productMock, Mockito.times(3)).getType();
    }
}
