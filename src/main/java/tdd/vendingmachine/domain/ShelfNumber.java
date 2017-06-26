package tdd.vendingmachine.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;

@EqualsAndHashCode
@ToString
class ShelfNumber {

    private final String number;

    ShelfNumber(String number) {
        this.number = Objects.requireNonNull(number);
    }

    String print() {
        return number;
    }
}
