package tdd.vendingMachine.model.builder;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.StringUtils;

import tdd.vendingMachine.model.Product;

public class ProductBuilder implements IBuilder<Product> {
    private BigDecimal price;
    private String name;
    
    public ProductBuilder withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
    
    public ProductBuilder withPrice(String price) {
        this.price = StringUtils.isEmpty(price) ? null : new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
        return this;
    }

    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }
    
    
    @Override
    public Product build() {
        Product product = new Product();
        product.setName(this.name);
        product.setPrice(this.price);
        
        return product;
    }
    
}
