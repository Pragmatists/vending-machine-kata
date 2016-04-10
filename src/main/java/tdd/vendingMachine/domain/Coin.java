package tdd.vendingMachine.domain;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

public enum Coin {
    FIVE(Money.of("5")),
    TWO(Money.of("2")),
    ONE(Money.of("1")),
    HALF(Money.of("0.5")),
    TWENTY(Money.of("0.2")),
    DIME(Money.of("0.1")),;

    Coin(Money aMoney) {
        this.money = aMoney;
    }

    private final Money money;

    public Coins nth(int n) {
        return Coins.of(Collections.nCopies(n, this));
    }

    public Coins nth(Money aMoney) {
        if (aMoney.compareTo(Money.ZERO) == 0) {
            return Coins.empty();
        }
        if (aMoney.compareTo(money) == 0) {
            return Coins.of(this);
        }
        List<Coin> resultCoins = Lists.newArrayList();
        Money moneyOfCoins = Money.ZERO;
        while ((moneyOfCoins = moneyOfCoins.plus(money)).compareTo(aMoney) <= 0) {
            resultCoins.add(this);
        }
        return Coins.of(resultCoins);
    }

    public Money asMoney() {
        return money;
    }

}
