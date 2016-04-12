package tdd.vendingMachine.test_infrastructure;

import java.util.Arrays;
import java.util.function.Consumer;

public class MethodCaller {

    public static <T> void callForEachArgument(Consumer<T> operation, T... arguments) {
        Arrays.stream(arguments).forEach(operation);
    }
}
