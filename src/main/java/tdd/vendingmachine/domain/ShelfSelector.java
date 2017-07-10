package tdd.vendingmachine.domain;

import java.util.Objects;
import java.util.Optional;

class ShelfSelector {

    private final Shelves shelves;

    ShelfSelector(Shelves shelves) {
        this.shelves = Objects.requireNonNull(shelves);
    }

    SelectedShelf select(ShelfNumber shelfNumber) {
        Optional<Shelf> shelfOptional = shelves.findBy(shelfNumber);
        if (!shelfOptional.isPresent()) {
            return new SelectedShelfNotFound(shelfNumber);
        }
        Shelf shelf = shelfOptional.get();
        if (shelf.isEmpty()) {
            return new SelectedShelfIsEmpty(shelfNumber);
        }
        return new SelectedShelfSuccessfully(shelf);
    }
}
