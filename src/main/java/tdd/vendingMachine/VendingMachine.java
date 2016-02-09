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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class VendingMachine {

    private final static Set<BigDecimal> ACCEPTED_DENOMINATIONS = Sets.newHashSet(
        new BigDecimal("5"),
        new BigDecimal("2"),
        new BigDecimal("1"),
        new BigDecimal("0.5"),
        new BigDecimal("0.2"),
        new BigDecimal("0.1"));

    private Map<BigDecimal, List<BigDecimal>> coins = Maps.asMap(ACCEPTED_DENOMINATIONS,
        (Function<BigDecimal, List<BigDecimal>>) bigDecimal -> Lists.newArrayList());

    private final Map<Integer, Shelve> shelves;

    private final Display display;

    private Product selectedProduct;

    private BigDecimal selectedProductPrice;

    private List<BigDecimal> insertedCoins = new ArrayList<>();

    public VendingMachine(List<Shelve> shelvesList, DisplayFactory displayFactory, Function<? super Shelve, Integer> keyMapper) {
        checkNotNull(displayFactory, "DisplayFactory can not be null");
        shelves = Maps.uniqueIndex(shelvesList, keyMapper);
        this.display = displayFactory.createDisplay();
    }

    public Map<Integer, Shelve> getShelves() {
        return shelves;
    }

    public Display getDisplay() {
        return display;
    }

    public BigDecimal selectShelve(Integer shelveNumber) {
        Shelve shelve = shelves.get(shelveNumber);
        if(shelve == null){
            display.showWarning("Shelve with number " + shelveNumber + " does not exist.");
        } else if (shelve.getProducts().isEmpty()){
            display.showEmptyShelve();
        } else {
            selectedProductPrice = shelve.getProductPrice();
            display.showProductPrice(selectedProductPrice);
        }
        return selectedProductPrice;
    }

    public VendingMachineReturnItems insertCoin(BigDecimal inputCoin) {
        Product product = null;
        List<BigDecimal> change = null;
        if(inputCoinValid(inputCoin) && selectedProductPrice != null){
            insertedCoins.add(inputCoin);
            BigDecimal sumInsertedCoins = sumInsertedCoins();
            if(sumInsertedCoins.compareTo(selectedProductPrice) >= 0){
                BigDecimal subtract = sumInsertedCoins.subtract(selectedProductPrice);
                if(canGiveAChange(subtract)) {
                    product = selectedProduct;
                    change = getChange(subtract);
                    addCoinsToMachine();
                } else {
                    display.showChangeWarning();
                    change = Lists.newArrayList(insertedCoins);
                    insertedCoins.clear();
                }
            } else {
                display.showCoverAmount(selectedProductPrice.subtract(sumInsertedCoins));
            }
        } else if(selectedProductPrice == null) {
            display.showProductNotSelected();
        } else {
            change = Lists.newArrayList(inputCoin);
        }
        return new VendingMachineReturnItems(change, product);
    }

    public VendingMachineReturnItems cancel(){
        List<BigDecimal> change = Lists.newArrayList(insertedCoins);
        insertedCoins.clear();
        selectedProduct = null;
        return new VendingMachineReturnItems(change, null);
    }

    private List<BigDecimal> getChange(BigDecimal subtract) {
        List<BigDecimal> change = new ArrayList<>();
        BigDecimal rest = new BigDecimal(subtract.toBigInteger());
        for (BigDecimal denomination : coins.keySet()) {
            if(rest.compareTo(denomination) > 0 && !coins.get(denomination).isEmpty()){
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
            if(rest.subtract(coin).compareTo(BigDecimal.ZERO) > 0){
                change.add(coin);
            }
        }
    }

    private boolean canGiveAChange(BigDecimal subtract) {
        BigDecimal rest = new BigDecimal(subtract.toBigInteger());
        for (BigDecimal denomination : coins.keySet()) {
            if(rest.compareTo(denomination) > 0 && !coins.get(denomination).isEmpty()){
                for (BigDecimal coin : coins.get(denomination)) {
                    if(rest.subtract(coin).compareTo(BigDecimal.ZERO) > 0){
                        rest = rest.subtract(coin);
                    }
                }
            }
        }
        return rest.equals(BigDecimal.ZERO);
    }

    private void addCoinsToMachine() {
        for (BigDecimal insertedCoin : insertedCoins) {
            coins.get(insertedCoin).add(insertedCoin);
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
