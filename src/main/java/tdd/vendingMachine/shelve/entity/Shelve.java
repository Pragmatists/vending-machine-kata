package tdd.vendingMachine.shelve.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import tdd.vendingMachine.product.Product;

@AllArgsConstructor(staticName = "of")
public class Shelve {

	@Getter
	private Product product;

	@Getter
	@Setter
	private int quantity;

}
