package tdd.vendingmachine.domain;

class SelectedShelfIsEmpty implements SelectedShelf {

    private final ShelfNumber shelfNumber;

    SelectedShelfIsEmpty(ShelfNumber shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    @Override
    public Display display() {
        return Display.emptyShelf(shelfNumber);
    }
}
