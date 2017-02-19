package tdd.vendingMachine.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Agustin on 2/19/2017.
 * @since 1.0
 */
public class CashImport {

    private final String label;
    private final int amount;

    @JsonCreator
    public CashImport(@JsonProperty("label") String label, @JsonProperty("amount") int amount) {
        this.label = label;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public String getLabel() {
        return label;
    }
}
