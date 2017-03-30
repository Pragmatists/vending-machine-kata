package tdd.vendingMachine;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tdd.vendingMachine.domain.Denomination;
import tdd.vendingMachine.listener.VendingActionListener;

public class VendingMachine {

    private final static Logger log = LoggerFactory.getLogger(VendingMachine.class);

    private VendingActionListener listener;

    public void insertCoin(Denomination denomination, Integer quantity) {
        if (listener != null) {
            log.debug("onCoinInsertion event invoked.");
            listener.onCoinInsertion(denomination, quantity);
            return;
        }
        log.error("onCoinInsertion event failed. Action listener is not set.");
    }

    public void selectShelf(Integer shelfNumber) {
        if (listener != null) {
            log.debug("onShelfSelected event invoked.");
            listener.onShelfSelected(shelfNumber);
            return;
        }
        log.error("onShelfSelected event failed. Action listener is not set.");
    }

    public void cancelTransaction() {
        if (listener != null) {
            log.debug("onCancelButtonClicked event invoked.");
            listener.onCancelButtonClicked();
            return;
        }
        log.error("cancelTransaction event failed. Action listener is not set.");
    }

    public void addListener(VendingActionListener listener) {
        this.listener = listener;
    }

}
