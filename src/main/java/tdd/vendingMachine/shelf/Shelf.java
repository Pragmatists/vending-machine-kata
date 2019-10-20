package tdd.vendingMachine.shelf;

import lombok.Getter;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.product.ProductType;

import java.util.Stack;

public class Shelf extends Stack<Product> implements IShelf {

    @Getter
    ProductType productsType;
    @Getter
    private Integer number;

    public Shelf(int number) {
        this.number = number;
    }

    @Override
    public Product push(Product product) {
        if (productsType.equals(product.getType())) {
            return super.push(product);
        }
        return null;
    }

    @Override
    public void setProductsType(ProductType productsType) throws CannotChangeShelfProductsTypeException {
        if (!isEmpty() && productsType != this.productsType) {
            throw new CannotChangeShelfProductsTypeException();
        }
        this.productsType = productsType;
    }

}
