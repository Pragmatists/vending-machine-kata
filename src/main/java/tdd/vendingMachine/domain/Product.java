package tdd.vendingMachine.domain;

public enum Product {
    COLA(Money.of("12.5")),
    CHIPS(Money.of("8")),
    CROISSANT(Money.of("5.99")),;

    Product(Money price) {
        this.price = price;
    }

    private final Money price;

    public Money price() {
        return price;
    }

}
