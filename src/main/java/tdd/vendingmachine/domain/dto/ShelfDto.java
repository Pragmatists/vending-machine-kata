package tdd.vendingmachine.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "shelfNumber")
public class ShelfDto {
    private String shelfNumber;
    private ProductTypeDto productType;
    private int productCount;
}
