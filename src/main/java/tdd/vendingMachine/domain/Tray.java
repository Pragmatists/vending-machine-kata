package tdd.vendingMachine.domain;

public class Tray {

    private Products product;
    private int count;

    public Tray(Products product, int initialCount) {
        this.product = product;
        this.count = initialCount;
    }

    public int getProductCount() {
        return count;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public Tray removeProduct(int countToRemove) {
        if (countToRemove > count) {
            throw new IllegalArgumentException("Not enough products on tray");
        }

        count -= countToRemove;

        return this;
    }

    public Products getProduct() {
        return product;
    }
}
