package tdd.vendingMachine;

import com.google.inject.Singleton;
import tdd.vendingMachine.dto.Coin;

import java.util.ArrayList;
import java.util.List;

@Singleton
class ChangeDispenserImpl implements ChangeDispenser {

    private final List<Coin> change = new ArrayList<>();

    @Override
    public void ejectChange(List<Coin> change) {
        this.change.addAll(change);
    }

    @Override
    public void ejectChange(Coin coin) {
        this.change.add(coin);
    }

    @Override
    public List<Coin> retrieveCoins() {
        List<Coin> change = new ArrayList<>(this.change);
        this.change.clear();
        return change;
    }
}
