package tdd.vendingMachine.money;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.RandomUtils;
import org.joda.money.Money;
import tdd.vendingMachine.money.coin.entity.Coin;
import tdd.vendingMachine.money.coin.factory.CoinFactory;

import java.util.Map;

public class ChangeStorage {

	private Map<Coin, Integer> coins;

	public ChangeStorage() {
		coins = Maps.newHashMap();
		coins.put(CoinFactory.create01(), RandomUtils.nextInt(6, 9));
		coins.put(CoinFactory.create02(), RandomUtils.nextInt(5, 8));
		coins.put(CoinFactory.create05(), RandomUtils.nextInt(4, 7));
		coins.put(CoinFactory.create10(), RandomUtils.nextInt(3, 6));
		coins.put(CoinFactory.create20(), RandomUtils.nextInt(2, 5));
		coins.put(CoinFactory.create50(), RandomUtils.nextInt(1, 4));
	}

	public boolean hasChange(Money money) {
		return false; // TODO
	}

	public Map<Coin, Integer> giveChange(Money money) {
		return Maps.newHashMap(); // TODO
	}

}
