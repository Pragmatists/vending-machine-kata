package tdd.vendingMachine.machine.state;

import lombok.Getter;

abstract class AbstractState {

	@Getter
	private String latestCommand;

	void showInvalidCommandMessage(String command, InteractionState interactionState) {
		this.latestCommand = command;
		interactionState.changeState(InteractionState.StateName.UNKNOWN_COMMAND);
	}

}
