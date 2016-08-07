package tdd.vendingMachine.machine.state;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tdd.vendingMachine.machine.Machine;
import tdd.vendingMachine.machine.cli.util.CommandLabelDecorator;

import java.util.List;

import static tdd.vendingMachine.machine.state.InteractionState.QUIT;

@Service
class PayingState extends AbstractState implements State {

	private Machine machine;

	@Autowired
	public PayingState(Machine machine) {
		this.machine = machine;
	}

	@Override
	public List<String> getDescription() {
		return Lists.newArrayList(
			CommandLabelDecorator.keyLegend("c", "cancel"),
			QUIT
		);
	}

	@Override
	public void executeCommand(String command, InteractionState interactionState) {
		if (command.equals("c")) {
			interactionState.changeState(InteractionState.StateName.CANCEL);
		}
	}
}
