package tdd.vendingMachine.domain;

import org.junit.Test;
import tdd.vendingMachine.domain.display.Messages;
import tdd.vendingMachine.domain.money.Coins;
import tdd.vendingMachine.domain.product.Products;
import tdd.vendingMachine.domain.state.States;

import static org.junit.Assert.assertEquals;

public class VendingMachineTest {

    private static int BASE_MONEY_AMOUNT = 100;
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
        machine.pressTraySelectionButton(0);

        assertEquals(States.TRAY_SELECTED, new VendingMachine().getState());
        assertEquals(
            String.format(
                Messages.PRODUCT_SELECTED.getMessage(),
                Products.COCA_COLA_0_33.name(),
                Products.COCA_COLA_0_33.getPrice()
            ), machine.getDisplay().getMessage()
        );
    }

    @Test
    public void should_be_in_coin_inserted_state_when_product_selected_and_coin_inserted() {
        VendingMachine machine = new VendingMachine();
        machine.pressTraySelectionButton(1);
        machine.insertCoin(Coins.COIN_0_1);

        assertEquals(States.COIN_INSERTED, new VendingMachine().getState());
        assertEquals(
            String.format(
                Messages.COINS_INSERTED.getMessage(),
                0.1,
                (Products.COCA_COLA_0_33.getPrice() / 10 - 0.1)),
            machine.getDisplay().getMessage()
        );
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
        machine.pressTraySelectionButton(0);
        machine.insertCoin(Coins.COIN_0_1);
        machine.pressTraySelectionButton(1);

        assertEquals(States.COIN_INSERTED, new VendingMachine().getState());
        assertEquals(Messages.IDLE.getMessage(), machine.getDisplay().getMessage());
        assertEquals(BASE_MONEY_AMOUNT, machine.getMoneyBox().getTotalAmount());
    }

    @Test
    public void should_dispense_product_and_be_back_in_base_state_when_correct_amount_of_money_inserted() {
        VendingMachine machine = new VendingMachine();
        machine.pressTraySelectionButton(0);
        machine.insertCoin(Coins.COIN_0_1);
        machine.insertCoin(Coins.COIN_0_1);
        machine.insertCoin(Coins.COIN_0_1);
        machine.insertCoin(Coins.COIN_0_1);

        assertEquals(States.BASE, new VendingMachine().getState());
        assertEquals(Messages.IDLE.getMessage(), machine.getDisplay().getMessage());
        assertEquals(BASE_MONEY_AMOUNT - Products.COCA_COLA_0_5.getPrice(), machine.getMoneyBox().getTotalAmount());
        assertEquals(BASE_COCA_COLA_0_33_COUNT - 1, machine.getProductBox().getTray(0).getProductCount());
    }

    @Test
    public void should_not_dispense_product_give_back_money_and_get_back_to_base_state_when_unable_to_give_change() {
        VendingMachine machine = new VendingMachine();
        machine.pressTraySelectionButton(0);
        machine.insertCoin(Coins.COIN_0_1);
        machine.insertCoin(Coins.COIN_0_1);
        machine.insertCoin(Coins.COIN_0_1);
        machine.insertCoin(Coins.COIN_0_1);

        assertEquals(States.BASE, new VendingMachine().getState());
        assertEquals(Messages.IDLE.getMessage(), machine.getDisplay().getMessage());
        assertEquals(BASE_MONEY_AMOUNT, machine.getMoneyBox().getTotalAmount());
        assertEquals(BASE_COCA_COLA_0_33_COUNT, machine.getProductBox().getTray(0).getProductCount());
    }
}
