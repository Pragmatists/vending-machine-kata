package tdd.vendingMachine.domain;

import tdd.vendingMachine.domain.state.State;
import tdd.vendingMachine.domain.state.States;
import tdd.vendingMachine.domain.state.currency.Coin;

public class VendingMachine {

    private State state;

    private MoneyBox moneyBox;
    private ProductBox productBox;
    private Display display;

    private Integer selectedTray;

    public VendingMachine() {
        setState(States.BASE);

        setSelectedTray(null);
    }

    public State getState() {
        return state;
    }

    public VendingMachine setState(State state) {
        this.state = state;

        return this;
    }

    public MoneyBox getMoneyBox() {
        return moneyBox;
    }

    public VendingMachine setMoneyBox(MoneyBox moneyBox) {
        this.moneyBox = moneyBox;
        return this;
    }

    public ProductBox getProductBox() {
        return productBox;
    }

    public VendingMachine setProductBox(ProductBox productBox) {
        this.productBox = productBox;
        return this;
    }

    public Display getDisplay() {
        return display;
    }

    public VendingMachine setDisplay(Display display) {
        this.display = display;
        return this;
    }

    public Integer getSelectedTray() {
        return selectedTray;
    }

    public VendingMachine setSelectedTray(Integer selectedTray) {
        this.selectedTray = selectedTray;
        return this;
    }

    public void pressTraySelectionButton(int trayNo) {

    }

    public void pressCancelButton() {

    }

    public void insertCoin(Coin coin) {

    }
}
