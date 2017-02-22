package tdd.vendingMachine.util;

import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;

/**
 * @author Agustin on 2/19/2017.
 * @since 1.0
 */
public class Constants {

    public static final double ACCURACY = 0.0001;
    public static final double SUM_DOUBLE_IDENTITY = 0.0;
    public static final DoubleBinaryOperator SUM_DOUBLE_BINARY_OPERATOR = (left, right) -> left + right;
    public static final Integer SUM_INT_IDENTITY = 0;
    public static final IntBinaryOperator SUM_INT_BINARY_OPERATOR = (left, right) -> left + right;
}
