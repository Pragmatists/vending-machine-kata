package tdd.vendingMachine.domain.parts.money;

public enum Coin {

    COIN_0_1("0.1"),
    COIN_0_2("0.2"),
    COIN_0_5("0.5"),
    COIN_1("1.0"),
    COIN_2("2.0"),
    COIN_5("5.0");

    private final Money denomination;

    Coin(String money) {
        this.denomination = Money.createMoney(money);
    }

    public Money getDenomination() {
        return denomination;
    }
}
