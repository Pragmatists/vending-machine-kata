package tdd.vendingMachine.domain;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Agustin on 2/19/2017.
 * @since 1.0
 * This class represents a shelf that contains products of the same type
 */
public class Shelf {

    public final String id;
    public final String type;
    public final int capacity;
    private final AtomicInteger itemCount;

    Shelf(String id, String type, int capacity, int itemCount) {
        this.type = type;
        this.capacity = capacity;
        this.itemCount = new AtomicInteger(itemCount);
        this.id = id;
    }

    public int getItemCount() {
        return itemCount.get();
    }

    public boolean isEmpty() {
        return itemCount.get() == 0;
    }
}
