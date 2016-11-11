package tdd.vendingMachine.domain;

public enum Products {

    COCA_COLA_0_33(20),
    COCA_COLA_0_5(25),
    WATER_0_5(17),
    CHOCOLATE_BAR(31);

    private int price;

    Products(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
