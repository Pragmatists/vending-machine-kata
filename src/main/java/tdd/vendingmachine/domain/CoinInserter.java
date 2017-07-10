package tdd.vendingmachine.domain;

class CoinInserter {

    private final TransactionState transactionState;
    private final MachineMoney machineMoney;
    private final Shelves shelves;
    private final ChangeDispenser changeDispenser;
    private final ProductDispenser productDispenser;

    CoinInserter(TransactionState transactionState, MachineMoney machineMoney, Shelves shelves,
                 ChangeDispenser changeDispenser, ProductDispenser productDispenser) {
        this.transactionState = transactionState;
        this.machineMoney = machineMoney;
        this.shelves = shelves;
        this.changeDispenser = changeDispenser;
        this.productDispenser = productDispenser;
    }

    CoinInserted insert(Coin coin) {
        if (isCoinNotAcceptable(coin)) {
            return new CoinNotAcceptable(transactionState, machineMoney, shelves, changeDispenser, productDispenser,
                coin);
        }
        MachineMoney newMachineMoney = machineMoney.add(coin);
        TransactionState newTransactionState = transactionState.add(coin);
        if (!newTransactionState.isPaid()) {
            return new TransactionNotPayed(newTransactionState, newMachineMoney, shelves, changeDispenser,
                productDispenser);
        }
        Money amountOfChange = newTransactionState.amountOfChange();
        return newMachineMoney.calculateChange(amountOfChange)
                              .map(change -> new TransactionPaidAndChangeAvailable(newTransactionState, newMachineMoney,
                                  shelves, changeDispenser, productDispenser, change))
                              .map(strategy -> (CoinInserted) strategy)
                              .orElse(new TransactionPaidAndChangeUnavailable(newTransactionState, newMachineMoney,
                                  shelves, changeDispenser, productDispenser));
    }

    private boolean isCoinNotAcceptable(Coin coin) {
        return !machineMoney.isCoinAcceptable(coin);
    }
}
