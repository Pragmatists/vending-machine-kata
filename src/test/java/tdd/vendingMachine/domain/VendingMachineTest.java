package tdd.vendingMachine.domain;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import tdd.vendingMachine.external_interface.Display;

import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.*;
import static tdd.vendingMachine.domain.Money.createMoney;


@RunWith(JUnitParamsRunner.class)
public class VendingMachineTest {

    @InjectMocks
    private VendingMachine machine;

    @Mock
    private Display displayMock;

    @Before
    public void setUp() throws Exception {
        displayMock = mock(Display.class);
        Money[] pricesPerShelves = {createMoney("1.50"), createMoney("3.00")};
        machine = new VendingMachine(displayMock, pricesPerShelves);
    }

    @Test
    public void should_display_welcome_message() throws Exception {
        Mockito.verify(displayMock, only()).displayMessage("Welcome! Please choose product:");
    }

    @Test
    @Parameters({
        "1, 1.50",
        "2, 3.00"
    })
    public void should_show_product_price_if_shelf_is_selected(int shelfChoice, String priceValue) throws Exception {
        machine.acceptChoice(shelfChoice);

        Mockito.verify(displayMock).displayMessage("Price: " + priceValue);
    }

    @Test
    @Parameters({
        "0",
        "3"
    })
    public void should_inform_about_invalid_choice(int shelfChoice) throws Exception {
        machine.acceptChoice(shelfChoice);

        Mockito.verify(displayMock).displayMessage("Invalid shelf choice. Please try again.");
        Mockito.verify(displayMock, never()).displayMessage(startsWith("Price"));
    }

    @Test
    public void should_show_remaining_amount_of_money_needed_after_inserting_a_coin() throws Exception {
        machine.acceptChoice(1);
        machine.acceptCoin(createMoney("1"));

        Mockito.verify(displayMock).displayMessage("Remaining: 0.50");
    }

    @Test
    public void should_show_that_zero_is_remaining_if_sufficient_or_more_money_has_been_paid() throws Exception {
        machine.acceptChoice(1);
        machine.acceptCoin(createMoney("1"));
        machine.acceptCoin(createMoney("1"));

        Mockito.verify(displayMock).displayMessage("Remaining: 0.00");
    }

    @Test
    public void should_show_welcome_message_if_coins_inserted_without_selecting_shelf() throws Exception {
        machine.acceptCoin(createMoney("1"));

        Mockito.verify(displayMock, only()).displayMessage("Welcome! Please choose product:");
    }
}
