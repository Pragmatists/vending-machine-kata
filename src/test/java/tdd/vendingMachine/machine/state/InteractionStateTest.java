package tdd.vendingMachine.machine.state;

import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class InteractionStateTest {

	private ApplicationContext applicationContext;

	private InteractionState interactionState;

	private State state;

	@Before
	public void setup() {
		state = mock(State.class);
		applicationContext = mock(ApplicationContext.class);
		when(applicationContext.getBean(any(Class.class))).thenReturn(state);
		interactionState = new InteractionState(applicationContext);
	}

	@Test
	public void initializes_to_state_hello() {
		Assertions.assertThat(interactionState.getStateName()).isEqualTo(InteractionState.StateName.HELLO);
	}

	@Test
	public void gets_description() {
		final String description = "description";

		when(state.getDescription()).thenReturn(Lists.newArrayList(description));

		Assertions.assertThat(interactionState.getDescription().get(0)).isEqualTo(description);
	}

	@Test
	public void delegates_command_execution() {
		final String command = "command";

		interactionState.executeCommand(command);

		verify(state).executeCommand(command, interactionState);
	}

	@Test
	public void does_state_transition() {
		final InteractionState.StateName stateName = InteractionState.StateName.CANCEL;

		interactionState.changeState(stateName);
		interactionState.changeState(stateName);
		Assertions.assertThat(interactionState.getStateName()).isEqualTo(stateName);
	}

}
