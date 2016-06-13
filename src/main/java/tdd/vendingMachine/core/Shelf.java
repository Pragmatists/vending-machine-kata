package tdd.vendingMachine.core;

public interface Shelf {
    Shelf charge(int amountOfProducts);
    boolean hasProducts();
    Product withdraw();
}
