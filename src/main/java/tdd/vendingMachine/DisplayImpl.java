package tdd.vendingMachine;

import com.google.inject.Singleton;

import java.math.BigDecimal;

@Singleton
class DisplayImpl implements Display {

    private final String welcomeMessage = "Welcome! Please enter shelf number.";
    private BigDecimal productPrice = null;
    private String warning = null;

    @Override
    public String view() {
        if (this.productPrice == null && this.warning == null) {
            return this.welcomeMessage;
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (this.productPrice != null) {
            stringBuilder.append("Product remaining price: ");
            stringBuilder.append(this.productPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN));
            stringBuilder.append("\n");
        }
        if (this.warning != null) {
            stringBuilder.append("Warning: ");
            stringBuilder.append(this.warning);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public void writeProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public void writeWarning(String warning) {
        this.warning = warning;
    }

    @Override
    public void reset() {
        this.productPrice = null;
        this.warning = null;
    }
}
