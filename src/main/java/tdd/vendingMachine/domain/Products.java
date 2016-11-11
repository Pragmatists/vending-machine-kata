package tdd.vendingMachine.domain;

public enum Products {

    COCA_COLA_0_33(2);

    private int price;

    Products(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
