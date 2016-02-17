package tdd.vendingMachine.Service;


import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;


public class ChoinChangerImplTest {
    Map<Integer, Integer> coins;
    CoinChanger changer;
    boolean verdict;

    @Before
    public void setUp() {
        coins = new HashMap<>();
        changer = new CoinChangerImpl(10);
    }

    @Test
    public void simpleChange() {
        coins.put(10, 3);
        List<Integer> given;
        given = changer.distribute(coins, 10);
        verdict = compareCoinSet(given, new int[]{10});
        assertThat(verdict).isTrue();
        given = changer.distribute(coins, 20);
        verdict = compareCoinSet(given, new int[]{10, 10});
        assertThat(verdict).isTrue();

        coins.put(20, 1);
        given = changer.distribute(coins, 20);
        verdict = compareCoinSet(given, new int[]{20});
        assertThat(verdict).isTrue();
        given = changer.distribute(coins, 50);
        verdict = compareCoinSet(given, new int[]{10, 10, 10, 20});
        assertThat(verdict).isTrue();
    }

    @Test
    public void notPossible() {
        coins.put(50, 1);
        List<Integer> given;
        given = changer.distribute(coins, 10);
        assertThat(given).isNull();
        given = changer.distribute(coins, 20);
        assertThat(given).isNull();

        coins.put(20, 1);
        given = changer.distribute(coins, 30);
        assertThat(given).isNull();
        given = changer.distribute(coins, 60);
        assertThat(given).isNull();
    }

    @Test
    public void cornerCases() {
        coins.put(10, 1);
        List<Integer> given;
        given = changer.distribute(coins, 10);
        verdict = compareCoinSet(given, new int[]{10});
        assertThat(verdict).isTrue();

        coins.clear();
        given = changer.distribute(coins, 0);
        verdict = compareCoinSet(given, new int[]{});
        assertThat(verdict).isTrue();
    }

    private boolean compareCoinSet(List<Integer> given, int[] expected) {
        Arrays.sort(expected);
        Collections.sort(given);
        if (given.size()!=expected.length) return false;
        for (int i = 0; i < expected.length; i++) {
            if (given.get(i)!=expected[i]) return false;
        }
        return true;
    }


}
