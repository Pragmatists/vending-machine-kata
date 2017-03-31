package tdd.vendingMachine.listener;

import tdd.vendingMachine.domain.Denomination;
import tdd.vendingMachine.exception.MoneyChangeException;
import tdd.vendingMachine.exception.ShelfNotExistException;

import java.math.BigDecimal;

/**
 * @author kdkz
 */
public interface VendingMachineObserver {

    void coinInserted(Denomination denomination, Integer integer);

    void shelfSelected(int shelfNumber) throws ShelfNotExistException;

    void sufficientValueInserted(BigDecimal amountToPay) throws MoneyChangeException;

    void cancelButtonSelected();

}
