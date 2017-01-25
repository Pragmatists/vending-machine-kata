package tdd.vendingMachine;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by dzalunin on 2017-01-25.
 * This class is responsible for:
 * 1. storing the products
 * 2. enable to peek the product from the given shelve
 * 3. remove the product from given shelve
 * 4. supply products to shelve
 */
public class Shelves {

    public static final int MAX_CAPACITY = 10;

    private Product[] products;
    private int[] quantity;

    public Shelves() {
        this(MAX_CAPACITY);
    }

    public Shelves(int shelveCapacity) {
        this.products = new Product[shelveCapacity];
        this.quantity = new int[shelveCapacity];
    }

    public Product peek(int shelveNo) {
        Preconditions.checkArgument(shelveNo > 0, "schelveNo should be greater thatn 0");
        Preconditions.checkArgument(shelveNo < products.length, "schelveNo should be less thatn " + products.length);
        return products[shelveNo];
    }

    public Product take(int shelveNo) {
        Preconditions.checkArgument(shelveNo > 0, "schelveNo should be greater thatn 0");
        Preconditions.checkArgument(shelveNo < products.length, "schelveNo should be less thatn " + products.length);
        if (quantity[shelveNo] > 0) {
            quantity[shelveNo] -= 1;
        }
        return products[shelveNo];
    }

    public void supplyProduct(int shelveNo, Product product, int q) throws ShelveAlreadyIsBusy {
        Preconditions.checkArgument(shelveNo > 0, "schelveNo should be greater thatn 0");
        Preconditions.checkArgument(shelveNo < products.length, "schelveNo should be less thatn " + products.length);
        Preconditions.checkArgument(q > 0, "quantity should be greater thatn 0 " + products.length);
        Preconditions.checkArgument(product != null, "product can't be null ");
        Product actual = products[shelveNo];
        if (actual != null && !product.equals(actual)) {
            throw new ShelveAlreadyIsBusy("shelve " + shelveNo + " already occupied by "
                    + actual.getName());
        }

        products[shelveNo] = product;
        quantity[shelveNo] += q;
    }

    public long count(int shelveNo) {
        Preconditions.checkArgument(shelveNo > 0, "schelveNo should be greater thatn 0");
        Preconditions.checkArgument(shelveNo < products.length, "schelveNo should be less thatn " + products.length);
        return quantity[shelveNo];
    }

}
