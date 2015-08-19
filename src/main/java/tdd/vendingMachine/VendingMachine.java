package tdd.vendingMachine;

import com.sun.istack.internal.NotNull;

import java.math.BigDecimal;
import java.util.*;

public class VendingMachine {
    private final Display display;
    private final CoinTray coinTray;
    private Map<Integer, Shelf> shelves;
    private Shelf selectedShelf;


    public VendingMachine(Display display, Keyboard keyboard, CoinTray coinTray) {
        this.coinTray = coinTray;
        this.shelves = new HashMap<Integer, Shelf>();
        this.display = display;

        keyboard.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                selectShelf((Integer) arg);
            }
        });

        coinTray.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if (selectedShelf != null) {
                    updateDisplay();
                } else {
                    ((CoinTray) o).returnCoins();
                }
            }
        });
    }

    private void updateDisplay() {
        BigDecimal moneyLeftToPay = selectedShelf.getPrice().subtract(coinTray.getInsertedAmount());
        display.setContent(String.valueOf(moneyLeftToPay));
    }

    public void selectShelf(int shelfNo) {
        selectedShelf = getShelf(shelfNo);

        final BigDecimal moneyLeftToPay = selectedShelf.getPrice().subtract(coinTray.getInsertedAmount());

        display.setContent(String.valueOf(moneyLeftToPay));
    }

    public void addProductToShelf(int shelfNo, Product product) {
        getShelf(shelfNo).addProduct(product);
    }

    public Product getProductFromShelf(int shelfNo) {
        return getShelf(shelfNo).getProduct();
    }

    @NotNull
    protected Shelf getShelf(int shelfNumber) {
        if (!shelves.containsKey(shelfNumber)) {
            shelves.put(shelfNumber, new Shelf());
        }
        return shelves.get(shelfNumber);
    }

}
