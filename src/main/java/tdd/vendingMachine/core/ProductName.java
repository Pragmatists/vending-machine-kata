package tdd.vendingMachine.core;

public class ProductName {

    private final String value;

    private ProductName(String value) {
        this.value = value;
    }

    public static ProductName valueOf(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Product name should not be empty");
        }

        return new ProductName(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ProductName that = (ProductName) obj;
        return value != null ? value.equals(that.value) : that.value == null;

    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    public String value() {
        return value;
    }
}
