package tdd.vendingMachine.domain;

import org.junit.Test;
import tdd.vendingMachine.domain.display.Messages;
import tdd.vendingMachine.domain.money.Coins;
import tdd.vendingMachine.domain.product.Products;
import tdd.vendingMachine.domain.state.States;
import tdd.vendingMachine.util.Filler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VendingMachineTest {

    private static int BASE_MONEY_AMOUNT = 880;
    private static int BASE_COCA_COLA_0_33_COUNT = 10;

    @Test
    public void should_set_up_new_vending_machine_in_base_state() {
        VendingMachine machine = new VendingMachine();

        assertEquals(States.BASE, machine.getState());
        assertEquals(Messages.IDLE.getMessage(), machine.getDisplay().getMessage());
    }

    @Test
    public void should_be_in_product_selected_state_after_product_choice() {
        VendingMachine machine = new VendingMachine();
        Filler.fill(machine, 10);

        machine.pressTraySelectionButton(0);

        assertEquals(Integer.valueOf(0), machine.getSelectedTray());
        assertEquals(States.TRAY_SELECTED, machine.getState());
        assertEquals(
            String.format(
                Messages.PRODUCT_SELECTED.getMessage(),
                Products.COCA_COLA_0_33.name(),
                (float) Products.COCA_COLA_0_33.getPrice() / 10
            ), machine.getDisplay().getMessage()
        );
    }

    @Test
    public void should_not_select_empty_tray() {
        VendingMachine machine = new VendingMachine();
        Filler.fill(machine, 0);

        machine.pressTraySelectionButton(0);

        assertNull(machine.getSelectedTray());
        assertEquals(States.BASE, machine.getState());
    }

    @Test
    public void should_not_change_tray_when_once_selected() {
        VendingMachine machine = new VendingMachine();
        Filler.fill(machine, 10);

        machine.pressTraySelectionButton(0);
        machine.pressTraySelectionButton(1);

        assertEquals(Integer.valueOf(0), machine.getSelectedTray());
        assertEquals(States.TRAY_SELECTED, machine.getState());
    }

    @Test
    public void should_be_back_in_base_state_on_cancel() {
        VendingMachine machine = new VendingMachine();
        Filler.fill(machine, 10);

        machine.pressTraySelectionButton(0);
        machine.pressCancelButton();

        assertNull(machine.getSelectedTray());
        assertEquals(States.BASE, machine.getState());
    }

    @Test
    public void should_be_in_coin_inserted_state_when_product_selected_and_coin_inserted() {
        VendingMachine machine = new VendingMachine();
        Filler.fill(machine, 10);

        machine.pressTraySelectionButton(0);
        machine.insertCoin(Coins.COIN_0_1);

        assertEquals(States.COIN_INSERTED, machine.getState());
        assertEquals(
            String.format(
                Messages.COINS_INSERTED.getMessage(),
                0.1,
                (Products.COCA_COLA_0_33.getPrice() / 10 - 0.1)),
            machine.getDisplay().getMessage()
        );
    }

    @Test
    public void should_return_inserted_coins_when_cancel_pressed_and_coins_inserted() {
        VendingMachine machine = new VendingMachine();
        Filler.fill(machine, 10);

        machine.pressTraySelectionButton(0);
        machine.insertCoin(Coins.COIN_0_1);
        machine.insertCoin(Coins.COIN_0_2);
        machine.insertCoin(Coins.COIN_0_5);
        machine.pressCancelButton();

        assertEquals(States.BASE, machine.getState());
        assertEquals(BASE_MONEY_AMOUNT, machine.getMoneyBox().getTotalAmount());
    }

    @Test
    public void should_return_to_base_state_on_cancel_pressed() {
        VendingMachine machine = new VendingMachine();
        machine.pressTraySelectionButton(0);
        machine.insertCoin(Coins.COIN_0_1);
        machine.pressCancelButton();

        assertEquals(States.BASE, new VendingMachine().getState());
        assertEquals(Messages.IDLE.getMessage(), machine.getDisplay().getMessage());
    }

    @Test
    public void should_not_allow_to_change_product_when_money_inserted() {
        VendingMachine machine = new VendingMachine();
        Filler.fill(machine, 10);
        machine.pressTraySelectionButton(0);
        machine.insertCoin(Coins.COIN_0_1);
        machine.pressTraySelectionButton(1);

        assertEquals(States.COIN_INSERTED, machine.getState());
        assertEquals(
            String.format(
                Messages.COINS_INSERTED.getMessage(),
                0.1,
                (Products.COCA_COLA_0_33.getPrice() / 10 - 0.1)),
            machine.getDisplay().getMessage()
        );
        assertEquals(BASE_MONEY_AMOUNT, machine.getMoneyBox().getTotalAmount());
    }

    @Test
    public void should_dispense_product_and_be_back_in_base_state_when_correct_amount_of_money_inserted() {
        VendingMachine machine = new VendingMachine();
        Filler.fill(machine, 10);
        machine.pressTraySelectionButton(0);
        machine.insertCoin(Coins.COIN_1);
        machine.insertCoin(Coins.COIN_1);

        assertEquals(States.BASE, machine.getState());
        assertEquals(Messages.IDLE.getMessage(), machine.getDisplay().getMessage());
        assertEquals(BASE_MONEY_AMOUNT + Products.COCA_COLA_0_33.getPrice(), machine.getMoneyBox().getTotalAmount());
        assertEquals(BASE_COCA_COLA_0_33_COUNT - 1, machine.getProductBox().getTray(0).getProductCount());
    }

    @Test
    public void should_dispense_product_and_give_back_change_and_be_back_in_base_state_when_over_correct_amount_of_money_inserted() {
        VendingMachine machine = new VendingMachine();
        Filler.fill(machine, 10);
        machine.pressTraySelectionButton(0);
        machine.insertCoin(Coins.COIN_0_5);
        machine.insertCoin(Coins.COIN_0_5);
        machine.insertCoin(Coins.COIN_5);

        assertEquals(States.BASE, machine.getState());
        assertEquals(Messages.IDLE.getMessage(), machine.getDisplay().getMessage());
        assertEquals(BASE_MONEY_AMOUNT + Products.COCA_COLA_0_33.getPrice(), machine.getMoneyBox().getTotalAmount());
        assertEquals(BASE_COCA_COLA_0_33_COUNT - 1, machine.getProductBox().getTray(0).getProductCount());
    }

    @Test
    public void should_not_dispense_product_give_back_money_and_get_back_to_base_state_when_unable_to_give_change() {
        VendingMachine machine = new VendingMachine();
        Filler.fill(machine, 1);

        machine.pressTraySelectionButton(0);
        machine.insertCoin(Coins.COIN_5);
        machine.pressTraySelectionButton(1);
        machine.insertCoin(Coins.COIN_5);

        assertEquals(States.BASE, machine.getState());
        assertEquals(Messages.IDLE.getMessage(), machine.getDisplay().getMessage());
        assertEquals(108, machine.getMoneyBox().getTotalAmount());
        assertEquals(0, machine.getProductBox().getTray(0).getProductCount());
        assertEquals(1, machine.getProductBox().getTray(1).getProductCount());
    }
}
