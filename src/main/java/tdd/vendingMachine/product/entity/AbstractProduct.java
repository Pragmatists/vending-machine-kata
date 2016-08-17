package tdd.vendingMachine.product.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joda.money.Money;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.product.quantity.ProductQuantity;

import static lombok.AccessLevel.PACKAGE;

@Getter
@AllArgsConstructor(access = PACKAGE)
public abstract class AbstractProduct implements Product {

	private String name;

	private Money price;

	private ProductQuantity quantity;

}
