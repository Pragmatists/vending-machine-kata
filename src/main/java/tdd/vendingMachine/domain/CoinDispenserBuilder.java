package tdd.vendingMachine.domain;

import org.apache.log4j.Logger;
import tdd.vendingMachine.dto.CashImport;

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
    private final int coinShelfCapacity;
    private final VendingMachineConfiguration vendingMachineConfiguration;
    private Map<Coin, CashImport> cashImports;

    public CoinDispenserBuilder(VendingMachineConfiguration vendingMachineConfiguration) {
        this.maxShelfAmount = Coin.values().length;
        this.coinShelfCapacity = vendingMachineConfiguration.getCoinShelfCapacity();
        this.cashImports = new HashMap<>();
        this.vendingMachineConfiguration = vendingMachineConfiguration;
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
     * @return a map of key Coin and value a Shelf of that Coin
     */
    public Map<Coin, Shelf<Coin>> buildShelf() {
        Map<Coin, Shelf<Coin>> cashDispenser = new HashMap<>();
        int counter = 0;
        for(Coin c: Coin.ascendingDenominationIterable()) {
            cashDispenser.put(c, new Shelf<>(counter++, c, coinShelfCapacity, 0));
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

    private Map<Coin, Shelf<Coin>> buildShelfWithGivenCoinItemCount(int shelfCapacity, int coinItemCount) {
        Map<Coin, Shelf<Coin>> cashDispenser = new HashMap<>();
        int counter = 0;
        for(Coin coin: Coin.ascendingDenominationIterable()) {
            cashDispenser.put(coin, ShelfFactory.buildShelf(counter++, coin, shelfCapacity, coinItemCount));
        }
        return cashDispenser;
    }

    /**
     * Builds a coin shelf with the given item count per coin shelf
     * @param coinItemCount the desired count must be >= 0 and <= maxCapacity
     * @return a coin dispenser for the vending machine with coinItemCount items per coin shelf
     */
    public Map<Coin, Shelf<Coin>> buildShelfWithGivenCoinItemCount(int coinItemCount) {
        return buildShelfWithGivenCoinItemCount(vendingMachineConfiguration.getCoinShelfCapacity(), coinItemCount);
    }

}
