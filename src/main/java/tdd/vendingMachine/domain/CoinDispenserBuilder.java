package tdd.vendingMachine.domain;

import org.apache.log4j.Logger;
import tdd.vendingMachine.dto.CashImport;
import tdd.vendingMachine.util.FileReaderHelper;

import java.util.*;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 *
 * Builder class to create a coin shelf for the vending machine given imports
 */
public class CoinDispenserBuilder {

    private static final Logger logger = Logger.getLogger(CoinDispenserBuilder.class);

    private final int maxShelfAmount;
    private final int shelfCapacity;
    private Map<Coin, CashImport> cashImports;

    public CoinDispenserBuilder(int shelfCapacity) {
        this.maxShelfAmount = Coin.values().length;
        this.shelfCapacity = shelfCapacity;
        this.cashImports = new HashMap<>();
    }

    private void validCashImport(CashImport cashImport) {
        if (cashImport.getAmount() < 0) {
            throw new InputMismatchException("Invalid value as cash import");
        }
    }

    /**
     * Loads the given Cash import
     * @param cashImport the import to be loaded
     * @return the instance of the builder to enable chain operations.
     */
    public CoinDispenserBuilder withCashImport(CashImport cashImport) {
        validCashImport(cashImport);
        Coin key = Coin.retrieveCoinByLabel(cashImport.getLabel());
        if (!cashImports.containsKey(key)) {
            cashImports.put(key, cashImport);
        } else {
            //if new import for the same coin accumulate
            cashImports.put(key, cashImports.get(key).accumulate(cashImport));
        }
        return this;
    }

    /**
     * Loads the list of imports to the Builder
     * @param cashImports the collection of imports that want to get loaded
     * @return the instance of the builder to enable chain operations.
     */
    public CoinDispenserBuilder withCashImport(Collection<CashImport> cashImports) {
        cashImports.forEach(this::withCashImport);
        return this;
    }

    /**
     * Builds a cash dispenser as a map of shelves based on the cash imports loaded.
     * If the amount in a cash import exceeds the shelf's capacity the shelf will get full and
     * the remaining amount will be discarded back.
     * @return
     */
    public Map<Coin, Shelf<Coin>> buildShelf() {
        Map<Coin, Shelf<Coin>> cashDispenser = new HashMap<>();
        int counter = 0;
        for(Coin c: Coin.values()) {
            cashDispenser.put(c, new Shelf<>(counter++, c, shelfCapacity, 0));
        }
        cashImports.forEach((coin, cashImport) -> {
            Shelf<Coin> coinShelf = cashDispenser.get(coin);
            int freeSlots = coinShelf.countFreeSlots();
            if (cashImport.getAmount() <= freeSlots) {
                coinShelf.provision(cashImport.getAmount());
            } else {
                logger.info(String.format("discarded %d items for cashImport: %s shelf is full", (cashImport.getAmount() - freeSlots), cashImport.getLabel()) );
                coinShelf.provision(freeSlots);
            }
        });
        cashImports = new HashMap<>();
        return cashDispenser;
    }

}
