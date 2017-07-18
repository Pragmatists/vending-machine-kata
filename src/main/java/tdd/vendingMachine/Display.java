package tdd.vendingMachine;

import java.math.BigDecimal;

interface Display {
    /**
     * Not using toString on purpose. Prefer to use toString for internal logging, rather than for business cases.
     *
     * @return
     */
    String view();

    void writeProductPrice(BigDecimal productPrice);

    void writeWarning(String warning);

    void reset();
}
