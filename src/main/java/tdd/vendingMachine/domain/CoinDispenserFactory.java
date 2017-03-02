package tdd.vendingMachine.domain;

import lombok.NonNull;
import org.apache.log4j.Logger;
import tdd.vendingMachine.dto.CashImport;

import java.util.*;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 *
 * Factory class to create a coin shelf for the vending machine given imports
 */
public class CoinDispenserFactory {

    private static final Logger logger = Logger.getLogger(CoinDispenserFactory.class);

    private static final VendingMachineConfiguration vendingMachineConfiguration = new VendingMachineConfiguration();

    private CoinDispenserFactory() { throw new AssertionError("CoinDispenserFactory should not be instantiated"); }

    static VendingMachineConfiguration getConfig() {
        return vendingMachineConfiguration;
    }

    private static void validCashImport(CashImport cashImport) {
        if (cashImport.getAmount() < 0) {
            throw new InputMismatchException("Invalid value as cash import");
        }
    }

    /**
     * Loads the list of imports to the Builder
     * @param cashImports the collection of imports that want to get loaded
     */
    private static Map<Coin, CashImport> provideCoinMapImport(Collection<CashImport> cashImports) {
        final Map<Coin, CashImport> importsMap = new HashMap<>();
        cashImports.forEach(cashImport -> {
            validCashImport(cashImport);
            Coin key = Coin.retrieveCoinByLabel(cashImport.getLabel());
            if (!importsMap.containsKey(key)) {
                importsMap.put(key, cashImport);
            } else {
                //if new import for the same coin accumulate
                importsMap.put(key, importsMap.get(key).accumulate(cashImport));
            }
        });
        return importsMap;
    }

    private static Map<Coin, Shelf<Coin>> buildShelfWithGivenCoinItemCount(int shelfCapacity, int coinItemCount) {
        Map<Coin, Shelf<Coin>> cashDispenser = new HashMap<>();
        int counter = 0;
        for(Coin coin: Coin.ascendingDenominationIterable()) {
            cashDispenser.put(coin, ShelfFactory.buildShelf(counter++, coin, shelfCapacity, coinItemCount));
        }
        return cashDispenser;
    }

    /**
     * Builds a cash dispenser as a map of shelves based on the cash imports loaded.
     * If the amount in a cash import exceeds the shelf's capacity the shelf will get full and
     * the remaining amount will be discarded back.
     * @param cashImportsCollection the collection of imports that want to get loaded
     * @return a coin dispenser with given amounts if capacity exceeded excess will be discarded
     */
    public static Map<Coin, Shelf<Coin>> buildShelf(@NonNull VendingMachineConfiguration config, final @NonNull Collection<CashImport> cashImportsCollection) {
        Map<Coin, CashImport> cashImports = provideCoinMapImport(cashImportsCollection);
        Map<Coin, Shelf<Coin>> cashDispenser = new HashMap<>();
        int counter = 0;
        int configCapacity = config.getCoinShelfCapacity();
        for(Coin c: Coin.ascendingDenominationIterable()) {
            cashDispenser.put(c, ShelfFactory.buildShelf(counter++, c, configCapacity, 0));
            logger.info(String.format("building coin shelf [%s]", c.label));
        }
        cashImports.forEach((coin, cashImport) -> {
            Shelf<Coin> coinShelf = cashDispenser.get(coin);
            int freeSlots = coinShelf.countFreeSlots();
            if (cashImport.getAmount() <= freeSlots) {
                coinShelf.provision(cashImport.getAmount());
                logger.info(String.format("loaded [%d] coins of [%s] to the shelf", cashImport.getAmount(), coinShelf.getType().label));
            } else {
                logger.warn(String.format("discarded %d items for cashImport: %s shelf is full", (cashImport.getAmount() - freeSlots), cashImport.getLabel()) );
                coinShelf.provision(freeSlots);
            }
        });
        return cashDispenser;
    }

    /**
     * Builds a cash dispenser as a map of shelves based on the cash imports loaded.
     * If the amount in a cash import exceeds the shelf's capacity the shelf will get full and
     * the remaining amount will be discarded back.
     * @param givenCashImport the import that want to get loaded
     * @return a coin dispenser with given amounts if capacity exceeded excess will be discarded
     */
    public static Map<Coin, Shelf<Coin>> buildShelf(@NonNull CashImport givenCashImport) {
        return buildShelf(getConfig(), Collections.singleton(givenCashImport));
    }

    /**
     * Builds a cash dispenser as a map of shelves based on the cash imports loaded.
     * If the amount in a cash import exceeds the shelf's capacity the shelf will get full and
     * the remaining amount will be discarded back.
     * @param cashImports the import that want to get loaded
     * @return a coin dispenser with given amounts if capacity exceeded excess will be discarded
     */
    public static Map<Coin, Shelf<Coin>> buildShelf(@NonNull Collection<CashImport> cashImports) {
        return buildShelf(getConfig(), cashImports);
    }

    /**
     * Builds a coin shelf with the given item count per coin shelf
     * @param coinItemCount the desired count must be >= 0 and <= maxCapacity
     * @return a coin dispenser for the vending machine with coinItemCount items per coin shelf
     */
    public static Map<Coin, Shelf<Coin>> buildShelfWithGivenCoinItemCount(int coinItemCount) {
        return buildShelfWithGivenCoinItemCount(getConfig().getCoinShelfCapacity(), coinItemCount);
    }

    public static Map<Coin, Shelf<Coin>> buildShelfWithGivenCoinItemCount(@NonNull VendingMachineConfiguration config, int coinItemCount) {
        return buildShelfWithGivenCoinItemCount(config.getCoinShelfCapacity(), coinItemCount);
    }
}
