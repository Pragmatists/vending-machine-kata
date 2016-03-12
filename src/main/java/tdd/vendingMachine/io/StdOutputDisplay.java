package tdd.vendingMachine.io;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class StdOutputDisplay implements Display {
    @Override
    public void display(String message) {
        System.out.print(message);
    }
}
