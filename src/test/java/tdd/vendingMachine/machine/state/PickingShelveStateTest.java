package tdd.vendingMachine.machine.state;

import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.TestUtil;
import tdd.vendingMachine.machine.Machine;
import tdd.vendingMachine.product.factory.ProductFactory;
import tdd.vendingMachine.shelve.entity.Shelve;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PickingShelveStateTest {

	private Machine machine;

	private PickingShelveState pickingShelveState;

	@Before
	public void setup() {
		machine = mock(Machine.class);
		pickingShelveState = new PickingShelveState(machine);
	}

	@Test
	public void gets_shelves_description() {
		when(machine.getShelves()).thenReturn(Lists.newArrayList(
			Shelve.of(ProductFactory.createCocaCola(), 3),
			Shelve.of(ProductFactory.createChocolateBar(), 4)
		));

		List<String> description = pickingShelveState.getDescription();

		Assertions.assertThat(description).hasSize(8);
		Assertions.assertThat(description.get(0)).isEqualTo("+---------------------------------------+");
		Assertions.assertThat(description.get(1)).isEqualTo("| Shelve no. 0 | Product: Coca-Cola     |");
		Assertions.assertThat(description.get(2)).isEqualTo("| Shelve no. 1 | Product: Chocolate bar |");
		Assertions.assertThat(description.get(3)).isEqualTo("+---------------------------------------+");
		Assertions.assertThat(description.get(4)).isEqualTo("");
		Assertions.assertThat(TestUtil.stripColors(description.get(5))).isEqualTo("[ 0 ] - pick shelve no. 0");
		Assertions.assertThat(TestUtil.stripColors(description.get(6))).isEqualTo("[ 1 ] - pick shelve no. 1");
		Assertions.assertThat(TestUtil.stripColors(description.get(7))).isEqualTo("[ q ] - quit");
	}

	@Test
	public void shows_unknown_command_message() {
		final String unknownCommand = "unknownCommand";
		final InteractionState interactionState = mock(InteractionState.class);
		pickingShelveState.executeCommand(unknownCommand, interactionState);

		Assertions.assertThat(pickingShelveState.getLatestInvalidCommand()).isEqualTo(unknownCommand);
		verify(interactionState).changeState(InteractionState.StateName.UNKNOWN_COMMAND);
	}

	@Test
	public void changes_state_to_paying_when_valid_shelve_is_picked() {
		final String validShelveCommand = "0";
		final InteractionState interactionState = mock(InteractionState.class);
		pickingShelveState.executeCommand(validShelveCommand, interactionState);

		verify(machine).setActiveShelveIndex(0);
		verify(interactionState).changeState(InteractionState.StateName.PAYING);
	}

}
