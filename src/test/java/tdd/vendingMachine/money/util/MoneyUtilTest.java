package tdd.vendingMachine.money.util;

import com.google.common.collect.Maps;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import tdd.vendingMachine.money.coin.entity.Coin;
import tdd.vendingMachine.money.coin.factory.CoinFactory;

import java.util.Map;

public class MoneyUtilTest {

	@Test
	public void sums_map_to_money() {
		Map<Coin, Integer> map = Maps.newLinkedHashMap();
		map.put(CoinFactory.create02(), 3);
		map.put(CoinFactory.create20(), 4);
		map.put(CoinFactory.create10(), 2);

		Assertions.assertThat(MoneyUtil.sum(map).getAmountMinorInt()).isEqualTo(1060);
	}

	@Test
	public void sums_maps_to_map() {
		Map<Coin, Integer> map1 = Maps.newLinkedHashMap();
		map1.put(CoinFactory.create02(), 3);
		map1.put(CoinFactory.create20(), 4);
		map1.put(CoinFactory.create10(), 2);

		Map<Coin, Integer> map2 = Maps.newLinkedHashMap();
		map2.put(CoinFactory.create01(), 15);
		map2.put(CoinFactory.create20(), 4);
		map2.put(CoinFactory.create50(), 1);

		Map<Coin, Integer> map3 = Maps.newLinkedHashMap();
		map3.put(CoinFactory.create02(), 0);
		map3.put(CoinFactory.create10(), 3);
		map3.put(CoinFactory.create05(), 2);
		map3.put(CoinFactory.create01(), 7);

		Map<Coin, Integer> sum = MoneyUtil.sum(map1, map2, map3);

		Assertions.assertThat(sum.get(CoinFactory.create01())).isEqualTo(22);
		Assertions.assertThat(sum.get(CoinFactory.create02())).isEqualTo(3);
		Assertions.assertThat(sum.get(CoinFactory.create05())).isEqualTo(2);
		Assertions.assertThat(sum.get(CoinFactory.create10())).isEqualTo(5);
		Assertions.assertThat(sum.get(CoinFactory.create20())).isEqualTo(8);
		Assertions.assertThat(sum.get(CoinFactory.create50())).isEqualTo(1);
	}

	@Test
	public void subtracts_map_from_map() {
		Map<Coin, Integer> minuend = Maps.newLinkedHashMap();
		minuend.put(CoinFactory.create01(), 9);
		minuend.put(CoinFactory.create02(), 7);
		minuend.put(CoinFactory.create05(), 5);

		Map<Coin, Integer> subtrahend = Maps.newLinkedHashMap();
		subtrahend.put(CoinFactory.create01(), 2);
		subtrahend.put(CoinFactory.create02(), 3);
		subtrahend.put(CoinFactory.create05(), 4);

		Map<Coin, Integer> difference = MoneyUtil.subtract(minuend, subtrahend);

		Assertions.assertThat(difference.get(CoinFactory.create01())).isEqualTo(7);
		Assertions.assertThat(difference.get(CoinFactory.create02())).isEqualTo(4);
		Assertions.assertThat(difference.get(CoinFactory.create05())).isEqualTo(1);
	}

}
