package tdd.vendingmachine.domain;

class TransactionPaidAndChangeUnavailable implements CoinInserted {

    private final TransactionState transactionState;
    private final MachineMoney machineMoney;
    private final Shelves shelves;
    private final ChangeDispenser changeDispenser;
    private final ProductDispenser productDispenser;

    TransactionPaidAndChangeUnavailable(TransactionState transactionState, MachineMoney machineMoney, Shelves shelves,
                                        ChangeDispenser changeDispenser, ProductDispenser productDispenser) {
        this.transactionState = transactionState;
        this.machineMoney = machineMoney;
        this.shelves = shelves;
        this.changeDispenser = ChangeDispenser.of(changeDispenser);
        this.productDispenser = productDispenser;
    }

    @Override
    public Display display() {
        return Display.changeCannotBeGiven();
    }

    @Override
    public TransactionState transactionState() {
        return TransactionState.clear();
    }

    @Override
    public MachineMoney machineMoney() {
        Coins transactionCoins = transactionState.coins();
        return machineMoney.remove(transactionCoins);
    }

    @Override
    public Shelves shelves() {
        return shelves;
    }

    @Override
    public ProductDispenser productDispenser() {
        return productDispenser;
    }

    @Override
    public ChangeDispenser changeDispenser() {
        Coins transactionCoins = transactionState.coins();
        changeDispenser.put(transactionCoins);
        return changeDispenser;
    }
}
