package tdd.vendingMachine.Domain;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;


public class CoinRepoTest {
    CoinRepo repo;

    @Before
    public void setUp() {
        List<Integer> nom = new ArrayList<>();
        nom.add(10);
        nom.add(20);
        nom.add(50);
        nom.add(100);
        nom.add(200);
        nom.add(500);
        repo = new CoinRepo(nom);
    }

    @Test
    public void canAddAndInsertCoins() {
        int nTen = 5;
        int nTwenty = 7;
        repo.addCoins(10, nTen);
        repo.addCoins(20, nTwenty);
        Map<Integer,Integer> coins = repo.getCoins();
        for (int i = 0; i < nTen + nTwenty; i++) {
            if (i<nTen) assertThat(coins.get(i)).isEqualTo(10);
            else assertThat(coins.get(i)).isEqualTo(20);
        }
        repo.insertCoin(50);
        repo.insertCoin(100);
        repo.insertCoin(10);
        coins = repo.getCoins();
        assertThat(coins.get(coins.size() - 1)).isEqualTo(10);
        assertThat(coins.get(coins.size() - 2)).isEqualTo(100);
        assertThat(coins.get(coins.size() - 3)).isEqualTo(50);
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



}
