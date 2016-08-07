package tdd.vendingMachine;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by okraskat on 11.02.16.
 */
public class ChangeCalculatorTest {

    @Test
    public void shouldNotGiveChangeWithoutCoins() throws Exception {
        //given
        ChangeCalculator changeCalculator = new ChangeCalculator();
        //when
        boolean result = changeCalculator.canGiveAChange(new BigDecimal("1"), giveEmptyMap());
        //then
        assertFalse(result);
    }

    @Test
    public void shouldReturnChangeWhenOneEqualCoinGiven() throws Exception {
        //given
        Map<BigDecimal, List<BigDecimal>> map = giveEmptyMap();
        BigDecimal coin = new BigDecimal("1");
        map.get(coin).add(new BigDecimal("1"));
        ChangeCalculator changeCalculator = new ChangeCalculator();
        //when
        boolean result = changeCalculator.canGiveAChange(coin, map);
        //then
        assertTrue(result);
    }

    @Test
    public void shouldReturnChangeWhenSmallerCoinsGiven() throws Exception {
        //given
        Map<BigDecimal, List<BigDecimal>> map = giveEmptyMap();
        BigDecimal coin = new BigDecimal("1");
        BigDecimal halfCoin = new BigDecimal("0.5");
        map.get(halfCoin).add(new BigDecimal("0.5"));
        map.get(halfCoin).add(new BigDecimal("0.5"));
        ChangeCalculator changeCalculator = new ChangeCalculator();
        //when
        boolean result = changeCalculator.canGiveAChange(coin, map);
        //then
        assertTrue(result);
    }

    @Test
    public void shouldNotReturnChangeWhenGraterCoinGiven() throws Exception {
        //given
        Map<BigDecimal, List<BigDecimal>> map = giveEmptyMap();
        BigDecimal coin = new BigDecimal("1");
        BigDecimal fiveCoin = new BigDecimal("5");
        map.get(fiveCoin).add(new BigDecimal("5"));
        ChangeCalculator changeCalculator = new ChangeCalculator();
        //when
        boolean result = changeCalculator.canGiveAChange(coin, map);
        //then
        assertFalse(result);
    }

    @Test
    public void shouldReturnChange() throws Exception {
        //given
        Map<BigDecimal, List<BigDecimal>> map = giveEmptyMap();
        BigDecimal coin = new BigDecimal("1");
        BigDecimal halfCoin = new BigDecimal("0.5");
        map.get(halfCoin).add(new BigDecimal("0.5"));
        map.get(halfCoin).add(new BigDecimal("0.5"));
        ChangeCalculator changeCalculator = new ChangeCalculator();
        //when
        List<BigDecimal> result = changeCalculator.getChange(coin, map);
        //then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).compareTo(halfCoin) == 0);
        assertTrue(result.get(1).compareTo(halfCoin) == 0);
    }

    private Map<BigDecimal, List<BigDecimal>> giveEmptyMap() {
        Map<BigDecimal, List<BigDecimal>> map = new HashMap<>();
        map.put(new BigDecimal("0.1"), new ArrayList<>());
        map.put(new BigDecimal("0.2"), new ArrayList<>());
        map.put(new BigDecimal("0.5"), new ArrayList<>());
        map.put(new BigDecimal("1"), new ArrayList<>());
        map.put(new BigDecimal("2"), new ArrayList<>());
        map.put(new BigDecimal("5"), new ArrayList<>());
        return map;
    }

}
