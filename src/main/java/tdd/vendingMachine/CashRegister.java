package tdd.vendingMachine;

import tdd.vendingMachine.dto.Coin;

import java.math.BigDecimal;

interface CashRegister {
    void setProductPrice(BigDecimal productPrice);

    boolean insertCoin(Coin coin);

    void reset();
}
