package tdd.vendingmachine.domain;

import lombok.ToString;

import java.util.Objects;

@ToString
class TransactionState {

    private final Shelf selectedShelf;
    private final Coins coins;
    private final TransactionPhase transactionPhase;

    private TransactionState(Shelf selectedShelf, Coins coins, TransactionPhase transactionPhase) {
        this.selectedShelf = selectedShelf;
        this.coins = Objects.requireNonNull(coins);
        this.transactionPhase = Objects.requireNonNull(transactionPhase);
    }

    private TransactionState(Shelf selectedShelf) {
        this(selectedShelf, Coins.empty(), TransactionPhase.SHELF_SELECTED);
    }

    private TransactionState() {
        this(null, Coins.empty(), TransactionPhase.IDLE);
    }

    private TransactionState changeTo(TransactionPhase phase) {
        return new TransactionState(selectedShelf, coins, phase);
    }

    static TransactionState clear() {
        return new TransactionState();
    }

    static TransactionState shelfSelected(Shelf selectedShelf) {
        return new TransactionState(Objects.requireNonNull(selectedShelf));
    }

    Coins coins() {
        return coins;
    }

    TransactionState add(Coin coin) {
        Coins newCoins = coins.add(coin);
        return new TransactionState(selectedShelf, newCoins, transactionPhase);
    }

    boolean isPaid() {
        return selectedShelf.price().isMetWith(coins.moneyValue());
    }

    Money amountLeftToPay() {
        return selectedShelf.price().amountLeftToPayAfterPaying(coins.moneyValue());
    }

    ProductType productType() {
        return selectedShelf.productType();
    }

    TransactionState changeToCoinInserted() {
        return changeTo(TransactionPhase.COIN_INSERTED);
    }

    TransactionPhase phase() {
        return transactionPhase;
    }

    Money amountOfChange() {
        return selectedShelf.price().amountOfChangeAfterPaying(coins.moneyValue());
    }

    ShelfNumber shelfNumber() {
        return selectedShelf.number();
    }
}
