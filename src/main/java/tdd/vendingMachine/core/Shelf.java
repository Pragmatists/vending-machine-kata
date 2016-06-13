package tdd.vendingMachine.core;

public interface Shelf {
    Shelf charge(int amountOfProducts);
    int amount();
    boolean hasProducts();
    Product withdraw();
    String getProductName();
    CurrencyUnit getProductPrice();
}
