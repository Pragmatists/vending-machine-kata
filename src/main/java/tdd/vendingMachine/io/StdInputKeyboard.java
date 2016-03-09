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
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String input = reader.readLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            return -1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
