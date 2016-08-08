package tdd.vendingMachine.machine.state;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import tdd.vendingMachine.machine.cli.util.AnsiColorDecorator;
import tdd.vendingMachine.machine.cli.util.CommandLabelDecorator;
import tdd.vendingMachine.machine.cli.util.DisplayDecorator;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static tdd.vendingMachine.machine.state.InteractionState.QUIT;

@Service
class HelloState extends AbstractState implements State {

	private static final List<String> DESCRIPTION = Lists.newArrayList();

	HelloState() {
		DESCRIPTION.addAll(DisplayDecorator.decorate(
			Lists.newArrayList(AnsiColorDecorator.green("Vending machine is ready."))
		));
		DESCRIPTION.add(EMPTY);
		DESCRIPTION.add(CommandLabelDecorator.keyLegend("s", "display shelves"));
		DESCRIPTION.add(QUIT);
	}

	@Override
	public List<String> getDescription() {
		return DESCRIPTION;
	}

	@Override
	public void executeCommand(String command, InteractionState interactionState) {
		if (command.equals("s")) {
			interactionState.changeState(InteractionState.StateName.PICKING_SHELVE);
		} else {
			this.showInvalidCommandMessage(command, interactionState);
		}
	}

}
