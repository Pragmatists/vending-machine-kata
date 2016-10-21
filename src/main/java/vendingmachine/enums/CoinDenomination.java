package vendingmachine.enums;

public enum CoinDenomination {

	// @formatter:off
	DENOMINATION_5(0, "5.0"),
	DENOMINATION_2(1, "2.0"),
	DENOMINATION_1(2, "1.0"),
	DENOMINATION_05(3, "0.5"),
	DENOMINATION_02(4, "0.2"),
	DENOMINATION_01(5, "0.1");

	private final int code;
	private final String value;
	
	private CoinDenomination(int code, String value) {
		this.code = code;
		this.value = value;
	}

	public int getCode() {
		return code;
	}
	
	public String getValue() {
		return value;
	}

	public static CoinDenomination getByCode(int code){
		switch(code){
			case 0:
			return DENOMINATION_5;
			case 1:
			return DENOMINATION_2;
			case 2:
			return DENOMINATION_1;
			case 3:
			return DENOMINATION_05;
			case 4:
			return DENOMINATION_02;
			case 5:
			return DENOMINATION_01;
		}
		
		return null;
	}
	
}
