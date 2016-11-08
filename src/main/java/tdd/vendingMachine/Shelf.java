package tdd.vendingMachine;

import java.util.concurrent.atomic.AtomicInteger;

public class Shelf {
    private Product product;
    private AtomicInteger productCount;
    
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public AtomicInteger getProductCount() {
        return productCount;
    }
    public void setProductCount(AtomicInteger productCount) {
        this.productCount = productCount;
    }
}
