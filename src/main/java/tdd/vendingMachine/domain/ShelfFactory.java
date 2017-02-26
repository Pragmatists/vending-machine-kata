package tdd.vendingMachine.domain;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.InputMismatchException;

/**
 * @author Agustin on 2/19/2017.
 * @since 1.0
 * This is an utility factory class to build shelves of shelfItems
 */
public class ShelfFactory {

    private static <T extends ShelfItem> void validateShelfInput(@NonNull T shelfItem, int capacity, int itemCount) {
        if (StringUtils.isEmpty(shelfItem.provideType())) {
            throw new InputMismatchException(VendingMachineMessages.SHELF_TYPE_MUST_NOT_BE_EMPTY.label);
        }
        if (capacity <= 0) {
            throw new InputMismatchException("Invalid capacity must be greater than zero");
        }
        if (itemCount < 0 || itemCount > capacity) {
            throw new InputMismatchException("Invalid item count must equal or greater than zero and less or equal than capacity");
        }
    }

    /**
     * Build a shelf of given type and given capacity
     * @param id the shelf id
     * @param shelfItem The shelfItem type to be contained on the shelf
     * @param capacity the capacity of the shelf
     * @return a Shelf object
     */
    public static <T extends ShelfItem> Shelf<T> buildShelf(int id, T shelfItem, int capacity) {
        return ShelfFactory.build(id, shelfItem, capacity, 0);
    }

    /**
     * Build a shelf of given type and given capacity and initial item count
     * @param id the shelf id
     * @param shelfItem The shelfItem type to be listed on the shelf
     * @param capacity the capacity of the shelf
     * @param itemCount the amount of items loaded on the shelf
     * @return a Shelf object
     */
    public static <T extends ShelfItem> Shelf<T> buildShelf(int id, T shelfItem, int capacity, int itemCount) {
        return ShelfFactory.build(id, shelfItem, capacity, itemCount);
    }

    /**
     * This method is the only one instantiating objects
     * @param shelfItem The shelfItem be listed on the shelf
     * @param capacity the capacity of the shelf
     * @param itemCount the amount of items loaded on the shelf
     * @return a Shelf object
     */
    private static <T extends ShelfItem> Shelf<T> build(int id, T shelfItem, int capacity, int itemCount) {
        validateShelfInput(shelfItem, capacity, itemCount);
        return new Shelf<>(id, shelfItem, capacity, itemCount);
    }
}
