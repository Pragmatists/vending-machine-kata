package tdd.vendingMachine.machine.state;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class PayingStateTest {

	private PayingState payingState;

	@Before
	public void setup() {
		payingState = new PayingState();
	}

	@Test
	public void shows_description() {
		Assertions.assertThat(payingState.getDescription()).isNull();
	}

}
