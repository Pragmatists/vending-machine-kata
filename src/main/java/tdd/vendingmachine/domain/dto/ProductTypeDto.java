package tdd.vendingmachine.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductTypeDto {
    private String name;
    private PriceDto price;
}
