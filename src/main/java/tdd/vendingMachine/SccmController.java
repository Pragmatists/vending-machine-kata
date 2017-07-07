package tdd.vendingMachine;

import tdd.vendingMachine.model.Product;

import java.util.List;

/**
 * @author Yevhen Sukhomud
 */
public class SccmController implements HardwareController {

    private Display display;
    private Inventory inventory;
    private Account account;
    private Bucket bucket;
    private Memory memory;

    public SccmController(Display display, Inventory inventory, Account account, Bucket bucket, Memory memory) {
        this.display = display;
        this.inventory = inventory;
        this.account = account;
        this.bucket = bucket;
        this.memory = memory;
    }

    @Override
    public void processSelection(int index) {
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

    }

    @Override
    public void processPayment(double money) {
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
        display.display("Balance : " + getSum(memory.insertedMoney()));
    }

    @Override
    public void cancel() {
        finishBaying();
    }

    private void finishBaying() {
        putProductAndChangeIntoBucket();
        memory.clear();
        display.display("Thanks!");
    }

    private void putProductAndChangeIntoBucket() {
        double change = memory.price() - getSum(memory.insertedMoney());
        if (needChange(change)) {
            withChange(change);
        } else {
            withoutChange();
        }
    }

    private void withoutChange() {
        bucket.putInto(null, inventory.get(memory.productIndex()));
    }

    private void withChange(double change) {
        if (account.hasChange(change)) {
            bucket.putInto(account.withdraw(change), inventory.get(memory.productIndex()));
        } else {
            display.display("Warning! Doesn't have change");
            bucket.putInto(account.withdraw(memory.insertedMoney()), null);
        }
    }

    private boolean needChange(double change) {
        return change > 0;
    }

    private double getSum(List<Double> money) {
        return money.stream().mapToDouble(Double::doubleValue).sum();
    }

    private boolean isEnoughMoney() {
        return getSum(memory.insertedMoney()) >= memory.price();
    }

    private double howMuchLeft() {
        return memory.price() - getSum(memory.insertedMoney());
    }

}
