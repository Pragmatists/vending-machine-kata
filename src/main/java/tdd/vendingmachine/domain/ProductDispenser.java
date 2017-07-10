package tdd.vendingmachine.domain;

import com.google.common.collect.ImmutableList;
import lombok.ToString;
import tdd.vendingmachine.domain.dto.ProductDto;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ToString
class ProductDispenser {

    private List<ProductType> productTypes;

    private ProductDispenser(List<ProductType> productTypes) {
        this.productTypes = Collections.unmodifiableList(Objects.requireNonNull(productTypes));
    }

    static ProductDispenser empty() {
        return new ProductDispenser(Collections.emptyList());
    }

    ProductDispenser put(ProductType productType) {
        return new ProductDispenser(ImmutableList.<ProductType>builder()
            .addAll(productTypes)
            .add(productType)
            .build());
    }

    Collection<ProductDto> dispense() {
        return productTypes.stream()
                           .map(ProductType::toDto)
                           .collect(Collectors.toList());
    }
}
