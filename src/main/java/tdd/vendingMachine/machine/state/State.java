package tdd.vendingMachine.machine.state;

import java.util.List;

public interface State {

	List<String> getDescription();

	String getLatestCommand();

	void executeCommand(String command, InteractionState interactionState);

}
