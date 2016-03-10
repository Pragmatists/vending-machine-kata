package tdd.vendingMachine;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Product {
    DIET_COKE("Diet Coke 0.25L", new BigDecimal("2.50")),
    KITKAT("Kitkat chocolate bar", new BigDecimal("2.00")),
    ;

    private final String name;
    private final BigDecimal price;
}
