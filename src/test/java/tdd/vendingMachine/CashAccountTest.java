package tdd.vendingMachine;

import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.exception.NotEnoughMoneyException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Yevhen Sukhomud
 */
public class CashAccountTest {

    private CashAccount account = new CashAccount();

    @Before
    public void setUp() throws Exception {
        account.withdrawAll(); // clean up account
    }

    @Test
    public void makeDepositAndWithdraw_shouldWithdrawAskedMoney() throws Exception {
        // when
        account.makeDeposit(1);
        account.makeDeposit(2);
        // then
        List<Integer> withdraw = account.withdraw(3);
        assertEquals(2, withdraw.size());
        assertTrue(withdraw.contains(1));
        assertTrue(withdraw.contains(2));
    }

    @Test
    public void makeMultiDepositAndWithdraw_shouldWithdrawAskedMoney() throws Exception {
        // given
        List<Integer> coins = new ArrayList<>();
        coins.add(1);
        coins.add(1);
        coins.add(3);
        // when
        account.makeDeposit(coins);
        List<Integer> withdraw = account.withdraw(2);
        // then
        assertTrue(withdraw.get(0).equals(1));
        assertTrue(withdraw.get(1).equals(1));
    }

    @Test
    public void withdrawSpecificCoins_shouldWithdrawAskedMoney() throws Exception {
        // given
        List<Integer> coinsInAccount = new ArrayList<>();
        coinsInAccount.add(1);
        coinsInAccount.add(2);
        coinsInAccount.add(3);
        List<Integer> coinsToWithdraw = new ArrayList<>();
        coinsToWithdraw.add(2);
        coinsToWithdraw.add(3);
        account.makeDeposit(coinsInAccount);
        // when
        List<Integer> withdraw = account.withdraw(coinsToWithdraw);
        // then
        assertTrue(withdraw.contains(2));
        assertTrue(withdraw.contains(3));
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void withdrawSpecificCoinsWhichIsNotPresent_shouldThrowNotEnoughMoneyException() throws Exception {
        // given
        List<Integer> coinsInAccount = new ArrayList<>();
        coinsInAccount.add(1);
        coinsInAccount.add(2);
        coinsInAccount.add(3);
        List<Integer> coinsToWithdraw = new ArrayList<>();
        coinsToWithdraw.add(5);
        account.makeDeposit(coinsInAccount);
        // when
        List<Integer> withdraw = account.withdraw(coinsToWithdraw);
        // then
        // expected NotEnoughMoneyException
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void withdrawEmptyDeposit_shouldThrowNotEnoughMoneyException() throws Exception {
        // given
        // account is empty
        // when
        List<Integer> withdraw = account.withdraw(1);
        // then
        // expected NotEnoughMoneyException
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void withdrawNotExistingCoins_shouldThrowNotEnoughMoneyException() throws Exception {
        // given
        account.makeDeposit(5);
        // when
        List<Integer> withdraw = account.withdraw(1);
        // then
        // expected NotEnoughMoneyException
    }

    @Test
    public void withdrawAllWithNotEmptyAccount_shouldReturnAllMoney() throws Exception {
        // given
        account.makeDeposit(2);
        account.makeDeposit(3);
        // when
        List<Integer> withdraw = account.withdrawAll();
        // then
        assertTrue(withdraw.size() == 2);
        assertTrue(withdraw.contains(2));
        assertTrue(withdraw.contains(3));
    }

    @Test
    public void withdrawAllWithEmptyAccount_shouldReturnEmptyList() throws Exception {
        // given
        // account is empty
        // when
        List<Integer> withdraw = account.withdrawAll();
        // then
        assertTrue(withdraw.isEmpty());
    }

}
