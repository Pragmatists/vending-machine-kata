package tdd.vendingMachine;

import com.sun.istack.internal.NotNull;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.*;

public class VendingMachine {
    private final Display display;
    private final CoinTray coinTray;
    private final CoinVault coinVault;
    private Map<Integer, Shelf> shelves;
    private Shelf selectedShelf;

    @Mock
    private GivingTray givingTray;

    public VendingMachine(Display display, Keyboard keyboard, CoinTray coinTray, GivingTray givingTray, CoinVault coinVault) {
        this.coinTray = coinTray;
        this.shelves = new HashMap<Integer, Shelf>();
        this.display = display;
        this.givingTray = givingTray;
        this.coinVault = coinVault;

        keyboard.addObserver(new KeyboardObserver());
        coinTray.addObserver(new CoinTrayObserver());
    }

    private void updateDisplay(String displayContent) {
        display.setContent(displayContent == null ? "" : displayContent);
    }

    public void selectShelfAndUpdateDisplay(int shelfNo) {
        selectedShelf = getShelf(shelfNo);

        final BigDecimal moneyLeftToPay = selectedShelf.getPrice().subtract(coinTray.getInsertedAmount());

        updateDisplay(String.valueOf(moneyLeftToPay));
    }

    public void addProductToShelf(int shelfNo, Product product) {
        getShelf(shelfNo).addProduct(product);
    }

    @NotNull
    protected Shelf getShelf(int shelfNumber) {
        if (!shelves.containsKey(shelfNumber)) {
            shelves.put(shelfNumber, new Shelf());
        }
        return shelves.get(shelfNumber);
    }

    private void giveChange(BigDecimal changeAmount) {
        List<Coin> coins = coinVault.getCoinsToChange(changeAmount);
        coinTray.giveChange(coins);
    }

    private class CoinTrayObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            if (selectedShelf != null) {
                BigDecimal moneyLeftToPay = selectedShelf.getPrice().subtract(coinTray.getInsertedAmount());

                if (moneyLeftToPay.signum() > 0) {
                    updateDisplay(moneyLeftToPay.toString());
                } else {
                    coinVault.add(coinTray.takeAllInsertedCoins());
                    givingTray.giveProduct(selectedShelf.getProduct());
                    updateDisplay(null);
                    if (moneyLeftToPay.signum() < 0) {
                        giveChange(moneyLeftToPay.negate());
                    }
                    selectedShelf = null;
                }

            } else {
                ((CoinTray) o).returnInsertedCoins();
            }
        }
    }


    private class KeyboardObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            selectShelfAndUpdateDisplay((Integer) arg);
        }
    }

}
