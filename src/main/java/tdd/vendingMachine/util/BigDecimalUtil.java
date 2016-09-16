package tdd.vendingMachine.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDecimalUtil {
    public static final int REQUIRED_VALUE_PRECISION = 2;

    public static BigDecimal createBigDecimalWithPrecision(Double valueToReturn) {
        return new BigDecimal(valueToReturn).round(new MathContext(REQUIRED_VALUE_PRECISION, RoundingMode.HALF_UP));
    }
}
