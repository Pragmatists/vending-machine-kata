package tdd.vendingMachine.money;

public enum Coin implements Money {

	COIN_0_1(0.1f), COIN_0_2(0.2f), COIN_0_5(0.5f), COIN_1(1f), COIN_2(2f), COIN_5(5f);

	private float value;

	private Coin(float value) {
		this.value = value;
	}

	public float getValue() {
		return value;
	}
}
