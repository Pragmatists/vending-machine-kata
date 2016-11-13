package tdd.vendingMachine.domain.product;

import java.util.ArrayList;
import java.util.List;

public class ProductBox {

    private List<Tray> trays = new ArrayList<>();

    public Integer getAvailableTrays() {
        return trays.size();
    }


    public ProductBox addTray(Tray tray) {
        trays.add(tray);

        return this;
    }

    public Tray getTray(int i) {
        if (trays.size() < i) {
            throw(new IllegalArgumentException("Invalid tray selected"));
        }

        return trays.get(i);
    }
}
