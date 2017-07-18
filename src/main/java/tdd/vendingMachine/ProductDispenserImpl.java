package tdd.vendingMachine;

import com.google.inject.Singleton;
import tdd.vendingMachine.dto.Product;

import java.util.ArrayList;
import java.util.List;

@Singleton
class ProductDispenserImpl implements ProductDispenser {

    private final List<Product> products = new ArrayList<>();

    @Override
    public void ejectProduct(Product product) {
        this.products.add(product);
    }

    @Override
    public List<Product> retrieveProducts() {
        List<Product> products = new ArrayList<>(this.products);
        this.products.clear();
        return products;
    }
}
