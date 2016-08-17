package tdd.vendingMachine.money.factory;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

public class MoneyFactory {

	public static Money of(double amount) {
		return Money.of(CurrencyUnit.USD, amount);
	}

}
