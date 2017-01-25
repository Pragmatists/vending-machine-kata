package tdd.vendingMachine;

/**
 * Created by dzalunin on 2017-01-25.
 */
public interface Display {

    String PRODUCT_PRICE_MSG = "PRODUCT_PRICE";
    String REMAINS_TO_PAY_MSG = "Remains to pay: ";
    String WARNING_MSG = "TO_BE_PAID";
    String WELCOME_MSG = "WELCOME!";

    void show(String msg);

    /**
     * This method exists for test purposes
     * @return
     */
    String getLastMessage();
}
