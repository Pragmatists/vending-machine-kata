package tdd.vendingMachine.listener;

import tdd.vendingMachine.domain.Denomination;
import tdd.vendingMachine.exception.MoneyChangeException;
import tdd.vendingMachine.exception.ShelfNotExistException;

import java.math.BigDecimal;

/**
 * Listener interface for listening Vending Machine actions and provide the response for them.
 *
 * @author kdkz
 */
public interface VendingMachineObserver {

    /**
     * Coin inserted Event
     *
     * @param denomination inserted denomination
     * @param integer      quantity inserted - For ease of testing. In real world coins are inserted singly.
     */
    void coinInserted(Denomination denomination, Integer integer);

    /**
     * Shelf selected Event
     *
     * @param shelfNumber selected shelf number
     * @throws ShelfNotExistException
     */
    void shelfSelected(int shelfNumber) throws ShelfNotExistException;

    /**
     * Sufficient amount inserted Event is called when inserted amount is equals or greater than product price
     *
     * @param amountToPay product price
     * @throws MoneyChangeException
     */
    void sufficientAmountInserted(BigDecimal amountToPay) throws MoneyChangeException;

    /**
     * Cancel button clicked Event
     */
    void cancelButtonSelected();

}
