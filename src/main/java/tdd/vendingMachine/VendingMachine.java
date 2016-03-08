package tdd.vendingMachine;

import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VendingMachine {

    private final int shelfCount;
    private final Keyboard keyboard;

    public Optional<Integer> readSelectedShelfNumber() {
        final int input = keyboard.readNumber();
        if (input > 1 && input <= shelfCount) {
            return Optional.of(input);
        } else {
            return Optional.empty();
        }
    }
}
