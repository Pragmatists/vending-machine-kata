package tdd.vendingMachine.impl;

import tdd.vendingMachine.core.CashHandler;
import tdd.vendingMachine.core.CurrencyUnit;

import java.util.*;

public class BasicCashHandler implements CashHandler {

    private final Map<CurrencyUnit, Integer> denominations = new TreeMap<>((left, right) -> right.compareTo(left));

    @Override
    public CashHandler deposit(Collection<CurrencyUnit> currencyUnits) {
        if (currencyUnits != null) {
            currencyUnits.forEach(elem -> deposit(elem, 1));
        }

        return this;
    }

    @Override
    public CashHandler deposit(CurrencyUnit currencyUnit, int amount) {
        if (amount > 0 && currencyUnit.greaterThan(CurrencyUnit.zero())) {
            Integer existingAmount = denominations.get(currencyUnit);
            denominations.put(currencyUnit, existingAmount == null ? amount : existingAmount + amount);
        }

        return this;
    }

    @Override
    public Collection<CurrencyUnit> withdraw(CurrencyUnit amount) {
        if (amount().isPositive()) {
            BasicCashHandlerWithdrawContext context = withdraw(amount, this.denominations);

            if (context.getSum().equals(amount)) {
                return cleanExtractedDenominations(context.getDenominations());
            } else {
                throw new IllegalStateException("Unable to withdraw " + amount);
            }
        }

        return new ArrayList<>();
    }

    @Override
    public CurrencyUnit amount() {
        CurrencyUnit result = CurrencyUnit.zero();

        for (Map.Entry<CurrencyUnit, Integer> entry : denominations.entrySet()) {
            result = result.add(entry.getKey().multiply(entry.getValue()));
        }

        return result;
    }

    private Collection<CurrencyUnit> cleanExtractedDenominations(Queue<CurrencyUnit> denominations) {
        if (!denominations.isEmpty()) {
            denominations.stream().forEach(currencyUnit -> {
                Integer value = this.denominations.get(currencyUnit);

                if (value != null) {
                    if (--value <= 0) {
                        this.denominations.remove(currencyUnit);
                    } else {
                        this.denominations.put(currencyUnit, value);
                    }
                }
            });
        }

        return denominations;
    }

    private BasicCashHandlerWithdrawContext withdraw(CurrencyUnit value, Map<CurrencyUnit, Integer> denominations) {
        Iterator<Map.Entry<CurrencyUnit, Integer>> iterator = denominations.entrySet().iterator();
        BasicCashHandlerWithdrawContext change = withdrawStartsWith(value, new BasicCashHandlerWithdrawContext(), iterator);

        return change.equalsWithCurrencyUnit(value) ? change : change.clear();
    }

    private BasicCashHandlerWithdrawContext withdrawStartsWith(CurrencyUnit value, BasicCashHandlerWithdrawContext change,
                                                          Iterator<Map.Entry<CurrencyUnit, Integer>> iterator) {

        while (iterator.hasNext()) {
            Map.Entry<CurrencyUnit, Integer> denomination = iterator.next();

            if (denomination.getValue() > 0) {
                if ((change.getSum().add(denomination.getKey())).greaterThan(value)) {
                    change.revertOne().add(denomination.getKey(), value.subtract(change.getSum()).divide(denomination.getKey()).toInteger());
                } else if (change.getSum().add(denomination.getKey()).multiply(denomination.getValue()).greaterThan(value)) {
                    change.add(denomination.getKey(), value.subtract(change.getSum()).divide(denomination.getKey()).toInteger());
                } else {
                    change.add(denomination.getKey(), denomination.getValue());
                }

                if (change.equalsWithCurrencyUnit(value)) {
                    return change;
                }
            }
        }

        return change;
    }
}
