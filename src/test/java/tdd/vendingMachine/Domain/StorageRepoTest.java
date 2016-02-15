package tdd.vendingMachine.Domain;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Consistency checks for the operation of StorageRepo
 */
public class StorageRepoTest {

    StorageRepo repo;

    @Before
    public void setUp() {
        repo = new StorageRepo(5, 10);  //5shelves, 10items max
    }


    @Test
    public void simpleSetGetShelfContents() {
        repo.setProductAtShelf(0, 1, 5);
        assertThat(repo.getCountAtShelf(0) == 5);
        assertThat(repo.getPidAtShelf(0) == 1);
    }

    @Test
    public void emptyShelves() {
    }


    @Test
    public void setAndAddThrowingExceptions() {

    }

    @Test
    public void serveProductLogic() {

    }


}
