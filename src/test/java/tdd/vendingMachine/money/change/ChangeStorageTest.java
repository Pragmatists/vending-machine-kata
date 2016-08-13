package tdd.vendingMachine.money.change;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.money.coin.factory.CoinFactory;

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
		changeStorage.insertCoin(CoinFactory.create010());
		changeStorage.insertCoin(CoinFactory.create010());
		changeStorage.insertCoin(CoinFactory.create020());

		Assertions.assertThat(changeStorage.getInsertedCoins().get(CoinFactory.create010())).isEqualTo(2);
		Assertions.assertThat(changeStorage.getInsertedCoins().get(CoinFactory.create020())).isEqualTo(1);
	}

}
