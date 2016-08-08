package tdd.vendingMachine.money.change;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.joda.money.Money;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import tdd.vendingMachine.money.coin.entity.Coin;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ChangeCalculator {

	public static Map<Coin, Integer> calculate(Map<Coin, Integer> availableCoins, Money requiredMoney) {
		int requiredAmount = requiredMoney.getAmountMinorInt();
		return chooseMostDispersedCoinSet(getAllSolutions(availableCoins, Lists.newArrayList(), requiredAmount));
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

}
