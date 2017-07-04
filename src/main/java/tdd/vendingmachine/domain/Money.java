package tdd.vendingmachine.domain;

import java.math.BigDecimal;

interface Money {

    Money add(Money money);

    Money subtract(Money money);

    BigDecimal value();
}
