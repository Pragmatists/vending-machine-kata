package tdd.vendingMachine;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import tdd.vendingMachine.dto.Coin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

@Singleton
class CashRegisterImpl implements CashRegister {
    private BigDecimal productPrice = null;
    private final EnumMap<Coin, Integer> coins = new EnumMap<>(Coin.class);
    private final List<Coin> coinOrdering;
    private final List<Coin> sessionCoins = new ArrayList<>();
    private final Display display;
    private final ChangeDispenser changeDispenser;

    @Inject
    CashRegisterImpl(Display display, ChangeDispenser changeDispenser) {
        this.display = display;
        this.changeDispenser = changeDispenser;

        for (Coin coin : Coin.values()) {
            this.coins.put(coin, 0);
        }

        // Create the ordering of coins in order from biggest worth to smallest.
        this.coinOrdering = new ArrayList<>(this.coins.keySet());
        this.coinOrdering.sort((coin1, coin2) -> coin2.getValue().compareTo(coin1.getValue()));
    }

    @Override
    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
        this.display.reset();
        this.display.writeProductPrice(productPrice);
    }

    @Override
    public boolean insertCoin(Coin coin) {
        if (this.productPrice == null) {
            this.display.writeWarning("Please choose product.");
            this.changeDispenser.ejectChange(coin);
            return false;
        }
        BigDecimal remainingCost = this.productPrice.subtract(coin.getValue());
        int comparison = remainingCost.compareTo(BigDecimal.ZERO);
        if (comparison == 0) {
            // Inserted just the right amount
            this.productPrice = null;
            this.coins.compute(coin, (coin1, integer) -> ++integer);
            this.sessionCoins.clear();
            this.display.reset();
            return true;
        } else if (comparison > 0) {
            // Inserted not enough yet
            this.productPrice = remainingCost;
            this.coins.compute(coin, (coin1, integer) -> ++integer);
            this.sessionCoins.add(coin);
            this.display.writeProductPrice(this.productPrice);
            return false;
        } else {
            // Inserted more than needed
            this.coins.compute(coin, (coin1, integer) -> ++integer);
            this.sessionCoins.add(coin);
            List<Coin> change = this.calculateCoinChange(remainingCost.negate());
            if (change.isEmpty()) {
                // Not enough coins for the change.
                this.display.writeWarning("The change can not be returned. Please take the change and enter new shelf number.");
                this.changeDispenser.ejectChange(new ArrayList<>(this.sessionCoins));
                this.sessionCoins.clear();
                this.productPrice = null;
                return false;
            } else {
                // Dispense the change.
                this.changeDispenser.ejectChange(change);
                this.productPrice = null;
                this.display.reset();
                this.sessionCoins.clear();
                return true;
            }
        }
    }

    @Override
    public void reset() {
        this.productPrice = null;
        this.changeDispenser.ejectChange(new ArrayList<>(this.sessionCoins));
        this.sessionCoins.clear();
        this.display.reset();
    }

    private List<Coin> calculateCoinChange(BigDecimal change) {
        List<Coin> coins = new ArrayList<>();
        BigDecimal remainingChange = change;
        for (Coin coin : this.coinOrdering) {
            Integer coinNumber = this.coins.get(coin);
            if (coinNumber > 0) {
                int coinNumberNeeded = remainingChange.divide(coin.getValue(), BigDecimal.ROUND_DOWN).intValue();
                if (coinNumberNeeded > 0) {
                    if (coinNumber < coinNumberNeeded) {
                        // There is fewer coins in the register than needed.
                        remainingChange = remainingChange.subtract(BigDecimal.valueOf(coinNumber).multiply(coin.getValue()));
                        coins.addAll(Collections.nCopies(coinNumber, coin));
                        this.coins.put(coin, 0);
                    } else {
                        // There is enough coins in the register.
                        remainingChange = remainingChange.subtract(BigDecimal.valueOf(coinNumberNeeded).multiply(coin.getValue()));
                        coins.addAll(Collections.nCopies(coinNumberNeeded, coin));
                        this.coins.put(coin, coinNumber - coinNumberNeeded);
                    }
                    if (remainingChange.compareTo(BigDecimal.ZERO) == 0) {
                        return coins;
                    }
                }
            }
        }
        return Collections.emptyList();
    }
}
