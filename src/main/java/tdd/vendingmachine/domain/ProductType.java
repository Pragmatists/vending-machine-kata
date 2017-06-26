package tdd.vendingmachine.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import tdd.vendingmachine.domain.dto.ProductTypeDto;

import java.util.Objects;

@EqualsAndHashCode
@ToString
class ProductType {

    private final String name;
    private final Price price;

    private ProductType(String name, Price price) {
        this.name = name;
        this.price = price;
    }

    static ProductType create(ProductTypeDto productTypeDto) {
        String name = Objects.requireNonNull(productTypeDto.getName());
        Price price = Price.create(productTypeDto.getPrice());
        return new ProductType(name, price);
    }

    Price price() {
        return price;
    }
}
