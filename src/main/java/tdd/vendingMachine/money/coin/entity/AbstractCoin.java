package tdd.vendingMachine.money.coin.entity;

import lombok.Getter;
import org.joda.money.Money;

public class AbstractCoin implements Coin {

	@Getter
	private final Money nominal;

	AbstractCoin(Money nominal) {
		this.nominal = nominal;
	}

}
