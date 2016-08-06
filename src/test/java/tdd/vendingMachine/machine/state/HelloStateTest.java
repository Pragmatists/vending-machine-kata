package tdd.vendingMachine.machine.state;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class HelloStateTest {

	private HelloState helloState;

	@Before
	public void setup() {
		helloState = new HelloState();
	}

	@Test
	public void has_complete_description() {
		final List<String> description = helloState.getDescription();

		Assertions.assertThat(description.get(0)).contains("Vending machine is ready.");
		Assertions.assertThat(description.get(2)).contains("quit");
	}

}
