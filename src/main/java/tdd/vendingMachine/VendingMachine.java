package tdd.vendingMachine;

import java.util.ArrayList;
import java.util.List;

public class VendingMachine {
    private List<Shelf> shelfs = new ArrayList<>();

    public List<Shelf> getShelfs() {
        return shelfs;
    }

    public void setShelfs(List<Shelf> shelfs) {
        this.shelfs = shelfs;
    }

}
