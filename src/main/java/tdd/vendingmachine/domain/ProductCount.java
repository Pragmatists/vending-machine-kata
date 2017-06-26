package tdd.vendingmachine.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
class ProductCount {

    private final int count;

    ProductCount(int count) {
        assertCountGreaterThanZero(count);
        this.count = count;
    }

    private static void assertCountGreaterThanZero(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Product count must be greater than 0, but is: " + count);
        }
    }

    boolean isZero() {
        return count == 0;
    }
}
