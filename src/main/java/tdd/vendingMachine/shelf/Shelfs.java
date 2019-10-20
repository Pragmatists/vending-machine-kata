package tdd.vendingMachine.shelf;

import java.util.HashMap;

public class Shelfs extends HashMap<Integer, IShelf> {
    public static final int FIRST_SHELF_NUMBER = 1;
    public static final int SHELFS_NUMBER = 10;

    public Shelfs() {
        initializeShelfs();
    }

    private void initializeShelfs() {
        for (int shelfNumber = FIRST_SHELF_NUMBER; shelfNumber <= SHELFS_NUMBER; shelfNumber++) {
            put(shelfNumber, new Shelf(shelfNumber));
        }
    }
}
