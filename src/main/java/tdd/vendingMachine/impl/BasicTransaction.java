package tdd.vendingMachine.impl;

import tdd.vendingMachine.core.CurrencyUnit;
import tdd.vendingMachine.core.Shelf;
import tdd.vendingMachine.core.Transaction;

import java.util.ArrayList;
import java.util.List;

public class BasicTransaction implements Transaction {

    private final Shelf shelf;
    private final List<CurrencyUnit> insertedCoins = new ArrayList<>();
    private CurrencyUnit insertedCoinsSum = CurrencyUnit.zero();

    public BasicTransaction(Shelf shelf) {
        if (shelf == null || !shelf.hasProducts()) {
            throw new IllegalArgumentException("Transaction should be created using only correct shelf with products");
        }

        this.shelf = shelf;
    }

    @Override
    public Transaction insertCoin(CurrencyUnit currencyUnit) {
        if (currencyUnit != null) {
            insertedCoins.add(currencyUnit);
            insertedCoinsSum = insertedCoinsSum.add(currencyUnit);
            return this;
        }

        throw new IllegalArgumentException("Transaction accepts only valid coins");
    }

    @Override
    public CurrencyUnit getShortFall() {
        CurrencyUnit price = shelf.getProductPrice();

        if (price.greaterOrEqualThan(insertedCoinsSum)) {
            return price.subtract(insertedCoinsSum);
        }

        return CurrencyUnit.zero();
    }
}
