package tdd.vendingMachine.impl;

import tdd.vendingMachine.core.Shelf;

import java.util.ArrayDeque;
import java.util.Deque;

public class BasicShelf implements Shelf<BasicProduct> {

    private final Deque<BasicProduct> products = new ArrayDeque<>();

    @Override
    public Shelf<BasicProduct> put(BasicProduct product) {
        products.add(product);
        return this;
    }

    @Override
    public boolean hasProducts() {
        return !products.isEmpty();
    }

    @Override
    public BasicProduct withdraw() {
        return products.poll();
    }
}
