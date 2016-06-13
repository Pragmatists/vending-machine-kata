package tdd.vendingMachine.impl;

import tdd.vendingMachine.core.*;

import java.util.*;

public class BasicTransaction implements Transaction {

    private final Shelf shelf;
    private final Set<CurrencyUnit> allowedDenominations;
    private final List<CurrencyUnit> insertedCoins = new ArrayList<>();
    private CurrencyUnit insertedCoinsSum = CurrencyUnit.zero();

    public BasicTransaction(Shelf shelf, Set<CurrencyUnit> allowedDenominations) {
        if (shelf == null || !shelf.hasProducts()) {
            throw new IllegalArgumentException("Transaction should be created using only correct shelf with products");
        }

        this.shelf = shelf;
        this.allowedDenominations = allowedDenominations != null ? allowedDenominations : new HashSet<>();
    }

    @Override
    public Transaction insertCoin(CurrencyUnit currencyUnit) {
        if (currencyUnit != null) {
            if (!allowedDenominations.isEmpty() && !allowedDenominations.contains(currencyUnit)) {
                throw new IllegalArgumentException("Coin of value '" + currencyUnit.value() + "' is not allowed to accept");
            }

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

    @Override
    public Collection<CurrencyUnit> rollback() {
        Collection<CurrencyUnit> result = new ArrayList<>(insertedCoins);

        insertedCoins.clear();
        insertedCoinsSum = CurrencyUnit.zero();

        return result;
    }

    @Override
    public PurchaseResult commit() {
        return new BasicPurchaseResult();
    }
}
