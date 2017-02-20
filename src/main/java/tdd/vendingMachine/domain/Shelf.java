package tdd.vendingMachine.domain;

import java.util.InputMismatchException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Agustin on 2/19/2017.
 * @since 1.0
 * This class represents a shelf that contains products of the same type
 */
public class Shelf<T extends ShelfItem> {

    public final String id;
    public final T type;
    public final int capacity;
    private final AtomicInteger itemCount;

    Shelf(String id, T type, int capacity, int itemCount) {
        this.type = type;
        this.capacity = capacity;
        this.itemCount = new AtomicInteger(itemCount);
        this.id = id;
    }

    private void validatePositiveAmount(int amount) {
        if (amount < 0) {
            throw new InputMismatchException("The amount of coins on the operation must be positive");
        }
    }

    /**
     * Provides the amount of items in the shelf
     * @return int
     */
    public int getItemCount() {
        return itemCount.get();
    }

    /**
     * Test if the shelf is empty (no items)
     * @return boolean true if empty shelf
     */
    public boolean isEmpty() {
        return itemCount.get() == 0;
    }

    /**
     * The type of element that the shelf stores
     * @return
     */
    public T getType() {
        return type;
    }

    /**
     * Counts the remaining available slots on the shelf
     * @return int
     */
    public int countFreeSlots() {
        return capacity - itemCount.get();
    }

    /**
     * Provisions the given amount items to the shelf
     * @param amount the amount of items to provision
     * @return int representing the current amount of items after provision.
     */
    public int provision(int amount) {
        validatePositiveAmount(amount);
        int freeSlots = countFreeSlots();
        if (freeSlots >= amount) {
            return itemCount.addAndGet(amount);
        }
        throw new InputMismatchException(String.format("unable to provision %d coins only %d available", amount, freeSlots));
    }

    /**
     * Helper method to provision only one item
     * @return int the amount of items after provision
     */
    public int provision() {
        return provision(1);
    }

    /**
     * Dispenses the given amount items from the shelf
     * @param amount the amount of items to dispense
     * @return int representing the current amount of items after provision.
     * @throws InputMismatchException
     */
    public int dispense(int amount) throws InputMismatchException {
        validatePositiveAmount(amount);
        if(itemCount.get() >= amount) {
            return itemCount.addAndGet(-amount);
        }
        throw new InputMismatchException(String.format("Unable to dispense %d elements out of %d available", amount, itemCount.get()));
    }

    /**
     * Helper method to dispenses one item from the shelf
     * @return int representing the current amount of items after dispensing.
     * @throws InputMismatchException
     */
    public int dispense() {
        return dispense(1);
    }
}
