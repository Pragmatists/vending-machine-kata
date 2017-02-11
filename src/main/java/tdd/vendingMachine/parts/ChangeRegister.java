package tdd.vendingMachine.parts;

import java.util.Map;

import tdd.vendingMachine.enumeration.CoinsEnum;

public class ChangeRegister extends CoinRegister{
	
	private PaymentRegister paymentRegister;
	
	public ChangeRegister(Map<CoinsEnum, Integer> coinsRegisterMap, PaymentRegister paymentRegister) {
		super(coinsRegisterMap);
		this.paymentRegister = paymentRegister;
	}

	/**
	 * Move money from payment register to changeRegister. Payment register should be empty after completing operation
	 */
	public void depositMoney() {
		paymentRegister.flush().entrySet().stream().forEach(it -> {
			coinsRegisterMap.merge(it.getKey(), it.getValue(), (q1, q2) -> q1+q2);
		});
	}

	/**
	 * Subtacting money from changeRegisterMap
	 * @param change
	 */
	public void subtractChange(Map<CoinsEnum, Integer> change) {
		change.entrySet().stream().forEach(it -> {
			coinsRegisterMap.merge(it.getKey(), it.getValue(), (q1, q2) -> q1-q2);
			if  (it.getValue().intValue() > 0) paymentRegister.depositMoney(it.getKey(), it.getValue());
		});
	}
}
