package tdd.vendingmachine.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
public class VendingMachineDto {
    private Set<ShelfDto> shelves;
    private Set<BigDecimal> acceptableDenominations;
}
