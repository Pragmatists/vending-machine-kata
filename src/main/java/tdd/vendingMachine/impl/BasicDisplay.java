package tdd.vendingMachine.impl;

import tdd.vendingMachine.core.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class BasicDisplay implements Display {

    private final Observer observer;
    private final Scanner scanner = new Scanner(System.in);
    private Transaction transaction;


    public BasicDisplay(Observer observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Display should be created with valid observer");
        }

        this.observer = observer;
    }

    @Override
    public void displayShelves(List<Shelf> shelves) {
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
        boolean shelfSelected = false;

        while (!shelfSelected) {
            try {
                System.out.print("Select shelve: ");
                transaction = observer.shelfHasBeenSelected(scanner.nextInt() - 1);
                shelfSelected = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        displayProductPrice(transaction.getProductPrice());
    }

    private void receiveCoins() {
        boolean terminate = false;

        while (!terminate) {
            System.out.print("Insert coin (0 - buy product, -1 - cancel): ");

            try {
                CurrencyUnit currencyUnit = CurrencyUnit.valueOf(scanner.next());

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
        Collection<CurrencyUnit> rollback = transaction.rollback();

        if (!rollback.isEmpty()) {
            System.out.print("Money returned: ");
            displayListOfCurrencyUnits(rollback);
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
