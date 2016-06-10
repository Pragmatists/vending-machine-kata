package tdd.vendingMachine.core;

public class ProductName {

    private final String value;

    private ProductName(String value) {
        this.value = value;
    }

    public static ProductName valueOf(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalProductNameException("Product name should not be empty");
        }

        return new ProductName(value);
    }
}
