package tdd.vendingMachine.Domain;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Consistency checks for the operation of StorageRepo
 */
public class StorageRepoTest {

    StorageRepo repo;

    int SPRITE = 1; //example protuct ID

    @Before
    public void setUp() {
        //Empty storage with 5 shelves and 10 items max; for tests
        repo = new StorageRepo(5, 10);
    }

    @Test
    public void simpleSetGetShelfContents() throws Exception{
        repo.setProductAtShelf(0, SPRITE, 5);
        assertThat(repo.getnShelves()).isEqualTo(5);
        assertThat(repo.getMaxItemsOnShelf()).isEqualTo(10);
        assertThat(repo.getCountAtShelf(0)).isEqualTo(5);
        assertThat(repo.getPidAtShelf(0)).isEqualTo(SPRITE);
    }

    @Test
    public void emptyShelves() {
        assertThat(repo.getCountAtShelf(0)).isEqualTo(0);
        assertThat(repo.getPidAtShelf(0)).isEqualTo(0);
    }


    @Test
    public void setAndAddThrowingExceptions() {
        try {
            repo.setProductAtShelf(111,SPRITE,5);
            fail("Should have thrown " + Error.INVALID_SHELF_NUMBER);
        } catch (RuntimeException e) {
            assertThat(e.toString()).endsWith(
                Error.INVALID_SHELF_NUMBER.toString()
            );
        }

        try {
            repo.setProductAtShelf(1,SPRITE,500);
            fail("Should have thrown " + Error.INVALID_NUMBER_OF_ITEMS_AT_SHELF);
        } catch (RuntimeException e) {
            assertThat(e.toString()).endsWith(
                Error.INVALID_NUMBER_OF_ITEMS_AT_SHELF.toString()
            );
        }

        try {
            repo.setProductAtShelf(1,SPRITE,-1);
            fail("Should have thrown " + Error.INVALID_NUMBER_OF_ITEMS_AT_SHELF);
        } catch (RuntimeException e) {
            assertThat(e.toString()).endsWith(
                Error.INVALID_NUMBER_OF_ITEMS_AT_SHELF.toString()
            );
        }
    }

    @Test
    public void addProductToShelf() {
        repo.setProductAtShelf(0, SPRITE, 5);
        repo.addProductsToShelf(0, 3);
        assertThat(repo.getCountAtShelf(0)).isEqualTo(8);
        repo.addProductsToShelf(0, -5);
        assertThat(repo.getCountAtShelf(0)).isEqualTo(3);

        try {
            repo.addProductsToShelf(1, 5);
            fail("Should have thrown " + Error.CANNOT_ADD_TO_EMPTY_SHELF);
        } catch (RuntimeException e) {
            assertThat(e.toString()).endsWith(
                Error.CANNOT_ADD_TO_EMPTY_SHELF.toString()
            );
        }

        try {
            repo.addProductsToShelf(0, 100);
            fail("Should have thrown " + Error.INVALID_NUMBER_OF_ITEMS_AT_SHELF);
        } catch (RuntimeException e) {
            assertThat(e.toString()).endsWith(Error.INVALID_NUMBER_OF_ITEMS_AT_SHELF.toString());
        }

        try {
            repo.addProductsToShelf(0, -100);
            fail("Should have thrown " + Error.INVALID_NUMBER_OF_ITEMS_AT_SHELF);
        } catch (RuntimeException e) {
            assertThat(e.toString()).endsWith(Error.INVALID_NUMBER_OF_ITEMS_AT_SHELF.toString());
        }

        try {
            repo.addProductsToShelf(22, 3);
            fail("Should have thrown " + Error.INVALID_SHELF_NUMBER);
        } catch (RuntimeException e) {
            assertThat(e.toString()).endsWith(Error.INVALID_SHELF_NUMBER.toString());
        }
    }

    @Test
    public void serveProductLogic() {

    }


}
