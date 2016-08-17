package tdd.vendingMachine.money.coin.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import tdd.vendingMachine.money.coin.entity.*;

import java.util.List;
import java.util.Map;

public class CoinFactory {

	private static final Map<Class, Coin> instances = Maps.newLinkedHashMap();

	static {
		instances.put(Coin010.class, new Coin010());
		instances.put(Coin020.class, new Coin020());
		instances.put(Coin050.class, new Coin050());
		instances.put(Coin100.class, new Coin100());
		instances.put(Coin200.class, new Coin200());
		instances.put(Coin500.class, new Coin500());
	}

	public static final List<Coin> AVAILABLE_COINS = Lists.newArrayList(
		create010(), create020(), create050(), create100(), create200(), create500());

	public static Coin create010() {
		return instances.get(Coin010.class);
	}

	public static Coin create020() {
		return instances.get(Coin020.class);
	}

	public static Coin create050() {
		return instances.get(Coin050.class);
	}

	public static Coin create100() {
		return instances.get(Coin100.class);
	}

	public static Coin create200() {
		return instances.get(Coin200.class);
	}

	public static Coin create500() {
		return instances.get(Coin500.class);
	}

	public static Map<Coin, Integer> emptyCoinStorage() {
		Map<Coin, Integer> emptyCoinStorage = Maps.newLinkedHashMap();
		emptyCoinStorage.put(create010(), 0);
		emptyCoinStorage.put(create020(), 0);
		emptyCoinStorage.put(create050(), 0);
		emptyCoinStorage.put(create100(), 0);
		emptyCoinStorage.put(create200(), 0);
		emptyCoinStorage.put(create500(), 0);
		return emptyCoinStorage;
	}

	public static Coin ofAmount(Integer amount) {
		switch(amount) {
			case 10:
				return create010();
			case 20:
				return create020();
			case 50:
				return create050();
			case 100:
				return create100();
			case 200:
				return create200();
			case 500:
				return create500();
			default:
				return null;
		}
	}

}
