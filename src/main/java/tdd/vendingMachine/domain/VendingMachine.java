package tdd.vendingMachine.domain;

import tdd.vendingMachine.domain.money.Coins;
import tdd.vendingMachine.domain.display.Display;
import tdd.vendingMachine.domain.money.MoneyBox;
import tdd.vendingMachine.domain.product.ProductBox;
import tdd.vendingMachine.domain.state.State;
import tdd.vendingMachine.domain.state.States;

public class VendingMachine {

    private State state;

    private MoneyBox moneyBox;
    private MoneyBox moneyBuffer;

    private ProductBox productBox;
    private Display display;

    private Integer selectedTray;

    public VendingMachine() {
        setState(States.BASE);

        setProductBox(new ProductBox());
        setMoneyBox(new MoneyBox());
        setMoneyBuffer(new MoneyBox());
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

    public MoneyBox getMoneyBuffer() {
        return moneyBuffer;
    }

    public VendingMachine setMoneyBuffer(MoneyBox moneyBuffer) {
        this.moneyBuffer = moneyBuffer;
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
        setState(state.traySelected(this, trayNo));
    }

    public void pressCancelButton() {
        setState(state.cancelSelected(this));
    }

    public void insertCoin(Coins coin) {
        setState(state.coinInserted(this, coin));
    }
}
