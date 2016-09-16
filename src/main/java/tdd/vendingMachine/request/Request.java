package tdd.vendingMachine.request;

import lombok.Getter;
import tdd.vendingMachine.product.Product;

public class Request {
    @Getter
    private final int shelfNumber;
    @Getter
    private final Product product;

    public Request(int shelfNumber, Product product) {
        this.shelfNumber = shelfNumber;
        this.product = product;
    }

}
