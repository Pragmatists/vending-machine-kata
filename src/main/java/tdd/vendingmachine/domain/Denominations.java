package tdd.vendingmachine.domain;

import lombok.ToString;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
class Denominations {

    private final Set<Denomination> denominations;

    private Denominations(Set<Denomination> denominations) {
        this.denominations = Collections.unmodifiableSet(Objects.requireNonNull(denominations));
    }

    static Denominations create(Set<BigDecimal> acceptableDenominations) {
        return new Denominations(acceptableDenominations.stream()
                                                        .map(Denomination::create)
                                                        .collect(Collectors.toSet()));
    }

    boolean contains(Denomination denomination) {
        return denominations.contains(denomination);
    }
}
