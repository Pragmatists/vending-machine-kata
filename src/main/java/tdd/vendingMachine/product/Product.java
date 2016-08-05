package tdd.vendingMachine.product;

import org.joda.money.Money;
import tdd.vendingMachine.product.quantity.ProductQuantity;

public interface Product {

	String getName();

	Money getPrice();

	ProductQuantity getQuantity();

}
