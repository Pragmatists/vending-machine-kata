package tdd.vendingMachine.machine.state;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.machine.cli.util.CommandLinePrinter;
import tdd.vendingMachine.money.change.ChangeStorage;
import tdd.vendingMachine.money.coin.entity.Coin;
import tdd.vendingMachine.money.coin.factory.CoinFactory;

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

		verify(commandLinePrinter).print(Lists.newArrayList(
			"+---------------------+",
			"| No coins to return. |",
			"+---------------------+"
		));
		verify(interactionState).changeState(InteractionState.StateName.PICKING_SHELVE);
	}

	@Test
	public void returns_coins_on_command_execution() {
		final InteractionState interactionState = mock(InteractionState.class);
		Map<Coin, Integer> insertedCoins = Maps.newHashMap();
		insertedCoins.put(CoinFactory.create02(), 1);
		insertedCoins.put(CoinFactory.create01(), 2);

		when(changeStorage.getInsertedCoins()).thenReturn(insertedCoins);

		cancelState.executeCommand("", interactionState);

		verify(commandLinePrinter).print(Lists.newArrayList(
			"+-------------------------------------+",
			"| Returned 1 coin with nominal 0.20.  |",
			"| Returned 2 coins with nominal 0.10. |",
			"+-------------------------------------+"
		));
		verify(interactionState).changeState(InteractionState.StateName.PICKING_SHELVE);
	}

}
