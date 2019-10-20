package tdd.vendingMachine.shelf;

import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.product.ProductType;

import java.util.List;

public interface IShelf extends List<Product> {
    Product push(Product product);

    Product pop();

    void setProductsType(ProductType productsType) throws CannotChangeShelfProductsTypeException;

    Integer getNumber();

    ProductType getProductsType();
}
