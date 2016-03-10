package tdd.vendingMachine;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Coin {
    COIN_5(new BigDecimal("5")),
    COIN_2(new BigDecimal("2")),
    COIN_1(BigDecimal.ONE),
    COIN_0_5(new BigDecimal("0.5")),
    COIN_0_2(new BigDecimal("0.2")),
    COIN_0_1(new BigDecimal("0.1")),
    ;

    private final BigDecimal denomination;

    public static Optional<Coin> fromDenomination(String denomination) {
        return Stream.of(values())
            .filter(c -> c.denomination.toString().equals(denomination))
            .findFirst();
    }
}
