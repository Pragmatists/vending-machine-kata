package tdd.vendingMachine.cash.coin;


import lombok.Getter;

public class Coin {
    @Getter
    private Double value;

    public Coin(Double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object second) {
        return second instanceof Coin && this.value.equals(((Coin) second).getValue());
    }

}
