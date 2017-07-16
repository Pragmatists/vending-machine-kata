package tdd.vendingMachine;

import java.util.List;

/**
 * @author Yevhen Sukhomud
 */
public interface Account {

    void makeDeposit(Integer money);

    void makeDeposit(List<Integer> money);

    List<Integer> withdraw(Integer sumToWithdraw);

    List<Integer> withdraw(List<Integer> denominationToWithdraw);

    List<Integer> withdrawAll();

}
