package tdd.vendingmachine.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class VendingMachineDto {
    private Set<ShelfDto> shelves;
}
