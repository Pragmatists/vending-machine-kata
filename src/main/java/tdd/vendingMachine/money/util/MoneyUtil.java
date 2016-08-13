package tdd.vendingMachine.money.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import org.joda.money.Money;
import tdd.vendingMachine.money.coin.entity.Coin;
import tdd.vendingMachine.money.coin.factory.CoinFactory;
import tdd.vendingMachine.money.factory.MoneyFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class MoneyUtil {

	@Data
	private static class Subset {
		List<Integer> amounts;

		Integer fromIndex;

		Integer target;

		Integer sumInStack;

		Set<List<Integer>> solutions;

		Stack<Integer> stack;

		Subset(List<Integer> amounts, Integer target) {
			this.amounts = amounts;
			this.target = target;
			solutions = Sets.newLinkedHashSet();
			stack = new Stack<>();
			fromIndex = sumInStack = 0;
		}
	}

	public static Money sum(Map<Coin, Integer> coins) {
		return MoneyFactory.USD(coins.entrySet().stream()
			.map(a -> a.getKey().getNominal().getAmount().multiply(BigDecimal.valueOf(a.getValue())))
			.reduce(BigDecimal::add).orElse(BigDecimal.ZERO).doubleValue());
	}

	public static Map<Coin, Integer> add(Map<Coin, Integer>... addends) {
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
		Map<Coin, Integer> difference = Maps.newLinkedHashMap(minuend);

		subtrahend.entrySet().forEach(coins -> {
			Coin coin = coins.getKey();
			difference.put(coin, Optional.ofNullable(minuend.get(coin)).orElse(0) - coins.getValue());
		});

		return difference;
	}

	public static Map<Coin, Integer> subset(Map<Coin, Integer> storage, Money target) {
		List<Coin> coins = toCoins(storage);
		List<Integer> amounts = toAmounts(coins);
		return getBestSubset(amounts, target);
	}

	private static List<Coin> toCoins(Map<Coin, Integer> storage) {
		List<Coin> coins = Lists.newArrayList();
		storage.entrySet().forEach(consumer -> {
			int i = 0;
			while(i < consumer.getValue()) {
				coins.add(consumer.getKey());
				i++;
			}
		});
		return coins;
	}

	private static List<Integer> toAmounts(List<Coin> coins) {
		return coins
			.stream()
			.map(coin -> coin.getNominal().getAmountMinorInt())
			.collect(Collectors.toList());
	}

	private static Map<Coin, Integer> getBestSubset(List<Integer> amounts, Money target) {
		Map<Coin, Integer> subset = Maps.newLinkedHashMap();

		getSubset(amounts, target.getAmountMinorInt()).forEach(consumer -> {
			Coin coin = CoinFactory.ofAmount(consumer);
			subset.put(coin, Optional.ofNullable(subset.get(coin)).orElse(0) + 1);
		});

		return subset;
	}

	// based on http://codereview.stackexchange.com/q/36214
	private static List<Integer> getSubset(List<Integer> amounts, Integer target) {
		Subset subset = new Subset(amounts, target);
		getSubsets(subset);
		return subset.getSolutions().stream()
			.sorted((base, compare) -> Integer.compare(base.size(), compare.size()))
			.findFirst().orElse(null);
	}

	private static void getSubsets(Subset subset) {
		if (sumInStackEqualsTarget(subset)) {
			addSolution(subset);
		}

		for (Integer currentIndex = subset.getFromIndex(); currentIndex < subset.getAmounts().size(); currentIndex++) {
			if (sumInStackAndNextAmountIsNotGraterThanTarget(subset, currentIndex)) {
				doIterateSubset(subset, currentIndex);
			}
		}
	}

	private static boolean sumInStackAndNextAmountIsNotGraterThanTarget(Subset subset, Integer currentIndex) {
		return subset.getSumInStack() + subset.getAmounts().get(currentIndex) <= subset.getTarget();
	}

	private static boolean sumInStackEqualsTarget(Subset subset) {
		return subset.getSumInStack().equals(subset.getTarget());
	}

	private static void addSolution(Subset subset) {
		List<Integer> list = Lists.newArrayList();
		subset.getStack().forEach(list::add);
		subset.getSolutions().add(list);
	}

	private static void doIterateSubset(Subset subset, Integer currentIndex) {
		subset.getStack().push(subset.getAmounts().get(currentIndex));
		subset.setSumInStack(subset.getSumInStack() + subset.getAmounts().get(currentIndex));
		subset.setFromIndex(currentIndex + 1);
		if (subset.getSolutions().isEmpty()) {
			getSubsets(subset);
		}
		subset.setSumInStack(subset.getSumInStack() - subset.getStack().pop());
	}

}
