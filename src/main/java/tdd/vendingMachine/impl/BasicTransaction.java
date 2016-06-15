package tdd.vendingMachine.impl;

import tdd.vendingMachine.core.*;

import java.util.*;

public class BasicTransaction implements Transaction {

    private final Shelf shelf;
    private final AllowedDenominations allowedDenominations;
    private final CashHandler cashHandler;
    private final List<CurrencyUnit> insertedCoins = new ArrayList<>();
    private CurrencyUnit insertedCoinsSum = CurrencyUnit.zero();

    public BasicTransaction(Shelf shelf, CashHandler cashHandler, AllowedDenominations allowedDenominations) {
        if (shelf == null || !shelf.hasProducts()) {
            throw new IllegalArgumentException("Transaction should be created using only correct shelf with products");
        }

        if (cashHandler == null) {
            throw new IllegalArgumentException("Transaction should be create using only valid cash handler");
        }

        this.shelf = shelf;
        this.cashHandler = cashHandler;
        this.allowedDenominations = allowedDenominations;
    }

    @Override
    public Transaction insertCoin(CurrencyUnit currencyUnit) {
        if (currencyUnit != null) {
            if (currencyUnit.isNegative() || currencyUnit.isZero() || (allowedDenominations != null && !allowedDenominations.isAllowed(currencyUnit))) {
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
        if (getShortFall().isZero()) {
            cashHandler.deposit(insertedCoins);
            Collection<CurrencyUnit> change;

            try {
                change = insertedCoinsSum.greaterOrEqualThan(shelf.getProductPrice())
                    ? cashHandler.withdraw(insertedCoinsSum.subtract(shelf.getProductPrice()))
                    : new ArrayList<>();
            } catch (Exception e) {
                cashHandler.withdraw(insertedCoinsSum);
                throw e;
            }

            try {
                return new BasicPurchaseResult(shelf.withdraw() ,change);
            } catch (Exception e) {
                cashHandler.deposit(change);
                throw e;
            }
        }

        throw new IllegalStateException("Do not have enough money to purchase a product");
    }

    @Override
    public CurrencyUnit getProductPrice() {
        return shelf.getProductPrice();
    }
}
