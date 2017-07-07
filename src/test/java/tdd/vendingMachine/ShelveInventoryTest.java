package tdd.vendingMachine;

import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.exception.ShelveProductsShouldBeSimilarException;
import tdd.vendingMachine.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Yevhen Sukhomud
 */
public class ShelveInventoryTest {

    private ShelveInventory inventory;
    private List<Product> products = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        inventory = new ShelveInventory(new HashMap<>()); // set empty inventory
        products.clear();
    }

    @Test
    public void getEmptyInventory_shouldReturnNull() throws Exception {
        // given
        inventory = new ShelveInventory(new HashMap<>());
        // when
        Product product = inventory.get(1);
        // then
        assertNull(product);
    }

    @Test
    public void getExistingProduct_shouldReturnProduct() throws Exception {
        // given
        HashMap<Integer, List<Product>> shelves = new HashMap<>();
        Product insertedProduct = new Product("chupa-chups", 2);
        products.add(insertedProduct);
        shelves.put(1, products);
        inventory = new ShelveInventory(shelves);
        // when
        Product gotProduct = inventory.get(1);
        // then
        assertEquals(insertedProduct, gotProduct);
    }

    @Test
    public void getNotExistingProduct_shouldReturnNull() throws Exception {
        // given
        HashMap<Integer, List<Product>> shelves = new HashMap<>();
        products.add(new Product("chupa-chups", 2));
        shelves.put(1, products);
        inventory = new ShelveInventory(shelves);
        // when
        Product product = inventory.get(2);
        // then
        assertNull(product);
    }


    @Test
    public void putProduct_shouldBeInInventory() throws Exception {
        // given
        int index = 1;
        // when
        Product insertedProduct = new Product("chupa-chups", 2);
        inventory.put(index, insertedProduct);
        // then
        Product gotProduct = inventory.get(index);
        assertEquals(insertedProduct, gotProduct);
    }

    @Test
    public void putTheSameProducts_shouldPutOnTheSameShelve() throws Exception {
        // given
        int index = 1;
        // when
        Product insertedProduct1 = new Product("chupa-chups", 2);
        Product insertedProduct2 = new Product("chupa-chups", 2);

        inventory.put(index, insertedProduct1);
        inventory.put(index, insertedProduct2);
        // then
        Product gotProduct1 = inventory.get(index);
        Product gotProduct2 = inventory.get(index);
        Product gotProduct3 = inventory.get(index);
        assertNotNull(gotProduct1);
        assertNotNull(gotProduct2);
        assertNull(gotProduct3);
    }

    @Test(expected = ShelveProductsShouldBeSimilarException.class)
    public void putDifferentProductsOnTheSameShelve_shouldThrowShelveProductsShouldBeSimilarException() throws Exception {
        // given
        int index = 1;
        // when
        Product insertedProduct1 = new Product("chupa-chups", 2);
        Product insertedProduct2 = new Product("cola", 5);

        inventory.put(index, insertedProduct1);
        inventory.put(index, insertedProduct2);
        // then
        // throw ShelveProductsShouldBeSimilarException
    }

    @Test
    public void putDifferentProductsOnDifferentShelve_shouldPutOnDifferentShelve() throws Exception {
        // given
        int index1 = 1;
        int index2 = 2;
        // when
        Product insertedProduct1 = new Product("chupa-chups", 2);
        Product insertedProduct2 = new Product("cola", 5);

        inventory.put(index1, insertedProduct1);
        inventory.put(index2, insertedProduct2);
        // then
        Product gotProduct1 = inventory.get(index1);
        Product gotProduct2 = inventory.get(index2);
        assertEquals(insertedProduct1, gotProduct1);
        assertEquals(insertedProduct2, gotProduct2);
    }

    @Test
    public void putTheSameProductsOnDifferentShelve_shouldPutOnDifferentShelve() throws Exception {
        // given
        int index1 = 1;
        int index2 = 2;
        // when
        Product insertedProduct1 = new Product("chupa-chups", 2);
        Product insertedProduct2 = new Product("chupa-chups", 2);

        inventory.put(index1, insertedProduct1);
        inventory.put(index2, insertedProduct2);
        // then
        Product gotProduct1 = inventory.get(index1);
        Product gotProduct2 = inventory.get(index2);
        assertEquals(insertedProduct1, gotProduct1);
        assertEquals(insertedProduct2, gotProduct2);
    }

    @Test
    public void clean_shouldBeClean() throws Exception {
        // given
        int index = 1;
        // given
        HashMap<Integer, List<Product>> shelves = new HashMap<>();
        products.add(new Product("chupa-chups", 2));
        shelves.put(index, products);
        inventory = new ShelveInventory(shelves);
        // when
        inventory.clean();
        // then
        Product product = inventory.get(index);
        assertNull(product);
    }

}
