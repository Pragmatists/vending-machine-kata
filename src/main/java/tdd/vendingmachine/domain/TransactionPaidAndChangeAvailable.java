package tdd.vendingmachine.domain;

class TransactionPaidAndChangeAvailable implements CoinInserted {

    private final TransactionState transactionState;
    private final MachineMoney machineMoney;
    private final Shelves shelves;
    private final ChangeDispenser changeDispenser;
    private final ProductDispenser productDispenser;
    private final Change change;

    TransactionPaidAndChangeAvailable(TransactionState transactionState, MachineMoney machineMoney,
                                      Shelves shelves, ChangeDispenser changeDispenser,
                                      ProductDispenser productDispenser,
                                      Change change) {
        this.transactionState = transactionState;
        this.machineMoney = machineMoney;
        this.shelves = shelves;
        this.changeDispenser = ChangeDispenser.of(changeDispenser);
        this.productDispenser = productDispenser;
        this.change = change;
    }

    @Override
    public Display display() {
        return Display.empty();
    }

    @Override
    public TransactionState transactionState() {
        return TransactionState.clear();
    }

    @Override
    public MachineMoney machineMoney() {
        return machineMoney.remove(change.coins());
    }

    @Override
    public Shelves shelves() {
        return shelves.removeProductFromShelf(transactionState.shelfNumber());
    }

    @Override
    public ProductDispenser productDispenser() {
        return productDispenser.put(transactionState.productType());
    }

    @Override
    public ChangeDispenser changeDispenser() {
        return changeDispenser.put(change.coins());
    }
}
