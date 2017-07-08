package tdd.vendingMachine;

import java.util.List;

/**
 * @author Yevhen Sukhomud
 */
public class CashAccount implements Account {

    @Override
    public void makeDeposit(double money) {

    }

    @Override
    public void makeDeposit(List<Double> money) {

    }

    @Override
    public List<Double> withdraw(double sum) {
        return null;
    }

    @Override
    public List<Double> withdraw(List<Double> sum) {
        return null;
    }

    @Override
    public List<Double> withdrawAll() {
        return null;
    }

    @Override
    public boolean hasThisMoney(double sum) {
        return false;
    }

}
