package tdd.vendingMachine;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class VendingMachine {

    private final int shelfCount;
    private final Keyboard keyboard;

    @Setter
    private VendingMachineState state;

    public void start(VendingMachineState state) {
        this.state = state;
        this.state.proceed(this);
    }

    public Optional<Integer> readSelectedShelfNumber() {
        final int input = keyboard.readNumber();
        if (input > 1 && input <= shelfCount) {
            return Optional.of(input);
        } else {
            return Optional.empty();
        }
    }
}
