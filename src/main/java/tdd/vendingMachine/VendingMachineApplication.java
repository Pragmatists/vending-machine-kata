package tdd.vendingMachine;

import tdd.vendingMachine.domain.VendingMachine;
import tdd.vendingMachine.domain.money.Coins;
import tdd.vendingMachine.util.Filler;

public class VendingMachineApplication {

    public static void main(String[] args) {
        //Example use

        VendingMachine machine = new VendingMachine();
        //Fill machine with coins and products
        Filler.fill(machine, 10);

        machine.pressTraySelectionButton(0);
        machine.insertCoin(Coins.COIN_1);
        machine.insertCoin(Coins.COIN_1);
        //Voila, you have your product

        machine.pressTraySelectionButton(0);
        machine.insertCoin(Coins.COIN_1);
        machine.pressCancelButton();
        //back to idle state

        machine.pressTraySelectionButton(0);
        machine.insertCoin(Coins.COIN_5);
        //you have your product and change

        machine.pressTraySelectionButton(0);
        machine.insertCoin(Coins.COIN_5);

        //etc...
    }
}
