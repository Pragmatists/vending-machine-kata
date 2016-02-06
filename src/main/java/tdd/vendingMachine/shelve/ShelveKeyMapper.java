package tdd.vendingMachine.shelve;

import com.google.common.base.Function;

/**
 * Created by okraskat on 06.02.16.
 */
public class ShelveKeyMapper implements Function<Shelve, Integer> {

    private static Integer counter = 0;

    @Override
    public Integer apply(Shelve shelve) {
        return ++counter;
    }
}
