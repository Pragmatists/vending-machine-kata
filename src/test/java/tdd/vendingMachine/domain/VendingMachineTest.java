package tdd.vendingMachine.domain;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import tdd.vendingMachine.external_interface.HardwareInterface;
import tdd.vendingMachine.test_infrastructure.TestMessages;

import java.util.Arrays;

import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.*;
import static tdd.vendingMachine.domain.Money.createMoney;
import static tdd.vendingMachine.test_infrastructure.MethodCaller.callForEachArgument;

@RunWith(JUnitParamsRunner.class)
public class VendingMachineTest {

    private static final int FIRST_SHELF = 1;

    private static final int SECOND_SHELF = 2;

    private VendingMachine machine;

    private HardwareInterface hardwareInterfaceMock;

    private Coin[] insertedCoins;

    @Before
    public void setUp() throws Exception {
        hardwareInterfaceMock = mock(HardwareInterface.class);
        Money[] pricesPerShelves = {createMoney("1.50"), createMoney("3.00")};
        machine = new VendingMachine(hardwareInterfaceMock, pricesPerShelves);
    }

    @Test
    public void should_display_welcome_message() throws Exception {
        Mockito.verify(hardwareInterfaceMock, only()).displayMessage(TestMessages.WELCOME_MESSAGE);
    }

    @Test
    @Parameters({
        "1, 1.50",
        "2, 3.00"
    })
    public void should_show_product_price_if_shelf_is_selected(int shelfChoice, String priceValue) throws Exception {
        machine.acceptChoice(shelfChoice);

        Mockito.verify(hardwareInterfaceMock).displayMessage(TestMessages.PRICE_MESSAGE + priceValue);
    }

    @Test
    @Parameters({
        "0",
        "3"
    })
    public void should_inform_about_invalid_choice(int shelfChoice) throws Exception {
        machine.acceptChoice(shelfChoice);

        Mockito.verify(hardwareInterfaceMock).displayMessage(TestMessages.INVALID_SHELF_CHOICE_MESSAGE);
    }

    @Test
    public void should_never_show_product_price_if_choice_is_invalid() throws Exception {
        machine.acceptChoice(0);

        Mockito.verify(hardwareInterfaceMock, never()).displayMessage(startsWith(TestMessages.PRICE_MESSAGE));
    }

    @Test
    public void should_show_remaining_amount_of_money_needed_after_inserting_a_coin() throws Exception {
        machine.acceptChoice(FIRST_SHELF);
        machine.acceptCoin(Coin.COIN_1);

        Mockito.verify(hardwareInterfaceMock).displayMessage(TestMessages.REMAINING_MONEY_MESSAGE + "0.50");
    }

    @Test
    public void should_show_that_zero_is_remaining_if_sufficient_or_more_money_has_been_paid() throws Exception {
        machine.acceptChoice(FIRST_SHELF);
        callForEachArgument(coin -> machine.acceptCoin(coin), Coin.COIN_1, Coin.COIN_5);

        Mockito.verify(hardwareInterfaceMock).displayMessage(TestMessages.REMAINING_MONEY_MESSAGE + "0.00");
    }

    @Test
    public void should_show_welcome_message_if_coins_inserted_without_selecting_shelf() throws Exception {
        machine.acceptCoin(Coin.COIN_2);

        Mockito.verify(hardwareInterfaceMock, only()).displayMessage(TestMessages.WELCOME_MESSAGE);
    }

    @Test
    public void should_show_welcome_message_if_purchase_is_canceled() throws Exception {
        machine.acceptChoice(FIRST_SHELF);
        machine.cancel();

        Mockito.verify(hardwareInterfaceMock, times(2)).displayMessage(TestMessages.WELCOME_MESSAGE);
    }

    @Test
    public void should_return_previously_inserted_money_when_cancelled() throws Exception {
        insertedCoins = new Coin[]{Coin.COIN_0_5, Coin.COIN_1};

        machine.acceptChoice(SECOND_SHELF);
        callForEachArgument(coin -> machine.acceptCoin(coin), insertedCoins);
        machine.cancel();

        Mockito.verify(hardwareInterfaceMock).disposeInsertedCoins(Arrays.asList(insertedCoins));
    }

    @Test
    public void should_return_product_when_user_inserts_exact_amount_of_money() throws Exception {
        insertedCoins = new Coin[]{Coin.COIN_1, Coin.COIN_0_5};

        machine.acceptChoice(FIRST_SHELF);
        callForEachArgument(coin -> machine.acceptCoin(coin), insertedCoins);

        Mockito.verify(hardwareInterfaceMock).disposeProduct(FIRST_SHELF);
    }

    @Test
    public void should_display_welcome_message_when_product_is_disposed() throws Exception {
        insertedCoins = new Coin[]{Coin.COIN_1, Coin.COIN_0_5};

        machine.acceptChoice(FIRST_SHELF);
        callForEachArgument(coin -> machine.acceptCoin(coin), insertedCoins);

        Mockito.verify(hardwareInterfaceMock, times(2)).displayMessage(TestMessages.WELCOME_MESSAGE);
    }
}
