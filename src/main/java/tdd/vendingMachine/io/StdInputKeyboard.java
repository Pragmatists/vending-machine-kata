package tdd.vendingMachine.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class StdInputKeyboard implements Keyboard {

    @Override
    public int readNumber() {
        String input = readInput();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    @Override
    public String readInput() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
