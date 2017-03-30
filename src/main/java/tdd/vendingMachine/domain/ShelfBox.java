package tdd.vendingMachine.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tdd.vendingMachine.exception.ShelfNotExistException;

import java.math.BigDecimal;
import java.util.ArrayList;

import static tdd.vendingMachine.domain.Product.CHOCOLATE_BAR;
import static tdd.vendingMachine.domain.Product.COLA;
import static tdd.vendingMachine.domain.Product.MINERAL_WATER;

/**
 * @author kdkz
 */
public class ShelfBox {

    private final static Logger log = LoggerFactory.getLogger(ShelfBox.class);

    private ArrayList<Shelf> shelves = new ArrayList<>();

    public ShelfBox() {
        shelves.add(new Shelf(COLA, 8));
        shelves.add(new Shelf(CHOCOLATE_BAR, 8));
        shelves.add(new Shelf(MINERAL_WATER, 8));
    }

    public BigDecimal selectShelfAndGetPrice(int shelfNumber) throws ShelfNotExistException {
        if (shelfNumber > shelves.size() || shelfNumber < 1) {
            log.error("Shelf with given number doest not exist");
            throw new ShelfNotExistException("Shelf with given number doest not exist");
        }
        log.debug("Shelf {} successfully selected.", shelfNumber);
        return shelves.get(shelfNumber - 1).getProductsOnShelf().getKey().getPrice();
    }

}
