package tdd.vendingMachine.domain;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import tdd.vendingMachine.external_interface.HardwareInterface;
import tdd.vendingMachine.test_infrastructure.CoinMapAssertions;
import tdd.vendingMachine.test_infrastructure.HardwareInteractionAssertions;
import tdd.vendingMachine.test_infrastructure.TestMessages;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static tdd.vendingMachine.domain.Money.createMoney;
import static tdd.vendingMachine.test_infrastructure.MethodCaller.callForEachArgument;

@RunWith(JUnitParamsRunner.class)
public class VendingMachineTest {

    private static final int FIRST_SHELF = 1;

    private static final int SECOND_SHELF = 2;


    private VendingMachine machine;

    private Coin[] insertedCoins;

    @Mock
    private HardwareInterface hardwareInterfaceMock;

    @Captor
    private ArgumentCaptor<Map<Coin, Integer>> changeCaptor;

    private Money[] pricesPerShelves;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        pricesPerShelves = new Money[]{createMoney("1.50"), createMoney("3.00")};
        machine = new VendingMachine(hardwareInterfaceMock, pricesPerShelves, new CoinDispenser(new HashMap<>()));
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
    public void should_dispose_previously_inserted_money_when_cancelled() throws Exception {
        insertedCoins = new Coin[]{Coin.COIN_0_5, Coin.COIN_1};

        machine.acceptChoice(SECOND_SHELF);
        callForEachArgument(coin -> machine.acceptCoin(coin), insertedCoins);
        machine.cancel();

        Mockito.verify(hardwareInterfaceMock).disposeInsertedCoins();
    }

    @Test
    public void should_not_add_previously_inserted_coins_value_after_cancellation() throws Exception {
        machine.acceptChoice(FIRST_SHELF);
        machine.acceptCoin(Coin.COIN_0_2);
        machine.cancel();
        machine.acceptChoice(FIRST_SHELF);
        machine.acceptCoin(Coin.COIN_0_1);

        HardwareInteractionAssertions.assertCorrectMessageOrder(hardwareInterfaceMock,
            TestMessages.PRICE_MESSAGE + "1.50",
            TestMessages.REMAINING_MONEY_MESSAGE + "1.30",
            TestMessages.REMAINING_MONEY_MESSAGE + "1.40"
        );
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

    @Test
    public void should_return_change_if_product_has_been_sold() throws Exception {
        machine = new VendingMachine(hardwareInterfaceMock, pricesPerShelves, new CoinDispenser(createCoinSupply()));

        whenUserPaidMoreThanNeeded();

        Mockito.verify(hardwareInterfaceMock).disposeChange(changeCaptor.capture());
        CoinMapAssertions.assertThat(changeCaptor.getValue()).totalValueEquals(createMoney("3.5"));
    }

    private Map<Coin, Integer> createCoinSupply() {
        Map<Coin, Integer> coinSupply = new HashMap<>();
        coinSupply.put(Coin.COIN_2, 1);
        coinSupply.put(Coin.COIN_1, 1);
        coinSupply.put(Coin.COIN_0_5, 1);
        return coinSupply;
    }

    @Test
    public void should_not_dispose_change_if_there_was_no_coins_in_machine() throws Exception {
        whenUserPaidMoreThanNeeded();

        Mockito.verify(hardwareInterfaceMock, never()).disposeChange(any());
    }

    @Test
    public void should_not_dispose_product_if_machine_cannot_return_change() throws Exception {
        whenUserPaidMoreThanNeeded();

        Mockito.verify(hardwareInterfaceMock, never()).disposeProduct(anyInt());
    }

    @Test
    public void should_display_a_warning_message_if_machine_cannot_return_change() throws Exception {
        whenUserPaidMoreThanNeeded();

        Mockito.verify(hardwareInterfaceMock).displayMessage(TestMessages.CANNOT_GIVE_THE_CHANGE_MESSAGE);
    }

    @Test
    public void should_dispose_inserted_coins_if_machine_cannot_return_change() throws Exception {
        whenUserPaidMoreThanNeeded();

        Mockito.verify(hardwareInterfaceMock).disposeInsertedCoins();
    }

    private void whenUserPaidMoreThanNeeded() {
        machine.acceptChoice(FIRST_SHELF);
        machine.acceptCoin(Coin.COIN_5);
    }
}
