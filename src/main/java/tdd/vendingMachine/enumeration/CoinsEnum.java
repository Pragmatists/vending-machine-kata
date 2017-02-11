package tdd.vendingMachine.enumeration;

import java.math.BigDecimal;

public enum CoinsEnum {

	FIVE("5.0"),
    TWO("2.0"), 
    ONE("1.0"),
    HALF("0.5"),
    POINT_TWO("0.2"),
    POINT_ONE("0.1");
 
	private BigDecimal value;
 
    private CoinsEnum(String value) {
    	this.value = new BigDecimal(value);
    }
    
    public BigDecimal getValue() {
    	return this.value;
    }
}
