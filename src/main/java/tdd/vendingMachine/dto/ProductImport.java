package tdd.vendingMachine.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Agustin on 2/19/2017.
 * @since 1.0
 */
public class ProductImport {

    private final String type;
    private final double price;
    private final int itemCount;

    @JsonCreator
    public ProductImport(@JsonProperty("type") String type,
                         @JsonProperty("price") double price,
                         @JsonProperty("itemCount") int itemCount) {
        this.type = type;
        this.price = price;
        this.itemCount = itemCount;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public int getItemCount() {
        return itemCount;
    }
}
