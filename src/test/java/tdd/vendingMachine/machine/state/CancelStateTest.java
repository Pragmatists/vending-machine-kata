package tdd.vendingMachine.machine.state;

import com.google.common.collect.Maps;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import tdd.vendingMachine.machine.cli.util.CommandLinePrinter;
import tdd.vendingMachine.money.change.ChangeStorage;
import tdd.vendingMachine.money.coin.entity.Coin;
import tdd.vendingMachine.money.coin.factory.CoinFactory;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class CancelStateTest {

	private CommandLinePrinter commandLinePrinter;

	private ChangeStorage changeStorage;

	private CancelState cancelState;

	@Before
	public void setup() {
		commandLinePrinter = mock(CommandLinePrinter.class);
		changeStorage = mock(ChangeStorage.class);
		cancelState = new CancelState(commandLinePrinter, changeStorage);
	}

	@Test
	public void has_empty_description() {
		Assertions.assertThat(cancelState.getDescription()).isEmpty();
	}

	@Test
	public void shows_empty_coins_description() {
		final InteractionState interactionState = mock(InteractionState.class);
		when(changeStorage.getInsertedCoins()).thenReturn(Maps.newHashMap());

		cancelState.executeCommand("", interactionState);

		ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
		verify(commandLinePrinter).print(argumentCaptor.capture());
		List<String> messages = argumentCaptor.getValue();

		Assertions.assertThat(messages.get(0)).containsSequence("No coins to return.");

		verify(interactionState).changeState(InteractionState.StateName.PICKING_SHELVE);
	}

	@Test
	public void returns_coins_on_command_execution() {
		final InteractionState interactionState = mock(InteractionState.class);
		Map<Coin, Integer> insertedCoins = Maps.newHashMap();
		insertedCoins.put(CoinFactory.create020(), 1);
		insertedCoins.put(CoinFactory.create010(), 2);

		when(changeStorage.getInsertedCoins()).thenReturn(insertedCoins);

		cancelState.executeCommand("", interactionState);

		ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
		verify(commandLinePrinter).print(argumentCaptor.capture());
		List<String> messages = argumentCaptor.getValue();

		Assertions.assertThat(messages.get(0)).containsSequence("Returned 1 coin with nominal 0.20.");
		Assertions.assertThat(messages.get(1)).containsSequence("Returned 2 coins with nominal 0.10.");

		verify(interactionState).changeState(InteractionState.StateName.PICKING_SHELVE);
	}

}
