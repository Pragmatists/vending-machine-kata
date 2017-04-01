package tdd.vendingMachine.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tdd.vendingMachine.exception.ShelfNotExistException;
import tdd.vendingMachine.listener.VendingMachineNotifier;
import tdd.vendingMachine.listener.VendingMachineObserver;

import java.math.BigDecimal;
import java.util.ArrayList;

import static tdd.vendingMachine.domain.Product.CHOCOLATE_BAR;
import static tdd.vendingMachine.domain.Product.COLA;
import static tdd.vendingMachine.domain.Product.MINERAL_WATER;

/**
 * Class represents a box where all shelves of Vending Machine are placed.
 *
 * @author kdkz
 */
public class ShelfBox implements VendingMachineObserver {

    private final static Logger log = LoggerFactory.getLogger(ShelfBox.class);

    private ArrayList<Shelf> shelves = new ArrayList<>();

    private int selectedShelf;

    private VendingMachineNotifier vendingMachineNotifier;

    public ShelfBox(VendingMachineNotifier vendingMachineNotifier) {
        this.vendingMachineNotifier = vendingMachineNotifier;
    }

    /**
     * @see VendingMachineObserver#coinInserted(Denomination, Integer)
     */
    @Override
    public void coinInserted(Denomination denomination, Integer integer) {
        //not implemented
    }

    /**
     * @see VendingMachineObserver#shelfSelected(int)
     */
    @Override
    public void shelfSelected(int shelfNumber) throws ShelfNotExistException {
        BigDecimal productPrice = selectShelfAndGetPrice(shelfNumber);
        vendingMachineNotifier.notifyProductPrice(shelfNumber, productPrice);
    }

    /**
     * @see VendingMachineObserver#sufficientAmountInserted(BigDecimal)
     */
    @Override
    public void sufficientAmountInserted(BigDecimal amountToPay) {
        shelves.get(selectedShelf - 1).takeOffProduct();
        deselectShelf();
    }

    /**
     * @see VendingMachineObserver#cancelButtonSelected()
     */
    @Override
    public void cancelButtonSelected() {
        deselectShelf();
    }

    /**
     * Selects shelf number and returns product price. Selecting shelf is required before coins are inserted.
     *
     * @param shelfNumber shelf number
     * @return product price
     * @throws ShelfNotExistException
     */
    private BigDecimal selectShelfAndGetPrice(int shelfNumber) throws ShelfNotExistException {
        if (shelfNumber > shelves.size() || shelfNumber < 1) {
            log.error("Shelf with given number doest not exist");
            throw new ShelfNotExistException("Shelf with given number doest not exist");
        }
        selectedShelf = shelfNumber;
        log.debug("Shelf {} successfully selected.", shelfNumber);
        return shelves.get(shelfNumber - 1).getProductOnShelf().getPrice();
    }

    /**
     * Util method for filling shelf box with Shelves with Products
     */
    public void addShelfToShelfBox(Shelf shelf) {
        shelves.add(shelf);
    }

    /**
     * Deselects shelf when transaction is completed or cancel button is clicked.
     */
    private void deselectShelf() {
        selectedShelf = 0;
    }
}
