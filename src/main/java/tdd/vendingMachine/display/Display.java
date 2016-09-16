package tdd.vendingMachine.display;

import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.shelf.IShelf;

import java.io.PrintStream;

public class Display implements IDisplay {

    public static final String INVALID_COIN_MESSAGE = "It's invalid coin format. You can put 5.0, 2.0, 1.0, 0.5, 0.2, 0.1";
    public static final String MACHINE_DISPLAY_OUTPUT_PREFIX = "MACHINE DISPLAY INFO: ";
    public static final String REMAINING_VALUE_MESSAGE = "Still need : %d more. Hurry up!";
    public static final String INCORRECT_SHELF_NUMBER_MESSAGE = "Cant find shelf which you specify in this machine!";
    public static final String ADD_PRODUCT_INFORMATION_MESSAGE = "Inserted product at shelf: %s. Product type is: %s with own price: %s";
    public static final String INCORRECT_SHELF_SELECT_MESSAGE = "No way! You chose wrong shelf: %s or shelf is empty. Please look closer and try again!";
    public static final String SELECTED_PRODUCT_MESSAGE = "Congratulations you choose product: %s. Please insert %s if you want to get this product.";
    public static final String RETURN_COIN_MESSAGE = "Returned coin: %s";
    public static final String ACTION_IS_NOT_ALLOWED_ON_THIS_MACHINE_STATE_MESSAGE = "Method is not allowed on this machine state";
    public static final String ADDED_COIN_TO_MACHINE_CASH_BOX_MESSAGE = "You added coin to machine cash box with value: %s";
    public static final String PLEASE_FIRST_SELECT_PRODUCT_MESSAGE = "Please, first select product.";
    public static final String REQUEST_WAS_CANCELED_MESSAGE = "Request was canceled. :(";
    public static final String CANT_RETURN_CHANGE_MESSAGE = "Sorry cant return your change. Not enough coins. Request will be canceled.";
    public static final String SHELF_IS_EMPTY_MESSAGE = "Shelf %s is empty!";
    public static final String SHELF_DETAILS_MESSAGE = "Shelf %s contains product type: %s. Products on shelf: %s";
    public static final String MACHINE_DROP_PRODUCT_MESSAGE = "Machine drop product of type: %s off price: %s.";
    private PrintStream output = System.out;

    public void showInvalidCoinFormatMessage() {
        print(INVALID_COIN_MESSAGE);
    }

    public void showRemainingValueForSelectedProductMessage(Double remainingValue) {
        print(REMAINING_VALUE_MESSAGE.replace("%d", remainingValue.toString()));
    }

    public void showInvalidShelfNumberMessage() {
        print(INCORRECT_SHELF_NUMBER_MESSAGE);
    }

    public void showInsertProductInformationMessage(Integer shelfNumber, Product product) {
        print(String.format(ADD_PRODUCT_INFORMATION_MESSAGE, shelfNumber, product.getType(), product.getPrice()));
    }

    public void showIncorrectProductSelectMessage() {
        print(INCORRECT_SHELF_SELECT_MESSAGE);
    }

    public void showProductSelectedMessage(Product selectedProduct) {
        print(String.format(SELECTED_PRODUCT_MESSAGE, selectedProduct.getType(), selectedProduct.getPrice()));
    }

    public void showReturnCoinMessage(Coin coin) {
        print(String.format(RETURN_COIN_MESSAGE, coin.getValue()));
    }

    public void showInvalidActionForMachineStateMessage() {
        print(ACTION_IS_NOT_ALLOWED_ON_THIS_MACHINE_STATE_MESSAGE);
    }

    public void showCoinAddedToCashBoxMessage(Coin coin) {
        print(String.format(ADDED_COIN_TO_MACHINE_CASH_BOX_MESSAGE, coin.getValue()));
    }

    public void showFirstSelectProductMessage() {
        print(PLEASE_FIRST_SELECT_PRODUCT_MESSAGE);
    }

    public void showRequestCanceledMessage() {
        print(REQUEST_WAS_CANCELED_MESSAGE);
    }

    public void showCantReturnChangeMessage() {
        print(CANT_RETURN_CHANGE_MESSAGE);
    }

    public void showShelfInformation(IShelf shelf) {
        if (!shelf.isEmpty()) {
            print(String.format(SHELF_DETAILS_MESSAGE, shelf.getNumber(), shelf.getProductsType(), shelf.size()));
        } else {
            print(String.format(SHELF_IS_EMPTY_MESSAGE, shelf.getNumber()));
        }
    }

    public void showDropProductMessage(Product product) {
        print(String.format(MACHINE_DROP_PRODUCT_MESSAGE, product.getType(), product.getPrice()));
    }

    void print(String println) {
        output.println(MACHINE_DISPLAY_OUTPUT_PREFIX + println);
    }
}
