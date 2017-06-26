package tdd.vendingmachine.domain;

class SelectedShelfSuccessfully implements SelectedShelf {

    private final Shelf shelf;

    SelectedShelfSuccessfully(Shelf shelf) {
        this.shelf = shelf;
    }

    @Override
    public Display display() {
        return Display.price(shelf.price());
    }
}
