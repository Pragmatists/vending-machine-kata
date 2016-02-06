package tdd.vendingMachine.errors;

/**
 * Created by okraskat on 06.02.16.
 */
public class ShelveNotFound extends RuntimeException {

    public ShelveNotFound(Integer shelveNumber) {
        super("Could not find shelve with number " + shelveNumber);
    }
}
