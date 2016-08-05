package tdd.vendingMachine.product.quantity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ProductQuantity {

	private ProductQuantityUnit unit;

	private double amount;

}
