package tdd.vendingMachine.display;


import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.shelf.IShelf;

public interface IDisplay {
    void showInvalidCoinFormatMessage();

    void showRemainingValueForSelectedProductMessage(Double coinsValue);

    void showInvalidShelfNumberMessage();

    void showInsertProductInformationMessage(Integer shelfNumber, Product product);

    void showIncorrectProductSelectMessage();

    void showProductSelectedMessage(Product selectedProduct);

    void showReturnCoinMessage(Coin coin);

    void showInvalidActionForMachineStateMessage();

    void showCoinAddedToCashBoxMessage(Coin coin);

    void showFirstSelectProductMessage();

    void showRequestCanceledMessage();

    void showCantReturnChangeMessage();

    void showShelfInformation(IShelf shelf);

    void showDropProductMessage(Product product);
}
