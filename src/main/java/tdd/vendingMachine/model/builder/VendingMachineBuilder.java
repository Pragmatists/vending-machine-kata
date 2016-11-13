package tdd.vendingMachine.model.builder;

import java.util.ArrayList;
import java.util.List;

import tdd.vendingMachine.model.Shelf;
import tdd.vendingMachine.model.VendingMachine;

public class VendingMachineBuilder implements IBuilder<VendingMachine> {
    private List<Shelf> shelfs = new ArrayList<>();

    public VendingMachineBuilder withShelf(IBuilder<Shelf> shelfBuilder) {
        this.shelfs.add(shelfBuilder.build());

        return this;
    }
    
    @Override
    public VendingMachine build() {
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setShelfs(this.shelfs);
        
        return vendingMachine;
    }

}
