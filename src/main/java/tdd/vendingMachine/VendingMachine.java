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

    public VendingMachine(List<Shelve> shelvesList, DisplayFactory displayFactory, Function<? super Shelve, Integer> keyMapper) {
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
        if(inputCoinValid(inputCoin) && selectedShelve != null && selectedShelve.getProductPrice() != null){
            insertedCoins.add(inputCoin);
            BigDecimal sumInsertedCoins = sumInsertedCoins();
            if(sumInsertedCoins.compareTo(selectedShelve.getProductPrice()) >= 0){
                BigDecimal subtract = sumInsertedCoins.subtract(selectedShelve.getProductPrice());
                if(canGiveAChange(subtract)) {
                    product = (Product) selectedShelve.getProducts().remove(selectedShelve.getProducts().size() - 1);
                    change = getChange(subtract);
                    addCoinsToMachine();
                    insertedCoins.clear();
                    selectedShelve = null;
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

    public VendingMachineReturnItems cancel(){
        List<BigDecimal> change = Lists.newArrayList(insertedCoins);
        insertedCoins.clear();
        selectedShelve = null;
        return new VendingMachineReturnItems(change, null);
    }

    private List<BigDecimal> getChange(BigDecimal subtract) {
        List<BigDecimal> change = new ArrayList<>();
        BigDecimal rest = new BigDecimal(subtract.toBigInteger());
        for (BigDecimal denomination : coins.keySet()) {
            if(rest.compareTo(denomination) >= 0 && !coins.get(denomination).isEmpty()){
                addToChange(change, rest, denomination);
            }
        }
        for (BigDecimal coin : change) {
            coins.get(coin).remove(coins.get(coin).size() - 1);
        }
        return change;
    }

    private void addToChange(List<BigDecimal> change, BigDecimal rest, BigDecimal denomination) {
        for (BigDecimal coin : coins.get(denomination)) {
            if(rest.subtract(coin).compareTo(BigDecimal.ZERO) >= 0){
                rest = rest.subtract(coin);
                change.add(coin);
            }
        }
    }

    private boolean canGiveAChange(BigDecimal subtract) {
        BigDecimal rest = new BigDecimal(subtract.toBigInteger());
        List<BigDecimal> keys = new ArrayList<>(coins.keySet());
        Collections.sort(keys);
        for (int i = keys.size() - 1; i >= 0; i--) {
            BigDecimal denomination = keys.get(i);
            if (rest.compareTo(denomination) >= 0 && !coins.get(denomination).isEmpty()) {
                for (BigDecimal coin : coins.get(denomination)) {
                    if (rest.subtract(coin).compareTo(BigDecimal.ZERO) >= 0) {
                        rest = rest.subtract(coin);
                    }
                }
            }
        }
        return rest.equals(BigDecimal.ZERO);
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
}
