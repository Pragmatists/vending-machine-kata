package tdd.vendingMachine.parts;

import java.util.Map;

import tdd.vendingMachine.enumeration.CoinsEnum;
import tdd.vendingMachine.services.CoinsRegisterMapFactory;

public class PaymentRegister extends CoinRegister {
	
	public PaymentRegister(Map<CoinsEnum, Integer> coinsRegisterMap) {
		super(coinsRegisterMap);
	}

	
	public void depositMoney(CoinsEnum coin, Integer quantity) {
		assert(quantity > 0);
		coinsRegisterMap.merge(coin, quantity, (q1,q2) -> q1+q2);
	}

	public void cancel() {
		System.out.println("Transaction was cancelled. Money was returned to client");
		coinsRegisterMap = CoinsRegisterMapFactory.getEmptytCoinRegister();
	}
	
	public Map<CoinsEnum, Integer> flush() {
		System.out.println("Transaction accepted");
		Map<CoinsEnum, Integer> result = coinsRegisterMap;
		coinsRegisterMap = CoinsRegisterMapFactory.getEmptytCoinRegister();
		return result;
	}
}
