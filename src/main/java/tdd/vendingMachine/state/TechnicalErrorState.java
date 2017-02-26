package tdd.vendingMachine.state;

import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.view.VendingMachineMessages;

/**
 * @author Agustin on 2/26/2017.
 * @since 1.0
 * State describing that vending machine requires technical assistance
 */
public class TechnicalErrorState extends State {

    public TechnicalErrorState(VendingMachine vendingMachine) {
        super(vendingMachine, false);
    }

    @Override
    public void insertCoin(Coin coin) {
        vendingMachine.showMessageOnDisplay(
            VendingMachineMessages.buildWarningMessageWithSubject(VendingMachineMessages.CASH_NOT_ACCEPTED_MACHINE_TECHNICAL_ERROR.label, coin.denomination));
        vendingMachine.showMessageOnDisplay(VendingMachineMessages.buildWarningMessageWithoutSubject(VendingMachineMessages.TECHNICAL_ERROR.label));
    }

    @Override
    public void selectShelfNumber(int shelfNumber) {
        vendingMachine.showMessageOnDisplay(VendingMachineMessages.buildWarningMessageWithoutSubject(VendingMachineMessages.TECHNICAL_ERROR.label));
    }

    @Override
    public void cancel() {
        vendingMachine.showMessageOnDisplay(VendingMachineMessages.buildWarningMessageWithoutSubject(VendingMachineMessages.TECHNICAL_ERROR.label));
    }
}
