package tdd.vendingMachine.money.factory;

import org.assertj.core.api.Assertions;
import org.joda.money.Money;
import org.junit.Test;

public class MoneyFactoryTest {

	@Test
	public void produces_money_instance_given_double() {
		Money money = MoneyFactory.of(3.33);

		Assertions.assertThat(money.getAmountMajorInt()).isEqualTo(3);
		Assertions.assertThat(money.getAmountMinorInt()).isEqualTo(333);
		Assertions.assertThat(money.getCurrencyUnit().getCurrencyCode()).isEqualTo("USD");
	}

}
