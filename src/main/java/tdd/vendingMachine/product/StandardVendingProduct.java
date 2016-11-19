package tdd.vendingMachine.product;

public class StandardVendingProduct implements VendingProduct {

	private float price;
	private String type;

	public StandardVendingProduct(float price, String type) {
		this.price = price;
		this.type = type;
	}

	@Override
	public float getProductPrice() {
		return price;
	}

	@Override
	public String getProductType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof StandardVendingProduct)) {
			return false;
		}
		StandardVendingProduct other = (StandardVendingProduct) obj;
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		return true;
	}
}
