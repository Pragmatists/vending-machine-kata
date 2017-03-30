package tdd.vendingMachine.listener;

import tdd.vendingMachine.domain.Denomination;

/**
 * @author kdkz
 */
public interface VendingActionListener {

    void onCoinInsertion(Denomination denomination, Integer quantity);

    void onShelfSelected(int shelfNumber);

    void onCancelButtonClicked();
}
