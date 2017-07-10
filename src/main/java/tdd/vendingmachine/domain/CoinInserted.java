package tdd.vendingmachine.domain;

interface CoinInserted {
    Display display();
    TransactionState transactionState();
    MachineMoney machineMoney();
    Shelves shelves();
    ProductDispenser productDispenser();
    ChangeDispenser changeDispenser();
}
