package vendingmachine.utils;

import vendingmachine.enums.CoinDenomination;

public class MessagesHelper {

	public static String createReturningCoinsVector(int[] coinsToReturn) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < coinsToReturn.length; ++i) {
			if (coinsToReturn[i] != 0) {
				sb.append(coinsToReturn[i] + " x " + CoinDenomination.getByCode(i).getValue() + ",");
			}
		}
		sb.setLength(sb.toString().length() - 1);
		return sb.toString();
	}
}
