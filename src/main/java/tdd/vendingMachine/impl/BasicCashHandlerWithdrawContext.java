package tdd.vendingMachine.impl;

import tdd.vendingMachine.core.CurrencyUnit;

import java.util.ArrayDeque;
import java.util.Queue;

public class BasicCashHandlerWithdrawContext {
    private Queue<CurrencyUnit> denominations = new ArrayDeque<>();
    private CurrencyUnit sum = CurrencyUnit.zero();

    public CurrencyUnit getSum() {
        return sum;
    }

    public Queue<CurrencyUnit> getDenominations() {
        return denominations;
    }

    public void add(CurrencyUnit denomination, Integer count) {
        if (denomination != null && count != null) {
            for (Integer i = 0; i < count; ++i) {
                denominations.add(denomination);
                sum = sum.add(denomination);
            }
        }
    }

    boolean equalsWithCurrencyUnit(CurrencyUnit value) {
        return sum.equals(value);
    }

    public BasicCashHandlerWithdrawContext revertOne() {
        if (!denominations.isEmpty()) {
            sum = sum.subtract(denominations.poll());
        }

        return this;
    }

    public BasicCashHandlerWithdrawContext clear() {
        denominations.clear();
        sum = CurrencyUnit.zero();

        return this;
    }
}
