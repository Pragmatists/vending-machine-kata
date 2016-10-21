package vendingmachine.calc;

public class ReturningChangePossibilityResult {

	private boolean isReturningChangePossible;
	private int[] coinsToReturn;

	public ReturningChangePossibilityResult(boolean isReturningChangePossible, int[] coinsToReturn) {
		super();
		this.isReturningChangePossible = isReturningChangePossible;
		this.coinsToReturn = coinsToReturn;
	}

	public boolean isReturningChangePossible() {
		return isReturningChangePossible;
	}

	public void setReturningChangePossible(boolean isReturningChangePossible) {
		this.isReturningChangePossible = isReturningChangePossible;
	}

	public int[] getCoinsToReturn() {
		return coinsToReturn;
	}

	public void setCoinsToReturn(int[] coinsToReturn) {
		this.coinsToReturn = coinsToReturn;
	}

}
