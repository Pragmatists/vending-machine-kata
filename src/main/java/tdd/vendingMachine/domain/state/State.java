package tdd.vendingMachine.domain.state;

import tdd.vendingMachine.domain.VendingMachine;
import tdd.vendingMachine.domain.currency.Coins;

public interface State {

    public State traySelected(VendingMachine context, int trayNo);

    public State cancelSelected(VendingMachine context);

    public State coinInserted(VendingMachine context, Coins coin);
}
