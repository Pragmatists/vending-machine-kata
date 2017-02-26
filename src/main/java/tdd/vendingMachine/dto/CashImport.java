package tdd.vendingMachine.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;

/**
 * @author Agustin on 2/19/2017.
 * @since 1.0
 */
public class CashImport {

    private final String label;
    private final int amount;

    @JsonCreator
    public CashImport(@NonNull @JsonProperty("label") String label, @JsonProperty("amount") int amount) {
        validateCashImport(label, amount);
        this.label = label;
        this.amount = amount;
    }

    private static void validateCashImport(String label, int amount) {
        if (StringUtils.isEmpty(label)) {
            throw new NullPointerException("The given amount must be non-empty");
        }
        if (amount < 0) {
            throw new InputMismatchException("The amount given must be positive: " + amount);
        }
    }

    public int getAmount() {
        return amount;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Creates a new cash import with the value accumulated from the given amount
     * @param cashImport the cashImport to accumulate with (must be of the same label)
     * @return a new cash import of the same label with the accumulated amount
     * @throws NoSuchElementException in case the label of the given import differs
     */
    public CashImport accumulate(@NonNull CashImport cashImport) throws NoSuchElementException{
        if (!cashImport.getLabel().equals(this.label)) {
            throw new NoSuchElementException("The given import label can not accumulate since label is different");
        }
        return new CashImport(label, this.amount + cashImport.getAmount());

    }
}
