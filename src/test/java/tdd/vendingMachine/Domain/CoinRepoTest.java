package tdd.vendingMachine.Domain;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;


public class CoinRepoTest {
    CoinRepo repo;

    @Before
    public void setUp() {
        Integer[] nn = new Integer[]{10, 20, 50, 100, 200, 500};
        List<Integer> nom = Arrays.asList(nn);
        repo = new CoinRepo(nom);
    }

    @Test
    public void canAddAndInsertCoins() {
        int nTen = 5;
        int nTwenty = 7;
        repo.addCoins(10, nTen);
        repo.addCoins(20, nTwenty);
        Map<Integer,Integer> coins = repo.getCoins();
        assertThat(coins.get(10)).isEqualTo(nTen);
        assertThat(coins.get(20)).isEqualTo(nTwenty);
        repo.insertCoin(50);
        repo.insertCoin(100);
        repo.insertCoin(10);
        coins = repo.getCoins();
        assertThat(coins.get(50)).isEqualTo(1);
        assertThat(coins.get(100)).isEqualTo(1);
        assertThat(coins.get(10)).isEqualTo(nTen+1);
    }

    @Test
    public void adminCanSubtractCoins() {
        assertThatThrownBy(() -> {
            repo.addCoins(10, -20);
        }).hasMessage(Error.NEGATIVE_NUMBER_OF_COINS.toString());
        repo.addCoins(10, 10);
        repo.addCoins(10, -9);
        assertThat(repo.getCoins().get(10)).isEqualTo(1);
    }

    @Test
    public void wrongNominalsThrowException() {
        assertThatThrownBy(() -> {
            repo.addCoins(5, 10);
        }).hasMessage(Error.INVALID_COIN_NOMINAL.toString());
        assertThatThrownBy(() -> {
            repo.insertCoin(1);
        }).hasMessage(Error.INVALID_COIN_NOMINAL.toString());
        assertThatThrownBy(() -> {
            List<Integer> ii = new ArrayList<>();
            ii.add(0);
            new CoinRepo(ii);
        }).hasMessage(Error.INVALID_COIN_NOMINAL.toString());
        assertThatThrownBy(() -> {
            List<Integer> ii = new ArrayList<>();
            ii.add(-1);
            new CoinRepo(ii);
        }).hasMessage(Error.INVALID_COIN_NOMINAL.toString());
        assertThatThrownBy(() -> {
            List<Integer> ii = new ArrayList<>();
            ii.add(1);
            ii.add(1);  //cannot repeat nominals
            new CoinRepo(ii);
        }).hasMessage(Error.INVALID_COIN_NOMINAL.toString());
    }

    @Test
    public void disbursedCoinsMustBeInRepo() {
        repo.addCoins(10, 5);
        repo.addCoins(20, 5);
        repo.addCoins(50, 5);
        Integer[] cc = new Integer[]{10, 10, 10, 20, 20, 50};
        repo.disburseCoins(Arrays.asList(cc));
        Map<Integer,Integer> coins = repo.getCoins();
        assertThat(coins.get(10)).isEqualTo(2);
        assertThat(coins.get(20)).isEqualTo(3);
        assertThat(coins.get(50)).isEqualTo(4);

        assertThatThrownBy(() -> {
            List<Integer> tooMany = new ArrayList<>();
            tooMany.add(10);
            tooMany.add(10);
            tooMany.add(10);
            repo.disburseCoins(tooMany);
        }).hasMessage(Error.TOO_FEW_COINS_FOR_DISBURSE_ORDER.toString());

    }



}
