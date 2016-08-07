package tdd.vendingMachine.machine.state;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HelloStateTest {

	private HelloState helloState;

	@Before
	public void setup() {
		helloState = new HelloState();
	}

	@Test
	public void has_complete_description() {
		final List<String> description = helloState.getDescription();

		Assertions.assertThat(description.get(1)).contains("Vending machine is ready.");
		Assertions.assertThat(description.get(4)).contains("display shelves");
		Assertions.assertThat(description.get(5)).contains("quit");
	}

	@Test
	public void changes_state_to_picking_shelve() {
		final InteractionState interactionState = mock(InteractionState.class);
		helloState.executeCommand("s", interactionState);

		verify(interactionState).changeState(InteractionState.StateName.PICKING_SHELVE);
	}

	@Test
	public void shows_unknown_command_message() {
		final String unknownCommand = "unknownCommand";
		final InteractionState interactionState = mock(InteractionState.class);
		helloState.executeCommand(unknownCommand, interactionState);

		Assertions.assertThat(helloState.getLatestInvalidCommand()).isEqualTo(unknownCommand);
		verify(interactionState).changeState(InteractionState.StateName.UNKNOWN_COMMAND);
	}

}
