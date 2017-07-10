package tdd.vendingmachine.domain;

class SelectedShelfSuccessfully implements SelectedShelf {

    private final Shelf shelf;

    SelectedShelfSuccessfully(Shelf shelf) {
        this.shelf = shelf;
    }

    @Override
    public Display display() {
        return Display.money(shelf.price());
    }

    @Override
    public TransactionState newTransactionState() {
        return TransactionState.shelfSelected(shelf);
    }
}
