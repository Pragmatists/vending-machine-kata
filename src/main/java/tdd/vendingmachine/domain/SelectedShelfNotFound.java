package tdd.vendingmachine.domain;

class SelectedShelfNotFound implements SelectedShelf {

    private final ShelfNumber shelfNumber;

    SelectedShelfNotFound(ShelfNumber shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    @Override
    public Display display() {
        return Display.shelfNotFound(shelfNumber);
    }

    @Override
    public TransactionState newTransactionState() {
        return TransactionState.clear();
    }
}
