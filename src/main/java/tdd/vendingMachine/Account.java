package tdd.vendingMachine;

import java.util.List;

/**
 * @author Yevhen Sukhomud
 */
public interface Account {

    void makeDeposit(double money);

    void makeDeposit(List<Double> money);

    List<Double> withdraw(double sum);

    List<Double> withdraw(List<Double> sum);

    List<Double> withdrawAll();

    boolean hasChange(double sum);

}
