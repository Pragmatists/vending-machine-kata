package tdd.vendingMachine;

import org.junit.Assert;
import org.junit.Test;
import tdd.vendingMachine.domain.Product;

/**
 * Created by Agustin on 2/19/2017.
 */
public class ProductTest {

    @Test
    public void should_provide_price_and_description_from_product() {
        String productDescription = "Cola drink 0.25l";
        double productPrice = 1.5;
        Product productStub = new Product(productPrice, productDescription);
        Assert.assertNotEquals(null, productStub);
        Assert.assertEquals(productDescription, productStub.getDescription());
        Assert.assertEquals(productPrice, productStub.getPrice(), 0.0001);
    }
}
