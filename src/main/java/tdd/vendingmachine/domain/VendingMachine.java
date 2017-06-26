package tdd.vendingmachine.domain;

import lombok.ToString;
import tdd.vendingmachine.domain.dto.VendingMachineDto;

import java.util.Objects;

@ToString
class VendingMachine {

    private final Shelves shelves;
    private Display display;

    private VendingMachine(Shelves shelves) {
        this.shelves = Objects.requireNonNull(shelves);
        display = Display.empty();
    }

    static VendingMachine create(VendingMachineDto vendingMachineDto) {
        Shelves shelves = Shelves.create(vendingMachineDto.getShelves());
        return new VendingMachine(shelves);
    }

    void selectShelfNumber(ShelfNumber shelfNumber) {
        SelectedShelf selectedShelf = new ShelfSelector(shelves).select(shelfNumber);
        display = selectedShelf.display();
    }

    String showDisplay() {
        return display.show();
    }
}
