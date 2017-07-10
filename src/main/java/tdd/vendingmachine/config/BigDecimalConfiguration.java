package tdd.vendingmachine.config;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalConfiguration {
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * {@link java.math.BigDecimal#equals(Object) BigDecimal.equals()} method checks for scale equality, not only value.
     * Thus, each {@link java.math.BigDecimal BigDecimal} used in application has to have the same scale
     * and rounding mode in order to compare them properly.
     */
    public static BigDecimal trimToDefaultScale(BigDecimal bigDecimal) {
        return bigDecimal.setScale(SCALE, ROUNDING_MODE);
    }
}
