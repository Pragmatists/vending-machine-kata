package tdd.vendingMachine.domain;

import tdd.vendingMachine.domain.state.State;
import tdd.vendingMachine.domain.state.States;
import tdd.vendingMachine.domain.state.currency.Coin;

public class VendingMachine {

    private State state;

    private MoneyBox moneyBox;
    private ProductBox productBox;
    private Display display;

    public VendingMachine() {
        setState(States.BASE);
    }

    public State getState() {
        return state;
    }

    public VendingMachine setState(State state) {
        this.state = state;

        return this;
    }

    public void pressTraySelectionButton(int trayNo) {

    }

    public void pressCancelButton() {

    }

    public void insertCoin(Coin coin) {

    }
}
