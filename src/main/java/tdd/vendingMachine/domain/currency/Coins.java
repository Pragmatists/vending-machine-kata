package tdd.vendingMachine.domain.currency;

public enum Coins {

    COIN_5(50),
    COIN_2(20),
    COIN_1(10),
    COIN_0_5(5),
    COIN_0_2(2),
    COIN_0_1(1);

    private int value;

    Coins(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
