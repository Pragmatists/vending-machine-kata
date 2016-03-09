package tdd.vendingMachine;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Product {
    DIET_COKE("Diet Coke 0.25L"),
    KITKAT("Kitkat chocolate bar"),
    ;

    private final String name;
}
