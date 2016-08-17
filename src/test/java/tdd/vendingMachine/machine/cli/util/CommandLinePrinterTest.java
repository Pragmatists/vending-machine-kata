package tdd.vendingMachine.machine.cli.util;

import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.PrintStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CommandLinePrinterTest {

	private PrintStream out;

	private PrintStream printStream;

	private CommandLinePrinter commandLinePrinter;

	@Before
	public void setup() {
		out = System.out;
		printStream = mock(PrintStream.class);
		System.setOut(printStream);
		commandLinePrinter = new CommandLinePrinter();
	}

	@After
	public void teardown() {
		System.setOut(out);
	}

	@Test
	public void outputs_multiple_strings() {
		final String testCommandFirst = "test_command_first";
		final String testCommandSecond = "test_command_second";
		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

		commandLinePrinter.print(Lists.newArrayList(testCommandFirst, testCommandSecond));
		verify(printStream).println(argumentCaptor.capture());

		Assertions.assertThat(argumentCaptor.getValue()).containsSequence(testCommandFirst);
		Assertions.assertThat(argumentCaptor.getValue()).containsSequence(testCommandSecond);
	}

	@Test
	public void outputs_single_string() {
		final String testCommand = "test_command";
		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

		commandLinePrinter.print(testCommand);
		verify(printStream).println(argumentCaptor.capture());

		Assertions.assertThat(argumentCaptor.getValue()).containsSequence(testCommand);
	}
}
