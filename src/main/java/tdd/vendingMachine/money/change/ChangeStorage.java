package tdd.vendingMachine.money.change;

import com.google.common.collect.Maps;
import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;
import org.joda.money.Money;
import org.springframework.stereotype.Service;
import tdd.vendingMachine.money.coin.entity.Coin;
import tdd.vendingMachine.money.coin.factory.CoinFactory;

import java.util.Map;

@Service
public class ChangeStorage {

	private Map<Coin, Integer> ownedCoins;

	@Getter
	private Map<Coin, Integer> insertedCoins;

	public ChangeStorage() {
		createCoins();
	}

	private void createCoins() {
		ownedCoins = Maps.newLinkedHashMap();
		ownedCoins.put(CoinFactory.create01(), RandomUtils.nextInt(6, 9));
		ownedCoins.put(CoinFactory.create02(), RandomUtils.nextInt(5, 8));
		ownedCoins.put(CoinFactory.create05(), RandomUtils.nextInt(4, 7));
		ownedCoins.put(CoinFactory.create10(), RandomUtils.nextInt(3, 6));
		ownedCoins.put(CoinFactory.create20(), RandomUtils.nextInt(2, 5));
		ownedCoins.put(CoinFactory.create50(), RandomUtils.nextInt(1, 4));
		insertedCoins = Maps.newHashMap();
	}

	public Map<Coin, Integer> getOwnedCoins() {
		return Maps.newLinkedHashMap(ownedCoins);
	}

	public Map<Coin, Integer> getInsertedCoins() {
		return Maps.newLinkedHashMap(insertedCoins);
	}

	public void insertCoin(Coin coin) {
		Integer value = insertedCoins.get(coin);
		value = value == null ? 1 : value + 1;
		insertedCoins.put(coin, value);
	}

	public void returnInsertedCoins() {
		insertedCoins.clear();
	}

	public boolean hasChange(Money money) {
		return ChangeCalculator.calculate(ownedCoins, money) != null;
	}

	public Map<Coin, Integer> giveChange(Money money) {
		Map<Coin, Integer> change = ChangeCalculator.calculate(ownedCoins, money);
		removeChange(change);
		return change;
	}

	private void removeChange(Map<Coin, Integer> change) {
		change.entrySet().forEach(entry -> {
			Coin coin = entry.getKey();
			ownedCoins.put(coin, ownedCoins.get(entry.getKey()) - entry.getValue());
		});
	}
}
