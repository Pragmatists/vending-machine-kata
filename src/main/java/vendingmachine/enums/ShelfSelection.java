package vendingmachine.enums;

public enum ShelfSelection {
	// @formatter:off
	NONE(-1),
	FIRST(0),
	SECOND(1),
	THIRD(2), 
	FOURTH(3);
	
	private final int code;

	private ShelfSelection(int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	
}
