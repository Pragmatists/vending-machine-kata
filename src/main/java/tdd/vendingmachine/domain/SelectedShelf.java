package tdd.vendingmachine.domain;

interface SelectedShelf {

    Display display();

    TransactionState newTransactionState();
}
