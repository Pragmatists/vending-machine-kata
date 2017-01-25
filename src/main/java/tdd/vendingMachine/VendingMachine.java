package tdd.vendingMachine;

import java.util.EnumMap;

public class VendingMachine {

    private Charger charger;
    private Shelves shelves;
    private Display display;

    private long collectedMoney = 0;
    private Product product;
    private int selectedShelve;

    public VendingMachine(Charger charger, Shelves shelves, Display display) {
        this.charger = charger;
        this.shelves = shelves;
        this.display = display;
        reset();
    }

    public boolean selectShelve(int shelveNo) {
        long count = shelves.count(shelveNo);
        if (count == 0) {
            return false;
        }
        product = shelves.peek(shelveNo);
        selectedShelve = shelveNo;
        display.show(Display.PRODUCT_PRICE_MSG);
        return true;
    }

    public Result buyProduct(Denomination denomination) throws InsuffiecientMoenyForChange {
        Preconditions.checkState(selectedShelve != -1, "The method VendingMachine#selectShelve should be called first");
        Preconditions.checkState(product != null, "The method VendingMachine#selectShelve should be called first.");
        Result result = null;
        charger.supplyMoney(denomination, 1);
        collectedMoney += denomination.value();
        long productPrice = product.getPrice();
        long remains = remainsToPay(productPrice);
        if (isPriceCovered(productPrice)) {
            EnumMap<Denomination, Integer> change = null;
            try {
                change = charger.getChange(collectedMoney - productPrice);
            } catch (InsuffiecientMoenyForChange insuffiecientMoenyForChange) {
                display.show(Display.WARNING_MSG);
                throw insuffiecientMoenyForChange;
            }
            Product product = shelves.take(selectedShelve);
            result = new Result(product, change);
            reset();
        } else {
            display.show(Display.REMAINS_TO_PAY_MSG + remains);
        }
        return result;
    }

    boolean isPriceCovered(long price) {
        return price <= collectedMoney;
    }

    long remainsToPay(long price) {
        long remain = price - collectedMoney;
        return remain <= 0 ? 0 : remain;
    }

    private void reset() {
        selectedShelve = -1;
        product = null;
        display.show(Display.WELCOME_MSG);
    }

    public EnumMap<Denomination, Integer> cancel() throws InsuffiecientMoenyForChange {
        EnumMap<Denomination, Integer> change = charger.getChange(collectedMoney);
        reset();
        return change;
    }
}
