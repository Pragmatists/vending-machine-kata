package tdd.vendingMachine.machine.state;

import lombok.Getter;

abstract class AbstractState {

	@Getter
	private String latestInvalidCommand;

	void showInvalidCommandMessage(String command, InteractionState interactionState) {
		this.latestInvalidCommand = command;
		interactionState.changeState(InteractionState.StateName.UNKNOWN_COMMAND);
	}

}
