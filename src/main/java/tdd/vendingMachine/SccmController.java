package tdd.vendingMachine;

import tdd.vendingMachine.exception.NotEnoughMoneyException;
import tdd.vendingMachine.model.Product;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static tdd.vendingMachine.util.CoinsCalculator.calculateChange;
import static tdd.vendingMachine.util.CoinsCalculator.calculateSum;

/**
 * @author Yevhen Sukhomud
 */
public class SccmController implements HardwareController {

    private Display display;
    private Inventory inventory;
    private Account account;
    private Bucket bucket;
    private Memory memory;
    private PropertyReader propertyReader = new PropertyReader();
    private Set<Integer> acceptableDenominations = new HashSet<>();

    public SccmController(Display display, Inventory inventory, Account account, Bucket bucket, Memory memory) {
        try {
            acceptableDenominations = propertyReader.readAcceptableDenominations();
        } catch (IOException e) {
            throw new IllegalStateException();
        }
        this.display = display;
        this.inventory = inventory;
        this.account = account;
        this.bucket = bucket;
        this.memory = memory;
    }

    @Override
    public void processSelection(int index) {
        try {
            Product product = inventory.get(index);
            if (product == null) {
                display.display("There is no product with " + index + " index");
                return;
            }
            memory.remember(product, index);

            if (isEnoughMoney()) {
                finishBaying();
                return;
            }

            if (memory.hasInsertedMoney()) {
                display.display("Please add : " + howMuchLeft());
            } else {
                display.display("Price: " + product.price());
            }
        } catch (NotEnoughMoneyException ex) {
            display.display("Warning! Doesn't have change");
            bucket.putInto(account.withdraw(memory.insertedMoney()), null);
        }

    }

    @Override
    public void processPayment(Integer money) {
        if (!acceptableDenominations.contains(money)) {
            bucket.putInto(money);
            return;
        }
        account.makeDeposit(money);
        memory.remember(money);
        if (memory.hasSelectedProduct()) {
            if (isEnoughMoney()) {
                finishBaying();
                return;
            }
            display.display("Please add : " + howMuchLeft());
            return;
        }
        display.display("Balance : " + calculateSum(memory.insertedMoney()));
    }

    @Override
    public void cancel() {
        finishBaying();
    }

    private void finishBaying() {
        putProductAndChangeIntoBucket();
        memory.clear();
    }

    private void putProductAndChangeIntoBucket() {
        Integer change = calculateChange(calculateSum(memory.insertedMoney()), memory.price());
        if (needChange(change)) {
            withChange(change);
        } else {
            withoutChange();
        }
    }

    private void withoutChange() {
        bucket.putInto(null, inventory.getAndDelete(memory.productIndex()));
    }

    private void withChange(Integer change) {
        List<Integer> withdraw = account.withdraw(change);
        bucket.putInto(withdraw, inventory.getAndDelete(memory.productIndex()));
    }

    private boolean needChange(Integer change) {
        return change > 0;
    }

    private boolean isEnoughMoney() {
        return calculateSum(memory.insertedMoney()) >= memory.price();
    }

    private Integer howMuchLeft() {
        return memory.price() - calculateSum(memory.insertedMoney());
    }

}
