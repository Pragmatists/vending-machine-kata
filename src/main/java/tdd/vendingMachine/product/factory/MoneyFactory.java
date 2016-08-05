package tdd.vendingMachine.product.factory;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

public class MoneyFactory {

	public static Money USD(double amount) {
		return Money.of(CurrencyUnit.USD, amount);
	}

}
