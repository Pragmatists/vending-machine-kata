package tdd.vendingMachine.domain;

/**
 * @author Agustin Cabra on 2/20/2017.
 * @since 1.0
 *
 * Interface that enables the method provideType and provideValue
 * this allows them to be stored in shelves
 */
public interface ShelfItem {

    /**
     * The type of the item to be shelved
     * @return string
     */
    String provideType();

    /**
     * The value in cents of the item on the shelve
     * @return int
     */
    int provideValue();
}
