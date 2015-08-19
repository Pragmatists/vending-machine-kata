package tdd.vendingMachine;

/**
 * @author macko
 * @since 2015-08-19
 */
public class ManyProductsOnOneShelfException extends RuntimeException {
    public ManyProductsOnOneShelfException() {
        super("Only one product type can be placed on one shelf");
    }
}
