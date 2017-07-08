package tdd.vendingMachine;

import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.exception.DoesNotHaveCoinException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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
        List<Double> withdraw = account.withdraw(3);
        assertEquals(2, withdraw.size());
        assertTrue(withdraw.contains(1d));
        assertTrue(withdraw.contains(2d));
    }

    @Test
    public void makeMultiDepositAndWithdraw_shouldWithdrawAskedMoney() throws Exception {
        // given
        List<Double> coins = new ArrayList<>();
        coins.add(1d);
        coins.add(1d);
        coins.add(3d);
        // when
        account.makeDeposit(coins);
        List<Double> withdraw = account.withdraw(2);
        // then
        assertTrue(withdraw.get(0) == 1d);
        assertTrue(withdraw.get(1) == 1d);
    }

    @Test
    public void withdrawSpecificCoins_shouldWithdrawAskedMoney() throws Exception {
        // given
        List<Double> coinsInAccount = new ArrayList<>();
        coinsInAccount.add(1d);
        coinsInAccount.add(2d);
        coinsInAccount.add(3d);
        List<Double> coinsToWithdraw = new ArrayList<>();
        coinsToWithdraw.add(2d);
        coinsToWithdraw.add(3d);
        account.makeDeposit(coinsInAccount);
        // when
        List<Double> withdraw = account.withdraw(coinsToWithdraw);
        // then
        assertTrue(withdraw.contains(2d));
        assertTrue(withdraw.contains(3d));
    }

    @Test(expected = DoesNotHaveCoinException.class)
    public void withdrawSpecificCoinsWhichIsNotPresent_shouldThrowDoesNotCoinException() throws Exception {
        // given
        List<Double> coinsInAccount = new ArrayList<>();
        coinsInAccount.add(1d);
        coinsInAccount.add(2d);
        coinsInAccount.add(3d);
        List<Double> coinsToWithdraw = new ArrayList<>();
        coinsToWithdraw.add(5D);
        account.makeDeposit(coinsInAccount);
        // when
        account.withdraw(coinsToWithdraw);
        // then
        // throw DoesNotHaveCoinException
    }

    @Test(expected = DoesNotHaveCoinException.class)
    public void withdrawEmptyDeposit_shouldReturnEmptyList() throws Exception {
        // given
        // account is empty
        // when
        account.withdraw(1);
        // then
        // throw DoesNotHaveCoinException
    }

    @Test(expected = DoesNotHaveCoinException.class)
    public void withdrawNotExistingCoins_shouldThrowDoesNotCoinException() throws Exception {
        // given
        account.makeDeposit(5);
        // when
        account.withdraw(1);
        // then
        // throw DoesNotHaveCoinException
    }

    @Test
    public void withdrawAllWithNotEmptyAccount_shouldReturnAllMoney() throws Exception {
        // given
        account.makeDeposit(2);
        account.makeDeposit(3);
        // when
        List<Double> withdraw = account.withdrawAll();
        // then
        assertTrue(withdraw.size() == 2);
        assertTrue(withdraw.contains(2d));
        assertTrue(withdraw.contains(3d));
    }

    @Test
    public void withdrawAllWithEmptyAccount_shouldReturnEmptyList() throws Exception {
        // given
        // account is empty
        // when
        List<Double> withdraw = account.withdrawAll();
        // then
        assertTrue(withdraw.isEmpty());
    }

    @Test
    public void hasChangeHasNotCoin_shouldReturnFalse() throws Exception {
        // given
        // account is empty
        // when
        boolean hasChange = account.hasThisMoney(5);
        // then
        assertFalse(hasChange);
    }

    @Test
    public void hasChangeHasNotProperCoins_shouldReturnFalse() throws Exception {
        // given
        account.makeDeposit(1);
        account.makeDeposit(1);
        // when
        boolean hasChange = account.hasThisMoney(5);
        // then
        assertFalse(hasChange);
    }

    @Test
    public void hasChangeHasProperCoins_shouldReturnTrue() throws Exception {
        // given
        account.makeDeposit(2);
        account.makeDeposit(1);
        // when
        boolean hasChange = account.hasThisMoney(3);
        // then
        assertTrue(hasChange);
    }

}
