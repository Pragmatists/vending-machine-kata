package tdd.vendingMachine.display;

import tdd.vendingMachine.shelve.Shelve;
import tdd.vendingMachine.errors.ShelveNotFound;

import java.math.BigDecimal;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by okraskat on 06.02.16.
 */
public class DefaultDisplay implements Display {

    private final Map<Integer, Shelve> shelves;

    public DefaultDisplay(Map<Integer, Shelve> shelves) {
        checkNotNull(shelves, "Shelves can not be null");
        this.shelves = shelves;
    }

    @Override
    public BigDecimal getProductPriceByShelveNumber(Integer shelveNumber) {
        checkNotNull(shelveNumber, "Shelve umber can not be null");
        Shelve shelve = shelves.get(shelveNumber);
        if(shelve == null){
            throw new ShelveNotFound(shelveNumber);
        }
        BigDecimal price = shelve.getProductPrice();
        System.out.println("Price for product in shelve " + shelveNumber + " is " + price.toString());
        return price;
    }
}
