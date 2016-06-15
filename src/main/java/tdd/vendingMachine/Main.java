package tdd.vendingMachine;

import tdd.vendingMachine.core.CurrencyUnit;
import tdd.vendingMachine.core.ProductName;
import tdd.vendingMachine.core.ProductPrice;
import tdd.vendingMachine.impl.AllowedDenominations;
import tdd.vendingMachine.impl.BasicShelf;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Random random = new Random();

        AllowedDenominations allowedDenominations = new AllowedDenominations()
            .add(CurrencyUnit.valueOf("0.5"))
            .add(CurrencyUnit.valueOf("5"))
            .add(CurrencyUnit.valueOf("0.1"))
            .add(CurrencyUnit.valueOf("2"))
            .add(CurrencyUnit.valueOf("0.2"))
            .add(CurrencyUnit.valueOf("1"));

        new VendingMachine(allowedDenominations)
            .addShelf(new BasicShelf(ProductName.valueOf("Water"), ProductPrice.valueOf("1.5")).charge(random.nextInt(10) + 1))
            .addShelf(new BasicShelf(ProductName.valueOf("Tropic Jiuce"), ProductPrice.valueOf("2.9")).charge(random.nextInt(10) + 1))
            .addShelf(new BasicShelf(ProductName.valueOf("Orange Jiuce"), ProductPrice.valueOf("4.8")).charge(random.nextInt(10) + 1))
            .addShelf(new BasicShelf(ProductName.valueOf("Mango Jiuce"), ProductPrice.valueOf("6")).charge(random.nextInt(10) + 1))
            .addShelf(new BasicShelf(ProductName.valueOf("Chocolate Bar"), ProductPrice.valueOf("2")).charge(random.nextInt(10) + 1))
            .addShelf(new BasicShelf(ProductName.valueOf("Crackers"), ProductPrice.valueOf("3.5")).charge(random.nextInt(10) + 1))
            .run();
    }
}
