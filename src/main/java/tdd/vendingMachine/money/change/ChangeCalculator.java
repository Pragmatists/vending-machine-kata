package tdd.vendingMachine.money.change;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import org.joda.money.Money;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import tdd.vendingMachine.money.coin.entity.Coin;
import tdd.vendingMachine.money.coin.factory.CoinFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ChangeCalculator {

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

	public static Map<Coin, Integer> calculateChange(Map<Coin, Integer> availableCoins, Money requiredMoney) {
		int requiredAmount = requiredMoney.getAmountMinorInt();
		return chooseMostDispersedCoinSet(getAllSolutions(availableCoins, Lists.newArrayList(), requiredAmount));
	}

	public static Map<Coin, Integer> calculateChangeDifference(Map<Coin, Integer> storage, Money target) {
		List<Coin> coins = toCoins(storage);
		List<Integer> amounts = toAmounts(coins);
		return getBestSubset(amounts, target);
	}

	private static Set<Map<Coin, Integer>> getAllSolutions(Map<Coin, Integer> availableCoins,
			List<Coin> alreadyUsedCoins, int requiredAmount) {
		Set<Map<Coin, Integer>> solutions = Sets.newHashSet();

		availableCoins.entrySet().forEach(consumer -> {
			Map<Coin, Integer> remainingAvailableCoins = removeCoin(availableCoins, consumer.getKey());

			for (int i = 0; i <= consumer.getValue(); i++) {
				int coinSumValue = getCoinValue(consumer.getKey()) * i;
				if (coinSumValue == 0) {
					continue;
				}
				List<Coin> coins = Lists.newArrayList(alreadyUsedCoins);
				int remainingRequiredAmount = requiredAmount;
				if (coinSumValue <= remainingRequiredAmount) {
					remainingRequiredAmount -= coinSumValue;
					addMultipleCoins(coins, consumer.getKey(), i);
				}
				if (remainingRequiredAmount == 0) {
					solutions.add(createSolution(coins));
				} else {
					solutions.addAll(getAllSolutions(remainingAvailableCoins, coins, remainingRequiredAmount));
				}
			}
		});

		return solutions;
	}

	private static Map<Coin, Integer> chooseMostDispersedCoinSet(Set<Map<Coin, Integer>> set) {
		List<Map<Coin, Integer>> list = set.stream().collect(Collectors.toList());

		if (list.isEmpty()) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		}

		return doChooseMostDispersedCoinSet(list);
	}

	private static Map<Coin, Integer> doChooseMostDispersedCoinSet(List<Map<Coin, Integer>> list) {
		MultiValueMap<Integer, Map<Coin, Integer>> dispersion = new LinkedMultiValueMap<>();
		list.forEach(map -> dispersion.add(map.size(), map));
		return dispersion.get(getMax(dispersion)).get(0);
	}

	private static Integer getMax(MultiValueMap<Integer, Map<Coin, Integer>> dispersion) {
		return dispersion.entrySet().stream().mapToInt(Map.Entry::getKey).max().getAsInt();
	}

	private static Map<Coin, Integer> createSolution(List<Coin> coins) {
		Map<Coin, Integer> solution = Maps.newHashMap();
		coins.forEach(coin -> solution.put(coin, Optional.ofNullable(solution.get(coin)).orElse(0) + 1));
		return solution;
	}

	private static void addMultipleCoins(List<Coin> coins, Coin coin, int repetitions) {
		int i = 0;
		while (i < repetitions) {
			coins.add(coin);
			i++;
		}
	}

	private static Map<Coin, Integer> removeCoin(Map<Coin, Integer> map, Coin coin) {
		return map.entrySet()
			.stream()
			.filter(coinIntegerEntry -> coinIntegerEntry.getKey() != coin)
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private static int getCoinValue(Coin coin) {
		return coin.getNominal().getAmountMinorInt();
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
		Map<Coin, Integer> subset = CoinFactory.emptyCoinStorage();

		List<Integer> nominals = getSubset(amounts, target.getAmountMinorInt());

		if (nominals == null) {
			return null;
		}

		if (!nominals.isEmpty()) {
			nominals.forEach(consumer -> {
				Coin coin = CoinFactory.ofAmount(consumer);
				subset.put(coin, subset.get(coin) + 1);
			});
		}

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
