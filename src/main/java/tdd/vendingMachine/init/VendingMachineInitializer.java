package tdd.vendingMachine.init;

import lombok.Getter;
import tdd.vendingMachine.IVendingMachine;
import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.product.ProductType;
import tdd.vendingMachine.shelf.CannotChangeShelfProductsTypeException;

import java.util.Arrays;
import java.util.List;

public class VendingMachineInitializer {
    public static final int EACH_COIN_COUNT = 2;
    List<ProductConfiguration> productConfigurations = Arrays.asList(
        new ProductConfiguration(1, ProductType.CHIPS),
        new ProductConfiguration(1, ProductType.CHIPS),
        new ProductConfiguration(2, ProductType.COLA),
        new ProductConfiguration(3, ProductType.COLA),
        new ProductConfiguration(2, ProductType.CHIPS),
        new ProductConfiguration(5, ProductType.CHOCOLATE_BAR),
        new ProductConfiguration(6, ProductType.CHIPS),
        new ProductConfiguration(7, ProductType.CHIPS),
        new ProductConfiguration(8, ProductType.COLA),
        new ProductConfiguration(8, ProductType.COLA),
        new ProductConfiguration(10, ProductType.CHOCOLATE_BAR)
    );

    List<Double> coinsDenominations = Arrays.asList(
        5.0,
        2.0,
        1.0,
        0.5,
        0.2,
        0.1
    );

    public void init(IVendingMachine vendingMachine) {
        vendingMachine.turnOnMachineSetUpState();
        initProducts(vendingMachine);
        insertCoins(vendingMachine);
        vendingMachine.turnOfMachineSetUpState();
    }

    private void insertCoins(IVendingMachine vendingMachine) {
        for (Double coinsDenomination : coinsDenominations) {
            for (int i = 0; i < EACH_COIN_COUNT; i++) {
                vendingMachine.insertCoinToCashBox(new Coin(coinsDenomination));
            }
        }
    }

    private void initProducts(IVendingMachine vendingMachine) {
        for (ProductConfiguration productConfiguration : productConfigurations) {
            try {
                vendingMachine.insertProduct(productConfiguration.getShelfNumber(), productConfiguration.getProduct());
            } catch (CannotChangeShelfProductsTypeException e) {
                System.err.println("Cannot insert product: " + productConfiguration.getProduct() + " to Shelf: " + productConfiguration.getShelfNumber());
            }
        }
    }


    class ProductConfiguration {
        @Getter
        private final Integer shelfNumber;
        @Getter
        private final Product product;

        public ProductConfiguration(Integer shelfNumber, ProductType productType) {
            this.shelfNumber = shelfNumber;
            this.product = new Product(productType);
        }
    }
}
