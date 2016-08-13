package tdd.vendingMachine.money.coin.factory;

import tdd.vendingMachine.money.coin.entity.*;

public class CoinFactory {

	public static Coin create01() {
		return new Coin01();
	}

	public static Coin create02() {
		return new Coin02();
	}

	public static Coin create05() {
		return new Coin05();
	}

	public static Coin create10() {
		return new Coin10();
	}

	public static Coin create20() {
		return new Coin20();
	}

	public static Coin create50() {
		return new Coin50();
	}

	public static Coin ofAmount(Integer amount) {
		switch(amount) {
			case 10:
				return create01();
			case 20:
				return create02();
			case 50:
				return create05();
			case 100:
				return create10();
			case 200:
				return create20();
			case 500:
				return create50();
			default:
				return null;
		}
	}

}
