package tdd.vendingMachine.Service;

import lombok.Data;

/**
 * Verbal representation of the shelf carried to the view
 */

@Data
public class ShelfTransferObject {
    Integer shelfnumber;
    String productname;
    Integer price;

    public ShelfTransferObject() {
    }

    public ShelfTransferObject(Integer shelfnumber, String productname, Integer price) {
        this.shelfnumber = shelfnumber;
        this.productname = productname;
        this.price = price;
    }
}
