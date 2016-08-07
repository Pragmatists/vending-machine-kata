package tdd.vendingMachine.money.coin.factory;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CoinFactoryTest {

	@Test
	public void creates_coins_with_proper_nominals() {
		Assertions.assertThat(CoinFactory.create01().getNominal().getAmountMinorInt()).isEqualTo(10);
		Assertions.assertThat(CoinFactory.create02().getNominal().getAmountMinorInt()).isEqualTo(20);
		Assertions.assertThat(CoinFactory.create05().getNominal().getAmountMinorInt()).isEqualTo(50);
		Assertions.assertThat(CoinFactory.create10().getNominal().getAmountMinorInt()).isEqualTo(100);
		Assertions.assertThat(CoinFactory.create20().getNominal().getAmountMinorInt()).isEqualTo(200);
		Assertions.assertThat(CoinFactory.create50().getNominal().getAmountMinorInt()).isEqualTo(500);
	}

}
