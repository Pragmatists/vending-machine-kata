package tdd.vendingmachine.domain;

class TransactionNotPayed implements CoinInserted {

    private final TransactionState transactionState;
    private final MachineMoney machineMoney;
    private final Shelves shelves;
    private final ChangeDispenser changeDispenser;
    private final ProductDispenser productDispenser;

    TransactionNotPayed(TransactionState transactionState, MachineMoney machineMoney, Shelves shelves,
                        ChangeDispenser changeDispenser, ProductDispenser productDispenser) {
        this.transactionState = transactionState;
        this.machineMoney = machineMoney;
        this.shelves = shelves;
        this.changeDispenser = changeDispenser;
        this.productDispenser = productDispenser;
    }

    @Override
    public Display display() {
        return Display.money(transactionState.amountLeftToPay());
    }

    @Override
    public TransactionState transactionState() {
        return transactionState.changeToCoinInserted();
    }

    @Override
    public MachineMoney machineMoney() {
        return machineMoney;
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
        return changeDispenser;
    }
}
