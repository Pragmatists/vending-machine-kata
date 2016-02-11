package tdd.vendingMachine;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import tdd.vendingMachine.display.Display;
import tdd.vendingMachine.display.DisplayFactory;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.shelve.Shelve;

import java.math.BigDecimal;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class VendingMachine {

    private static Set<BigDecimal> ACCEPTED_DENOMINATIONS = Sets.newTreeSet(
        Lists.newArrayList(
        new BigDecimal("5"),
        new BigDecimal("2"),
        new BigDecimal("1"),
        new BigDecimal("0.5"),
        new BigDecimal("0.2"),
        new BigDecimal("0.1")));

    private Map<BigDecimal, List<BigDecimal>> coins;

    private final Map<Integer, Shelve> shelves;

    private final Display display;

    private List<BigDecimal> insertedCoins = new ArrayList<>();

    private Shelve selectedShelve;

    private final ChangeCalculator changeCalculator;

    public VendingMachine(List<Shelve> shelvesList, DisplayFactory displayFactory, Function<? super Shelve, Integer> keyMapper, ChangeCalculator changeCalculator) {
        checkNotNull(changeCalculator, "ChangeCalculator can not be null");
        this.changeCalculator = changeCalculator;
        checkNotNull(displayFactory, "DisplayFactory can not be null");
        shelves = Maps.uniqueIndex(shelvesList, keyMapper);
        this.display = displayFactory.createDisplay();
        coins = new HashMap<>();
        for (BigDecimal acceptedDenomination : ACCEPTED_DENOMINATIONS) {
            coins.put(acceptedDenomination, new ArrayList<>());
        }
    }

    public Map<Integer, Shelve> getShelves() {
        return shelves;
    }

    public Display getDisplay() {
        return display;
    }

    public void selectShelve(Integer shelveNumber) {
        Shelve shelve = shelves.get(shelveNumber);
        if(shelve == null){
            display.showWarning("Shelve with number " + shelveNumber + " does not exist.");
        } else if (shelve.getProducts().isEmpty()){
            display.showEmptyShelve();
        } else {
            selectedShelve = shelves.get(shelveNumber);
            display.showProductPrice(selectedShelve.getProductPrice());
        }
    }

    public VendingMachineReturnItems insertCoin(BigDecimal inputCoin) {
        Product product = null;
        List<BigDecimal> change = null;
        if(inputCoinValid(inputCoin) && selectionIsValid()){
            insertedCoins.add(inputCoin);
            BigDecimal sumInsertedCoins = sumInsertedCoins();
            if(insertedEnoughMoney(sumInsertedCoins)){
                BigDecimal subtract = sumInsertedCoins.subtract(selectedShelve.getProductPrice());
                if(changeCalculator.canGiveAChange(subtract, coins)) {
                    product = (Product) selectedShelve.getProducts().remove(selectedShelve.getProducts().size() - 1);
                    change = changeCalculator.getChange(subtract, coins);
                    restartMachineState();
                } else {
                    display.showChangeWarning();
                    change = Lists.newArrayList(insertedCoins);
                    insertedCoins.clear();
                }
            } else {
                display.showCoverAmount(selectedShelve.getProductPrice().subtract(sumInsertedCoins));
            }
        } else if(selectedShelve == null) {
            display.showProductNotSelected();
        } else {
            insertedCoins.add(inputCoin);
            change = Lists.newArrayList(insertedCoins);
            insertedCoins.clear();
        }
        return new VendingMachineReturnItems(change, product);
    }

    private void restartMachineState() {
        addCoinsToMachine();
        insertedCoins.clear();
        selectedShelve = null;
    }

    private boolean insertedEnoughMoney(BigDecimal sumInsertedCoins) {
        return sumInsertedCoins.compareTo(selectedShelve.getProductPrice()) >= 0;
    }

    private boolean selectionIsValid() {
        return selectedShelve != null && selectedShelve.getProductPrice() != null;
    }

    public VendingMachineReturnItems cancel(){
        List<BigDecimal> change = Lists.newArrayList(insertedCoins);
        insertedCoins.clear();
        selectedShelve = null;
        return new VendingMachineReturnItems(change, null);
    }

    private void addCoinsToMachine() {
        for (BigDecimal insertedCoin : insertedCoins) {
            coins.keySet().stream().filter(key -> key.compareTo(insertedCoin) == 0).forEach(key -> coins.get(key).add(insertedCoin));
        }
    }

    private BigDecimal sumInsertedCoins() {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal insertedCoin : insertedCoins) {
            sum = sum.add(insertedCoin);
        }
        return sum;
    }

    private boolean inputCoinValid(BigDecimal inputCoin) {
        return ACCEPTED_DENOMINATIONS.contains(inputCoin);
    }

    public Map<BigDecimal, List<BigDecimal>> getCoins() {
        return coins;
    }
}
