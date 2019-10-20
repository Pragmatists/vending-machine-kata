package tdd.vendingMachine;


import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.shelf.CannotChangeShelfProductsTypeException;


public class MachineConfiguration {
    private VendingMachine vendingMachine;
    private Product product;
    private int shelfNumber;
    private double[] coinsInCashBox = {};
    private int selectedProductShelf;
    private double[] coinsInCurrentRequestPocket = {};

    public MachineConfiguration(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    public static MachineConfiguration aMachine(VendingMachine vendingMachine) {
        return new MachineConfiguration(vendingMachine);
    }


    public MachineConfiguration withProduct(Product product) {
        this.product = product;
        return this;
    }

    public MachineConfiguration onShelf(int shelfNumber) {
        this.shelfNumber = shelfNumber;
        return this;
    }

    public MachineConfiguration withCashBoxCoins(double... coinsValue) {
        this.coinsInCashBox = coinsValue;
        return this;
    }

    public MachineConfiguration withCurrentRequestCoins(double... coinsValue) {
        this.coinsInCurrentRequestPocket = coinsValue;
        return this;
    }

    public void configure() throws CannotChangeShelfProductsTypeException {
        vendingMachine.turnOnMachineSetUpState();
        vendingMachine.insertProduct(shelfNumber, product);
        for (double coinValue : coinsInCashBox) {
            vendingMachine.insertCoinToCashBox(new Coin(coinValue));
        }
        vendingMachine.turnOfMachineSetUpState();
        vendingMachine.selectProduct(selectedProductShelf);
        for (double coinValue : coinsInCurrentRequestPocket) {
            vendingMachine.insertCoinForCurrentRequest(new Coin(coinValue));
        }
    }

    public MachineConfiguration withSelectedProduct(int selectedProductShelf) {
        this.selectedProductShelf = selectedProductShelf;
        return this;
    }
}
