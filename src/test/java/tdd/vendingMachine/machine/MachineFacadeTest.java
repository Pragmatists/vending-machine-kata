package tdd.vendingMachine.machine;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.machine.cli.util.CommandLinePrinter;

import java.util.List;

import static org.mockito.Mockito.*;

public class MachineFacadeTest {

	private CommandLinePrinter commandLinePrinter;

	private MachineFacade machineFacade;

	@Before
	public void setup() {
		commandLinePrinter = mock(CommandLinePrinter.class);
		machineFacade = new MachineFacade(commandLinePrinter);
	}

	@Test
	public void machine_is_ready_on_initialization() {
		List<String> state = machineFacade.getState();

		Assertions.assertThat(state.get(0)).containsSequence("Vending machine is ready.");
	}

	@Test
	public void application_can_be_exited() {
		doNothing().when(commandLinePrinter).print(anyString());
		doNothing().when(commandLinePrinter).exit(0);

		machineFacade.executeCommand("q");

		verify(commandLinePrinter).print(anyString());
		verify(commandLinePrinter).exit(0);
	}

}
