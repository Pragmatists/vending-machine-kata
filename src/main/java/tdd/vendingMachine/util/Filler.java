    package tdd.vendingMachine.util;

import tdd.vendingMachine.domain.VendingMachine;
import tdd.vendingMachine.domain.money.Coins;
import tdd.vendingMachine.domain.money.MoneyBox;
import tdd.vendingMachine.domain.product.ProductBox;
import tdd.vendingMachine.domain.product.Products;
import tdd.vendingMachine.domain.product.Tray;
import tdd.vendingMachine.domain.state.States;

public class Filler {

    public static void fill(VendingMachine machine, final int count) {
        if (States.BASE != machine.getState()) {
            throw new IllegalArgumentException("Machine has to be idle before filling");
        }

        machine
            .setProductBox(getPreFilledProductBox(count))
            .setMoneyBox(getPreFilledMoneyBox(count));

    }

    public static ProductBox getPreFilledProductBox(final int count) {
        ProductBox box = new ProductBox();
        box
            .addTray(new Tray(Products.COCA_COLA_0_33, count))
            .addTray(new Tray(Products.COCA_COLA_0_5, count))
            .addTray(new Tray(Products.CHOCOLATE_BAR, count))
            .addTray(new Tray(Products.WATER_0_5, count))
            .addTray(new Tray(Products.COCA_COLA_0_33, count));

        return box;
    }

    public static MoneyBox getPreFilledMoneyBox(final int count) {
        MoneyBox box = new MoneyBox();
        box
            .insert(Coins.COIN_0_1, count)
            .insert(Coins.COIN_0_2, count)
            .insert(Coins.COIN_0_5, count)
            .insert(Coins.COIN_1, count)
            .insert(Coins.COIN_2, count)
            .insert(Coins.COIN_5, count);

        return box;
    }
}
