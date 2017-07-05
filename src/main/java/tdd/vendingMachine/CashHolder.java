package tdd.vendingMachine;

import java.util.List;

/**
 * @author Yevhen Sukhomud
 */
public interface CashHolder {

    void insertCoin(double coin);

    List<Double> getSupportedCoins();

}
