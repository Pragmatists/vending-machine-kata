package tdd.vendingMachine;

/**
 * Created by dzalunin on 2017-01-25.
 */
public enum Denomination {
    FIVE(500),
    TWO(200),
    ONE(100),
    FIFTY_CENTS(50),
    TWENTY_CENTS(20),
    TEN_CENTS(10);

    private long value;

    Denomination(long value) {
        this.value = value;
    }

    public long value() {
        return value;
    }
}
