package tdd.vendingMachine.domain;

import static tdd.vendingMachine.domain.Money.createMoney;

public class PaymentRegistrar {

    private static final Money ZERO = createMoney("0");

    private Money collectedAmount = ZERO;

    private Money amountToBeCollected = ZERO;

    public void register(Money money) {
        collectedAmount = money.add(collectedAmount);
    }

    public boolean hasSufficientMoneyBeenCollected() {
        return collectedAmount.isGreaterOrEqualTo(amountToBeCollected);
    }

    public Money tellHowMuchMoreNeedsToBeCollected() {
        if (hasSufficientMoneyBeenCollected()) {
            return ZERO;
        } else {
            return amountToBeCollected.subtract(collectedAmount);
        }
    }

    public void reset() {
        collectedAmount = ZERO;
        amountToBeCollected = ZERO;
    }

    public Money getCollectedAmount() {
        return collectedAmount;
    }

    public Money getAmountToBeCollected() {
        return amountToBeCollected;
    }

    public void setAmountToBeCollected(Money amountToBeCollected) {
        this.amountToBeCollected = amountToBeCollected;
    }
}
