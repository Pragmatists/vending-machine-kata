package tdd.vendingmachine.domain;

import lombok.ToString;

import java.util.Objects;

@ToString
class TransactionState {

    private final Shelf selectedShelf;
    private final Coins coins;

    private TransactionState(Shelf selectedShelf, Coins coins) {
        this.selectedShelf = selectedShelf;
        this.coins = Objects.requireNonNull(coins);
    }

    private TransactionState(Shelf selectedShelf) {
        this(selectedShelf, Coins.empty());
    }

    private TransactionState() {
        this(null);
    }

    static TransactionState clear() {
        return new TransactionState();
    }

    static TransactionState shelfSelected(Shelf selectedShelf) {
        return new TransactionState(Objects.requireNonNull(selectedShelf));
    }

    TransactionState add(Coin coin) {
        return new TransactionState(selectedShelf, coins.add(coin));
    }

    Money amountLeftToPay() {
        return selectedShelf.price().subtract(coins.moneyValue());
    }
}
