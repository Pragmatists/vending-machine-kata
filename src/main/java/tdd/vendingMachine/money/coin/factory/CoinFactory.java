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

}
