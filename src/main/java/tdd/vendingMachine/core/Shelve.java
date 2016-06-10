package tdd.vendingMachine.core;

public interface Shelve<T extends Product> {
    Shelve<T> put(T product);
    boolean hasProducts();
    T withdraw();
}
