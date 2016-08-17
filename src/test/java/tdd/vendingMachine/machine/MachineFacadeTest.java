package tdd.vendingMachine.machine;

import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.machine.cli.util.CommandLinePrinter;
import tdd.vendingMachine.machine.state.InteractionState;

import java.util.List;

import static org.mockito.Mockito.*;

public class MachineFacadeTest {

	private CommandLinePrinter commandLinePrinter;

	private MachineFacade machineFacade;

	private InteractionState interactionState;

	@Before
	public void setup() {
		commandLinePrinter = mock(CommandLinePrinter.class);
		interactionState = mock(InteractionState.class);
		machineFacade = new MachineFacade(commandLinePrinter, interactionState);
	}

	@Test
	public void machine_is_ready_on_initialization() {
		final String hello = "hello";
		when(interactionState.getDescription()).thenReturn(Lists.newArrayList(hello));

		List<String> state = machineFacade.getState();

		Assertions.assertThat(state.get(0)).containsSequence("hello");
	}

	@Test
	public void application_can_be_exited() {
		machineFacade.executeCommand("q");

		verify(commandLinePrinter).print(anyString());
		verify(commandLinePrinter).exit(0);
	}

	@Test
	public void command_is_passed_to_InteractionState() {
		final String command = "command";

		machineFacade.executeCommand(command);

		verify(interactionState).executeCommand(command);
	}

}
