package tdd.vendingMachine;

import tdd.vendingMachine.dto.Product;

import java.util.List;

interface ProductDispenser {
    void ejectProduct(Product product);

    List<Product> retrieveProducts();
}
