package tdd.vendingMachine.shelve;

import tdd.vendingMachine.product.Product;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by okraskat on 06.02.16.
 */
public class DefaultShelve<T extends Product> implements Shelve {

    private final List<T> products;
    private final BigDecimal productPrice;
    private final String productName;

    public DefaultShelve(List<T> products, BigDecimal productPrice, String productName) {
        checkNotNull(productName, "Product name can not be null");
        this.productName = productName;
        checkNotNull(products, "Products can not be null");
        this.products = products;
        checkNotNull(productPrice, "Product price can not be null");
        this.productPrice = productPrice;
    }

    @Override
    public List<T> getProducts() {
        return products;
    }

    @Override
    public BigDecimal getProductPrice() {
        return productPrice;
    }

    @Override
    public String getProductName() {
        return productName;
    }

    @Override
    public String toString() {
        return "Shelve products=" + products.size() +
            ", productPrice=" + productPrice +
            ", productName='" + productName + '\'';
    }
}
