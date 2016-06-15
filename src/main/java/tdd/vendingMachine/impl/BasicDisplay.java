package tdd.vendingMachine.impl;

import tdd.vendingMachine.core.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class BasicDisplay implements Display {

    private final Observer observer;
    private final Input input;
    private Transaction transaction = null;

    public BasicDisplay(Observer observer, Input input) {
        if (observer == null || input == null) {
            throw new IllegalArgumentException("Display should be created with valid observer and input source");
        }

        this.observer = observer;
        this.input = input;
    }

    @Override
    public void run(List<Shelf> shelves) {
        int shelfIndex = 0;
        System.out.println("Available shelves: ");

        for (Shelf shelf : shelves) {
            System.out.println(++shelfIndex + ". " + shelf.getProductName() + " (" + shelf.getProductPrice() + ')');
        }

        selectShelf();
    }

    @Override
    public void displayProductPrice(CurrencyUnit productPrice) {
        System.out.println("Selected product price: " + productPrice.value());
        receiveCoins();
    }

    private void selectShelf() {
        while (transaction == null) {
            try {
                System.out.print("Select shelve (-1 terminate): ");
                int index = input.readInt();

                if (index >= 0) {
                    transaction = observer.shelfHasBeenSelected(index - 1);
                } else {
                    return;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println();
        displayProductPrice(transaction.getProductPrice());
    }

    private void receiveCoins() {
        boolean terminate = false;

        while (!terminate) {
            System.out.print("Insert coin (0 - buy product, -1 - cancel): ");

            try {
                CurrencyUnit currencyUnit = CurrencyUnit.valueOf(input.readString());

                if (currencyUnit.isNegative()) {
                    System.out.println("Byuing process has been cancelled");
                    rollbackTransaction();
                    terminate = true;
                } else if (currencyUnit.isZero()) {
                    commitTransaction();
                    terminate = true;
                } else {
                    System.out.println("Money left: " + transaction.insertCoin(currencyUnit).getShortFall());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        selectShelf();
    }

    private void rollbackTransaction() {
        try {
            Collection<CurrencyUnit> rollback = transaction.rollback();

            if (!rollback.isEmpty()) {
                System.out.print("Money returned: ");
                displayListOfCurrencyUnits(rollback);
            }
        } finally {
            transaction = null;
        }
    }

    private void commitTransaction() {
        try {
            PurchaseResult result = transaction.commit();

            System.out.println("You have bought product '" + result.getProduct().getName().value() +
                "' which costs " + result.getProduct().getPrice().value());

            System.out.println("Your change is: ");
            displayListOfCurrencyUnits(result.getChange());
        } catch (IllegalStateException e) {
            System.out.println("Product buying has been cancelled, reason: " + e.getMessage());
            rollbackTransaction();
        } finally {
            transaction = null;
        }
    }

    private void displayListOfCurrencyUnits(Collection<CurrencyUnit> currencyUnits) {
        if (!currencyUnits.isEmpty()) {
            Iterator<CurrencyUnit> iterator = currencyUnits.iterator();
            System.out.print(iterator.next().value());

            while (iterator.hasNext()) {
                System.out.print(", " + iterator.next().value());
            }

            System.out.println();
        } else {
            System.out.println(CurrencyUnit.valueOf("0"));
        }
    }
}
