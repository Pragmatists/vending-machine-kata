package vendingmachine.core;

import java.math.BigDecimal;

import vendingmachine.config.xsd.classes.ProductQuantitiesOnShelfType;
import vendingmachine.config.xsd.classes.ProductsInVendingMachine;
import vendingmachine.config.xsd.classes.ProductsOnFirstShelfType;
import vendingmachine.config.xsd.classes.ProductsOnFourthShelfType;
import vendingmachine.config.xsd.classes.ProductsOnSecondShelfType;
import vendingmachine.config.xsd.classes.ProductsOnThirdShelfType;
import vendingmachine.model.Product;

public class ConfigToModelAssembler {

	public void assemble(ProductsInVendingMachine productsInVendingMachine, Product[][] products) {

		ProductsOnFirstShelfType productsOnFirstShelf = productsInVendingMachine.getProductsOnFirstShelf();
		assembleShelf(products, 0, productsOnFirstShelf.getProductType().getPrice(), productsOnFirstShelf.getProductType().getName(),
				productsOnFirstShelf.getProductQuantitiesOnShelf());

		ProductsOnSecondShelfType productsOnSecondShelf = productsInVendingMachine.getProductsOnSecondShelf();
		assembleShelf(products, 1, productsOnSecondShelf.getProductType().getPrice(), productsOnSecondShelf.getProductType().getName(),
				productsOnSecondShelf.getProductQuantitiesOnShelf());

		ProductsOnThirdShelfType productsOnThirdShelf = productsInVendingMachine.getProductsOnThirdShelf();
		assembleShelf(products, 2, productsOnThirdShelf.getProductType().getPrice(), productsOnThirdShelf.getProductType().getName(),
				productsOnThirdShelf.getProductQuantitiesOnShelf());

		ProductsOnFourthShelfType productsOnFourthShelf = productsInVendingMachine.getProductsOnFourthShelf();
		assembleShelf(products, 3, productsOnFourthShelf.getProductType().getPrice(), productsOnFourthShelf.getProductType().getName(),
				productsOnFourthShelf.getProductQuantitiesOnShelf());

	}

	private void assembleShelf(Product[][] products, int i, BigDecimal productTypePrice, String productTypeName, ProductQuantitiesOnShelfType productQuantitiesOnShelf) {
		products[i][0] = createProduct(productTypePrice, productTypeName, productQuantitiesOnShelf.getFirstSlotQuantity());
		products[i][1] = createProduct(productTypePrice, productTypeName, productQuantitiesOnShelf.getSecondSlotQuantity());
		products[i][2] = createProduct(productTypePrice, productTypeName, productQuantitiesOnShelf.getThirdSlotQuantity());
		products[i][3] = createProduct(productTypePrice, productTypeName, productQuantitiesOnShelf.getFourthSlotQuantity());
		products[i][4] = createProduct(productTypePrice, productTypeName, productQuantitiesOnShelf.getFifthSlotQuantity());
		products[i][5] = createProduct(productTypePrice, productTypeName, productQuantitiesOnShelf.getSixthSlotQuantity());

	}

	private Product createProduct(BigDecimal price, String name, int quantity) {
		return new Product(price, name, quantity);
	}

}
