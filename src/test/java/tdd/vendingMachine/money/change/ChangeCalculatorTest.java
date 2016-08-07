package tdd.vendingMachine.money.change;

import com.google.common.collect.Maps;
import org.assertj.core.api.Assertions;
import org.joda.money.Money;
import org.junit.Test;
import tdd.vendingMachine.money.coin.entity.Coin;
import tdd.vendingMachine.money.coin.factory.CoinFactory;
import tdd.vendingMachine.money.factory.MoneyFactory;

import java.util.Map;

public class ChangeCalculatorTest {

	@Test
	public void calculates_change_for_amount_of_250_and_coins_100_50_50_20_20_10_10() {
		Map<Coin, Integer> coins = Maps.newHashMap();
		coins.put(CoinFactory.create10(), 1);
		coins.put(CoinFactory.create05(), 2);
		coins.put(CoinFactory.create02(), 2);
		coins.put(CoinFactory.create01(), 2);

		Money amount = MoneyFactory.USD(2.50);

		Map<Coin, Integer> solution = ChangeCalculator.calculate(coins, amount);

		Assertions.assertThat(solution).isNotNull();
		Assertions.assertThat(solution).hasSize(4);
		Assertions.assertThat(solution.get(CoinFactory.create10())).isEqualTo(1);
		Assertions.assertThat(solution.get(CoinFactory.create05())).isEqualTo(2);
		Assertions.assertThat(solution.get(CoinFactory.create02())).isEqualTo(2);
		Assertions.assertThat(solution.get(CoinFactory.create01())).isEqualTo(1);
	}

	@Test
	public void calculates_change_for_amount_of_80_and_coins_50_20_20_20_20_10_10_10() {
		Map<Coin, Integer> coins = Maps.newHashMap();
		coins.put(CoinFactory.create05(), 1);
		coins.put(CoinFactory.create02(), 4);
		coins.put(CoinFactory.create01(), 3);

		Money amount = MoneyFactory.USD(.80);

		Map<Coin, Integer> solution = ChangeCalculator.calculate(coins, amount);

		Assertions.assertThat(solution).isNotNull();
		Assertions.assertThat(solution).hasSize(3);
		Assertions.assertThat(solution.get(CoinFactory.create05())).isEqualTo(1);
		Assertions.assertThat(solution.get(CoinFactory.create02())).isEqualTo(1);
		Assertions.assertThat(solution.get(CoinFactory.create01())).isEqualTo(1);
	}

	@Test
	public void calculates_change_for_amount_of_300_and_coins_3_coins_of_100_6_coins_of_50_15_coins_of_20_30_coins_of_10() {
		Map<Coin, Integer> coins = Maps.newHashMap();
		coins.put(CoinFactory.create10(), 3);
		coins.put(CoinFactory.create05(), 6);
		coins.put(CoinFactory.create02(), 15);
		coins.put(CoinFactory.create01(), 30);

		Money amount = MoneyFactory.USD(3);

		Map<Coin, Integer> solution = ChangeCalculator.calculate(coins, amount);

		Assertions.assertThat(solution).isNotNull();
		Assertions.assertThat(solution).hasSize(4);
		Assertions.assertThat(solution.get(CoinFactory.create10())).isEqualTo(1);
		Assertions.assertThat(solution.get(CoinFactory.create05())).isEqualTo(1);
		Assertions.assertThat(solution.get(CoinFactory.create02())).isEqualTo(1);
		Assertions.assertThat(solution.get(CoinFactory.create01())).isEqualTo(13);
	}

	@Test
	public void calculates_change_for_amount_of_200_and_30_coins_of_10() {
		Map<Coin, Integer> coins = Maps.newHashMap();
		coins.put(CoinFactory.create01(), 30);

		Money amount = MoneyFactory.USD(2);

		Map<Coin, Integer> solution = ChangeCalculator.calculate(coins, amount);

		Assertions.assertThat(solution).isNotNull();
		Assertions.assertThat(solution).hasSize(1);
		Assertions.assertThat(solution.get(CoinFactory.create01())).isEqualTo(20);
	}

	@Test
	public void calculates_change_for_amount_of_50_and_coins_20_20_20() {
		Map<Coin, Integer> coins = Maps.newHashMap();
		coins.put(CoinFactory.create02(), 3);

		Money amount = MoneyFactory.USD(.50);

		Map<Coin, Integer> solution = ChangeCalculator.calculate(coins, amount);

		Assertions.assertThat(solution).isNull();
	}

}
