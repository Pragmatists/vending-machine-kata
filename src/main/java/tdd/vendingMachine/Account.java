package tdd.vendingMachine;

import java.util.List;

/**
 * @author Yevhen Sukhomud
 */
public interface Account {

    void makeDeposit(double coin);

    void makeDeposit(List<Double> coins);

    List<Double> withdraw();

}
