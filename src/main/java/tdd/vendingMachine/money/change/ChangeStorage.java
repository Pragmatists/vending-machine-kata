package tdd.vendingMachine.money.change;

import com.google.common.collect.Maps;
import lombok.Setter;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import tdd.vendingMachine.money.coin.entity.Coin;
import tdd.vendingMachine.money.coin.factory.CoinFactory;

import java.util.Map;

@Service
public class ChangeStorage {

	@Setter
	private Map<Coin, Integer> ownedCoins;

	@Setter
	private Map<Coin, Integer> insertedCoins;

	public ChangeStorage() {
		createCoins();
	}

	private void createCoins() {
		ownedCoins = Maps.newLinkedHashMap();
		ownedCoins.put(CoinFactory.create01(), RandomUtils.nextInt(0, 2));
		ownedCoins.put(CoinFactory.create02(), RandomUtils.nextInt(0, 2));
		ownedCoins.put(CoinFactory.create05(), RandomUtils.nextInt(0, 2));
		ownedCoins.put(CoinFactory.create10(), RandomUtils.nextInt(0, 2));
		ownedCoins.put(CoinFactory.create20(), RandomUtils.nextInt(0, 2));
		ownedCoins.put(CoinFactory.create50(), RandomUtils.nextInt(0, 2));
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

}
