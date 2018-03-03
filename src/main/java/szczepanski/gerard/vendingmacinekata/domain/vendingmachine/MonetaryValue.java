package szczepanski.gerard.vendingmacinekata.domain.vendingmachine;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Value object that represents monetary value in system
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MonetaryValue {

    private final BigDecimal value;
    private Currency currency;

    public static MonetaryValue of(BigDecimal value, Currency currency) {
        checkNotNull(value);
        checkNotNull(currency);
        checkState(value.intValue() >= 0, "MonetaryValue must be positive!");

        return new MonetaryValue(value, currency);
    }

}
