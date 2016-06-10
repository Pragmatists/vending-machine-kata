package tdd.vendingMachine.core;

public interface Shelf<T extends Product> {
    Shelf<T> put(T product);
    boolean hasProducts();
    T withdraw();
}
