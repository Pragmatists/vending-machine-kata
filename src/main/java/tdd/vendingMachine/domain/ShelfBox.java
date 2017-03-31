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
 * @author kdkz
 */
public class ShelfBox implements VendingMachineObserver{

    private final static Logger log = LoggerFactory.getLogger(ShelfBox.class);

    private ArrayList<Shelf> shelves = new ArrayList<>();

    private VendingMachineNotifier vendingMachineNotifier;

    public ShelfBox(VendingMachineNotifier vendingMachineNotifier) {
        this.vendingMachineNotifier = vendingMachineNotifier;
        initializeShelfBox();
    }

    private BigDecimal selectShelfAndGetPrice(int shelfNumber) throws ShelfNotExistException {
        if (shelfNumber > shelves.size() || shelfNumber < 1) {
            log.error("Shelf with given number doest not exist");
            throw new ShelfNotExistException("Shelf with given number doest not exist");
        }
        log.debug("Shelf {} successfully selected.", shelfNumber);
        return shelves.get(shelfNumber - 1).getProductsOnShelf().getKey().getPrice();
    }

    private void initializeShelfBox() {
        shelves.add(new Shelf(COLA, 8));
        shelves.add(new Shelf(CHOCOLATE_BAR, 8));
        shelves.add(new Shelf(MINERAL_WATER, 8));
    }

    @Override
    public void coinInserted(Denomination denomination, Integer integer) {
        //not implemented
    }

    @Override
    public void shelfSelected(int shelfNumber) throws ShelfNotExistException {
        BigDecimal productPrice = selectShelfAndGetPrice(shelfNumber);
        vendingMachineNotifier.notifyProductPrice(shelfNumber, productPrice);
    }

    @Override
    public void sufficientValueInserted(BigDecimal amountToPay){
        //not implemented yet. in rear application it could subtract
        //sold product from current product quantity on shelf
    }

    @Override
    public void cancelButtonSelected() {
        //not implemented
    }
}
