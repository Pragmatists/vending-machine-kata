package vendingmachine.model;

import java.math.BigDecimal;

public class VendingMachineConstants {

	public static final int SHELVES_NR = 4;
	public static final int PRODUCTS_PER_SHELF_NR = 6;

	public static final int DENOMINATIONS_NR = 6;

	// @formatter:off
	public static final BigDecimal[] COIN_DENOMINATION_VALUES = new BigDecimal[]{
		new BigDecimal("5.0"),	
		new BigDecimal("2.0"),	
		new BigDecimal("1.0"),	
		new BigDecimal("0.5"),	
		new BigDecimal("0.2"),	
		new BigDecimal("0.1"),	
	};
	
}
