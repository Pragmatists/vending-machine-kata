package tdd.vendingmachine.domain;

import lombok.ToString;
import tdd.vendingmachine.domain.dto.PriceDto;

import java.math.BigDecimal;
import java.util.Objects;

@ToString
class Price {

    private final BigDecimal value;

    private Price(BigDecimal value) {
        this.value = value;
    }

    /**
     * Cannot use Lombok, because {@link java.math.BigDecimal#equals(Object) BigDecimal.equals()} method checks for
     * scale equality, not only value.
     * Thus, generating equals with {@link java.math.BigDecimal#compareTo(Object) BigDecimal.compareTo()}.
      */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Price)) {
            return false;
        }

        Price price = (Price) o;

        return value.compareTo(price.value) == 0;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    static Price create(PriceDto priceDto) {
        BigDecimal value = Objects.requireNonNull(priceDto.getValue());
        return new Price(value);
    }

    BigDecimal value() {
        return value;
    }
}
