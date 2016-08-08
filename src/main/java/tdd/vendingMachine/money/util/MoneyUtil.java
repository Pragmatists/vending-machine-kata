package tdd.vendingMachine.money.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.joda.money.Money;
import tdd.vendingMachine.money.coin.entity.Coin;
import tdd.vendingMachine.money.factory.MoneyFactory;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public class MoneyUtil {

	public static Money sum(Map<Coin, Integer> coins) {
		return MoneyFactory.USD(coins.entrySet().stream()
			.map(a -> a.getKey().getNominal().getAmount().multiply(BigDecimal.valueOf(a.getValue())))
			.reduce(BigDecimal::add).orElse(BigDecimal.ZERO).doubleValue());
	}

	public static Map<Coin, Integer> sum(Map<Coin, Integer>... addends) {
		Map<Coin, Integer> sum = Maps.newLinkedHashMap();

		Lists.newArrayList(addends).forEach(consumer ->
			consumer.entrySet().forEach(coins -> {
				Coin coin = coins.getKey();
				sum.put(coin, Optional.ofNullable(sum.get(coin)).orElse(0) + coins.getValue());
			})
		);

		return sum;
	}

	public static Map<Coin, Integer> subtract(Map<Coin, Integer> minuend, Map<Coin, Integer> subtrahend) {
		Map<Coin, Integer> difference = Maps.newLinkedHashMap();

		subtrahend.entrySet().forEach(coins -> {
			Coin coin = coins.getKey();
			difference.put(coin, Optional.ofNullable(minuend.get(coin)).orElse(0) - coins.getValue());
		});

		return difference;
	}

}
