package tdd.vendingMachine.machine.state;

import java.util.List;

class PayingState extends AbstractState implements State {

	@Override
	public List<String> getDescription() {
		return null;
	}

	@Override
	public void executeCommand(String command, InteractionState interactionState) {

	}
}
