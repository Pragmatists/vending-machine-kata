package tdd.vendingMachine.machine.state;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.TestUtil;
import tdd.vendingMachine.machine.Machine;
import tdd.vendingMachine.machine.purchase.PurchaseFacade;
import tdd.vendingMachine.machine.purchase.enums.PurchaseStatus;
import tdd.vendingMachine.money.change.ChangeStorage;
import tdd.vendingMachine.money.coin.entity.Coin;
import tdd.vendingMachine.money.coin.factory.CoinFactory;
import tdd.vendingMachine.product.factory.ProductFactory;
import tdd.vendingMachine.shelve.entity.Shelve;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PayingStateTest {

	private PurchaseFacade purchaseFacade;

	private Machine machine;

	private ChangeStorage changeStorage;

	private PayingState payingState;

	@Before
	public void setup() {
		purchaseFacade = mock(PurchaseFacade.class);
		machine = mock(Machine.class);
		changeStorage = mock(ChangeStorage.class);
		final Shelve shelve = mock(Shelve.class);
		when(shelve.getProduct()).thenReturn(ProductFactory.createCocaCola());
		when(machine.getActiveShelve()).thenReturn(shelve);
		payingState = new PayingState(purchaseFacade, machine, changeStorage);
	}

	@Test
	public void shows_description_for_when_there_is_no_coins() {
		when(purchaseFacade.getPurchaseStatus()).thenReturn(PurchaseStatus.INSUFFICIENT_FUNDS);
		when(changeStorage.getInsertedCoins()).thenReturn(Maps.newLinkedHashMap());

		List<String> description = payingState.getDescription();

		Assertions.assertThat(description.get(1)).containsSequence("Buying", "Coca-Cola");
		Assertions.assertThat(description.get(2)).containsSequence("Price", "1.50");
		Assertions.assertThat(description.get(3)).containsSequence("Inserted", " 0.00");
		Assertions.assertThat(description.get(5)).contains("insufficient founds");
	}

	@Test
	public void shows_description_for_when_product_is_buyable() {
		when(purchaseFacade.getPurchaseStatus()).thenReturn(PurchaseStatus.PURCHASABLE);
		final Map<Coin, Integer> founds = Maps.newLinkedHashMap();
		founds.put(CoinFactory.create10(), 1);
		founds.put(CoinFactory.create05(), 1);
		when(changeStorage.getInsertedCoins()).thenReturn(founds);

		List<String> description = payingState.getDescription();

		Assertions.assertThat(description.get(1)).containsSequence("Buying", "Coca-Cola");
		Assertions.assertThat(description.get(2)).containsSequence("Price", "1.50");
		Assertions.assertThat(description.get(3)).containsSequence("Inserted", "1.50");
		Assertions.assertThat(description.get(5)).contains("You can buy now!");
	}

	@Test
	public void shows_description_for_when_product_is_buyable_but_no_change_can_be_given() {
		when(purchaseFacade.getPurchaseStatus()).thenReturn(PurchaseStatus.INSUFFICIENT_CHANGE);
		final Map<Coin, Integer> founds = Maps.newLinkedHashMap();
		founds.put(CoinFactory.create10(), 1);
		founds.put(CoinFactory.create02(), 3);
		when(changeStorage.getInsertedCoins()).thenReturn(founds);

		List<String> description = payingState.getDescription();

		Assertions.assertThat(description.get(1)).containsSequence("Buying", "Coca-Cola");
		Assertions.assertThat(description.get(2)).containsSequence("Price", "1.50");
		Assertions.assertThat(description.get(3)).containsSequence("Inserted", "1.60");
		Assertions.assertThat(description.get(5)).contains(" machine is unable to return change");
	}

	@Test
	public void description_contains_coins_description() {
		when(purchaseFacade.getPurchaseStatus()).thenReturn(PurchaseStatus.INSUFFICIENT_FUNDS);
		when(purchaseFacade.getAvailableCoin()).thenReturn(Lists.newArrayList(
			CoinFactory.create01(),
			CoinFactory.create02())
		);

		List<String> description = payingState.getDescription();

		Assertions.assertThat(TestUtil.stripColors(description.get(9))).containsSequence("[ 0 ]", "coin with value 0.10");
		Assertions.assertThat(TestUtil.stripColors(description.get(10))).containsSequence("[ 1 ]", "coin with value 0.20");
	}

	@Test
	public void goes_to_cancel_state_when_canceled() {
		final InteractionState interactionState = mock(InteractionState.class);

		payingState.executeCommand("c", interactionState);

		verify(interactionState).changeState(InteractionState.StateName.CANCEL);
	}

	@Test
	public void coin_can_be_inserted() {
		final InteractionState interactionState = mock(InteractionState.class);

		payingState.executeCommand("1", interactionState);

		verify(purchaseFacade).insertCoin(1);
	}

	@Test
	public void goes_to_unknown_command_state() {
		final String unknownCommand = "unknownCommand";
		final InteractionState interactionState = mock(InteractionState.class);
		payingState.executeCommand(unknownCommand, interactionState);

		Assertions.assertThat(payingState.getLatestInvalidCommand()).isEqualTo(unknownCommand);
		verify(interactionState).changeState(InteractionState.StateName.UNKNOWN_COMMAND);
	}

	@Test
	public void delegates_buying() {
		final String command = "b";
		final InteractionState interactionState = mock(InteractionState.class);

		payingState.executeCommand(command, interactionState);

		verify(purchaseFacade).buy();
	}

}
