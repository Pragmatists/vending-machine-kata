package tdd.vendingMachine.machine.cli;

import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.machine.MachineFacade;
import tdd.vendingMachine.machine.cli.util.CommandLinePrinter;

import java.io.InputStream;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class CommandLineInterfaceTest {

	private InputStream in;

	private InputStream inputStream;

	private MachineFacade machineFacade;

	private CommandLinePrinter commandLinePrinter;

	private CommandLineInterface commandLineInterface;

	@Before
	public void setup() {
		machineFacade = mock(MachineFacade.class);
		commandLinePrinter = mock(CommandLinePrinter.class);
		commandLineInterface = new CommandLineInterface(machineFacade, commandLinePrinter);

		in = System.in;
		inputStream = mock(InputStream.class);
		System.setIn(inputStream);
	}

	@After
	public void teardown() {
		System.setIn(in);
	}

	@Test
	public void runs_command() {
		final List<String> stateElements = Lists.newArrayList("It's OK.");
		when(machineFacade.getState()).thenReturn(stateElements);
		doNothing().when(machineFacade).executeCommand("line");
		doNothing().when(commandLinePrinter).print(anyString());

		try {
			commandLineInterface.run();
		} catch(Exception e) {
		}

		verify(commandLinePrinter).print(stateElements);
	}

}
