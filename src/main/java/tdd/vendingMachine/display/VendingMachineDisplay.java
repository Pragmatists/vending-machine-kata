package tdd.vendingMachine.display;

import tdd.vendingMachine.domain.Money;

public interface VendingMachineDisplay {

    String getDisplayableMessage();

    void displayText(String text);

    void displayMoney(Money money);

    void clear();

}
