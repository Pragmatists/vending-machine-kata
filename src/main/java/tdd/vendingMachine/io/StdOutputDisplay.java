package tdd.vendingMachine.io;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class StdOutputDisplay implements Display {
    @Override
    public void display(String message, Object... args) {
        System.out.print(String.format(message, args));
    }
}
