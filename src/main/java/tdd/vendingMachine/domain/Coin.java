package tdd.vendingMachine.domain;

/**
 * @author Agustin on 2/19/2017.
 * @since 1.0
 * Enum representing the coins available/acceptable by the vending machine
 */
public enum Coin {
    ONE("1.0", 1.0),
    TWO("2.0", 2.0),
    FIVE("5.0", 5.0),
    TEN_CENTS("1.0", 1.0),
    TWENTY_CENTS("2.0", 1.0),
    FIFTY_CENTS("5.0", 1.0);

    /**
     * label since is final can be public
     */
    public final String label;

    /**
     * denomination of the coin since is final can be public
     */
    public final double denomination;

    Coin(String label, double denomination) {
        this.label = label;
        this.denomination = denomination;
    }
}
