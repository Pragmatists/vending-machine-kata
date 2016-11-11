package tdd.vendingMachine.domain;

import org.junit.Test;
import tdd.vendingMachine.domain.state.States;
import tdd.vendingMachine.domain.state.currency.Coin;

import static org.junit.Assert.assertEquals;

public class VendingMachineTest {

    @Test
    public void should_set_up_new_vending_machine_in_base_state() {
        assertEquals(States.BASE, new VendingMachine().getState());
    }

    @Test
    public void should_be_in_product_selected_state_after_product_choice() {
        VendingMachine machine = new VendingMachine();
        machine.pressTraySelectionButton(1);

        assertEquals(States.TRAY_SELECTED, new VendingMachine().getState());
    }

    @Test
    public void should_be_in_coin_inserted_state_when_product_selected_and_coin_inserted() {
        VendingMachine machine = new VendingMachine();
        machine.pressTraySelectionButton(1);
        machine.insertCoin(new Coin());

        assertEquals(States.COIN_INSERTED, new VendingMachine().getState());
    }

    @Test
    //TODO - add inserted money return check if possible
    public void should_return_to_base_state_on_cancel_pressed() {
        VendingMachine machine = new VendingMachine();
        machine.pressTraySelectionButton(1);
        machine.insertCoin(new Coin());
        machine.pressCancelButton();

        assertEquals(States.BASE, new VendingMachine().getState());
    }

    @Test
    //TODO - add tray inserted check if needed? possible exposing internal components
    public void should_not_allow_to_change_product_when_money_inserted() {
        VendingMachine machine = new VendingMachine();
        machine.pressTraySelectionButton(1);
        machine.insertCoin(new Coin());
        machine.pressTraySelectionButton(2);

        assertEquals(States.COIN_INSERTED, new VendingMachine().getState());
    }

    @Test
    //TODO - add product dispensed check
    public void should_dispense_product_and_be_back_in_base_state_when_correct_amount_of_money_inserted() {
        VendingMachine machine = new VendingMachine();
        machine.pressTraySelectionButton(1);
        machine.insertCoin(new Coin());
        machine.insertCoin(new Coin());
        machine.insertCoin(new Coin());
        machine.insertCoin(new Coin());

        assertEquals(States.BASE, new VendingMachine().getState());
    }

    @Test
    //TODO - add product not dispensed and money returned check
    public void should_not_dispense_product_give_back_money_and_get_back_to_base_state_when_unable_to_give_change() {
        VendingMachine machine = new VendingMachine();
        machine.pressTraySelectionButton(1);
        machine.insertCoin(new Coin());
        machine.insertCoin(new Coin());
        machine.insertCoin(new Coin());
        machine.insertCoin(new Coin());

        assertEquals(States.BASE, new VendingMachine().getState());
    }
}
