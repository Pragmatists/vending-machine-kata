package tdd.vendingmachine.domain;

class CoinNotAcceptable implements CoinInserted {

    private final TransactionState transactionState;
    private final Shelves shelves;
    private final MachineMoney machineMoney;
    private final ChangeDispenser changeDispenser;
    private final ProductDispenser productDispenser;
    private final Coin coin;

    CoinNotAcceptable(TransactionState transactionState, MachineMoney machineMoney, Shelves shelves,
                      ChangeDispenser changeDispenser, ProductDispenser productDispenser, Coin coin) {
        this.transactionState = transactionState;
        this.shelves = shelves;
        this.machineMoney = machineMoney;
        this.changeDispenser = ChangeDispenser.of(changeDispenser);
        this.productDispenser = productDispenser;
        this.coin = coin;
    }

    @Override
    public Display display() {
        return Display.coinNotAcceptable();
    }

    @Override
    public TransactionState transactionState() {
        return transactionState;
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
        return changeDispenser.put(coin);
    }
}
