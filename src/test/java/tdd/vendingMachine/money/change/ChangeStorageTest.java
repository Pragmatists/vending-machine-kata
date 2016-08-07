package tdd.vendingMachine.money.change;

import com.google.common.collect.Maps;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.money.coin.entity.Coin;
import tdd.vendingMachine.money.coin.factory.CoinFactory;
import tdd.vendingMachine.money.factory.MoneyFactory;

import java.util.Map;

public class ChangeStorageTest {

	private ChangeStorage changeStorage;

	@Before
	public void setup() {
		changeStorage = new ChangeStorage();
	}

	@Test
	public void owned_coins_are_created_on_initialization() {
		Assertions.assertThat(changeStorage.getOwnedCoins()).hasSize(6);
	}

	@Test
	public void inserted_coins_are_created_on_initialization() {
		Assertions.assertThat(changeStorage.getInsertedCoins()).isEmpty();
	}

	@Test
	public void coins_can_be_inserted() {
		changeStorage.insertCoin(CoinFactory.create01());
		changeStorage.insertCoin(CoinFactory.create01());
		changeStorage.insertCoin(CoinFactory.create02());

		Assertions.assertThat(changeStorage.getInsertedCoins().get(CoinFactory.create01())).isEqualTo(2);
		Assertions.assertThat(changeStorage.getInsertedCoins().get(CoinFactory.create02())).isEqualTo(1);
	}

	@Test
	public void inserted_coins_can_be_returned() {
		changeStorage.insertCoin(CoinFactory.create01());

		Assertions.assertThat(changeStorage.getInsertedCoins()).isNotEmpty();

		changeStorage.returnInsertedCoins();

		Assertions.assertThat(changeStorage.getInsertedCoins()).isEmpty();
	}

	@Test
	public void no_change_should_be_available_for_very_big_money_value() {
		Assertions.assertThat(changeStorage.hasChange(MoneyFactory.USD(1000))).isFalse();
	}

	@Test
	public void change_should_be_available_for_amount_equal_to_a_single_coin() {
		Assertions.assertThat(changeStorage.hasChange(MoneyFactory.USD(1))).isTrue();
	}

	@Test
	public void change_is_returned() {
		Coin coin01 = CoinFactory.create01();
		Map<Coin, Integer> previousOwnedCount = changeStorage.getOwnedCoins();
		Map<Coin, Integer> change = Maps.newHashMap();
		change.put(coin01, 1);

		changeStorage.giveChange(MoneyFactory.USD(.1));

		Assertions.assertThat(previousOwnedCount.get(coin01) - 1).isEqualTo(changeStorage.getOwnedCoins().get(coin01));
	}

}
