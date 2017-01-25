package tdd.vendingMachine;

/**
 * Created by dzalunin on 2017-01-25.
 */
public class Preconditions {

    public static void checkArgument(boolean expr, String msg) {
        if (!expr)
            throw new IllegalArgumentException(msg);
    }

    public static void checkState(boolean expr, String msg) {
        if (!expr)
            throw new IllegalStateException(msg);
    }

}
