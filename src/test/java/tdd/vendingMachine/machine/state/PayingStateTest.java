package tdd.vendingMachine.machine.state;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.TestUtil;
import tdd.vendingMachine.machine.Machine;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PayingStateTest {

	private Machine machine;

	private PayingState payingState;

	@Before
	public void setup() {
		machine = mock(Machine.class);
		payingState = new PayingState(machine);
	}

	@Test
	public void shows_description() {
		Assertions.assertThat(TestUtil.stripColors(payingState.getDescription().get(0))).isEqualTo("[ c ] - cancel");
	}

	@Test
	public void goes_to_cancel_state_when_canceled() {
		final InteractionState interactionState = mock(InteractionState.class);

		payingState.executeCommand("c", interactionState);

		verify(interactionState).changeState(InteractionState.StateName.CANCEL);
	}

}
