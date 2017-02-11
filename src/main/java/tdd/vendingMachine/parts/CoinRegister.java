package tdd.vendingMachine.parts;

import java.util.Map;

import tdd.vendingMachine.enumeration.CoinsEnum;

public abstract class CoinRegister {
	
	protected Map<CoinsEnum, Integer> coinsRegisterMap;
	
	public CoinRegister(Map<CoinsEnum, Integer> coinsRegisterMap) {
		this.coinsRegisterMap = coinsRegisterMap;
	}
	
	public Map<CoinsEnum, Integer> getCoinsRegisterMap() {
		return coinsRegisterMap;
	}
	
//	public void setCoinsRegisterMap(Map<CoinsEnum, Integer> map) {
//		this.coinsRegisterMap = map;
//	}
}
