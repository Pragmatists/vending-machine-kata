package tdd.vendingMachine;

import tdd.vendingMachine.exception.NotEnoughMoneyException;

import java.util.*;
import java.util.stream.IntStream;

import static tdd.vendingMachine.util.CoinsCalculator.calculateSum;

/**
 * @author Yevhen Sukhomud
 */
public class CashAccount implements Account {

    private Map<Integer, Integer> cashHolder = new TreeMap<>(Collections.reverseOrder());
    private Integer balance = 0;

    @Override
    public void makeDeposit(Integer money) {
        add(money);
    }

    @Override
    public void makeDeposit(List<Integer> money) {
        money.forEach(this::makeDeposit);
    }

    @Override
    public List<Integer> withdraw(Integer sumToWithdraw) {
        List<Integer> result = new ArrayList<>();
        if (sumToWithdraw > balance) {
            throw new NotEnoughMoneyException();
        }

        Set<Integer> denominations = new HashSet<>(cashHolder.keySet());

        for (Integer denomination : denominations) {
            if (sumToWithdraw >= denomination) {
                int denominationQuantity = calculateDenominationQuantity(sumToWithdraw, denomination);
                IntStream.range(0, denominationQuantity).forEach(i -> result.add(denomination));
                sumToWithdraw -= denominationQuantity * denomination;
            }
        }
        if (sumToWithdraw == 0) {
            subtractFromCashHolder(result);
            return result;
        } else {
            throw new NotEnoughMoneyException();
        }
    }

    @Override
    public List<Integer> withdraw(List<Integer> moneyToWithdraw) {
        List<Integer> result = new ArrayList<>();
        moneyToWithdraw.forEach(denomination -> {
            Integer quantity = cashHolder.get(denomination);
            if (quantity == null) {
                throw new NotEnoughMoneyException();
            }
            result.add(denomination);
        });
        subtractFromCashHolder(result);
        return result;
    }

    @Override
    public List<Integer> withdrawAll() {
        List<Integer> result = new ArrayList<>();
        cashHolder.forEach((denomination, quantity) -> IntStream.range(0, quantity).forEach(i -> result.add(denomination)));
        cashHolder.clear();
        balance = 0;
        return result;
    }

    private void subtractFromCashHolder(List<Integer> denominationsToSubtract) {
        denominationsToSubtract
            .forEach(denomination -> {
                int denominationQuantity = cashHolder.get(denomination);
                cashHolder.put(denomination, --denominationQuantity);
            });
        balance -= calculateSum(denominationsToSubtract);
    }

    private void add(Integer denomination) {
        Integer quantity = cashHolder.getOrDefault(denomination, 0);
        cashHolder.put(denomination, quantity + 1);
        balance += denomination;
    }

    private int calculateDenominationQuantity(int sum, int denomination) {
        int denominationQuantity = sum / denomination;
        int availableDenominationQuantity = cashHolder.get(denomination);
        return availableDenominationQuantity > denominationQuantity
            ? denominationQuantity
            : availableDenominationQuantity;
    }

}
