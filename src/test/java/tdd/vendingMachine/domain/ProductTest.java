package tdd.vendingMachine.domain;

import org.junit.Assert;
import org.junit.Test;
import tdd.vendingMachine.util.Constants;

/**
 * Created by Agustin on 2/19/2017.
 */
public class ProductTest {

    @Test
    public void should_provide_price_and_description_from_product() {
        String productType = "Cola drink 0.25l";
        int productPrice = 150;
        Product productStub = new Product(productPrice, productType);
        Assert.assertNotEquals(null, productStub);
        Assert.assertEquals(productType, productStub.getType());
        Assert.assertEquals(productPrice, productStub.getPrice());
    }
}
