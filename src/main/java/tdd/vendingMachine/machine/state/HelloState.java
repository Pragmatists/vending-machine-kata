package tdd.vendingMachine.machine.state;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import tdd.vendingMachine.machine.cli.util.AnsiColorDecorator;

import java.util.List;

import static tdd.vendingMachine.machine.cli.util.CommandLinePrinter.EMPTY_LINE;
import static tdd.vendingMachine.machine.state.InteractionState.QUIT;

@Service
public class HelloState implements State {

	private static final List<String> DESCRIPTION = Lists.newArrayList(
		AnsiColorDecorator.green("Vending machine is ready."),
		EMPTY_LINE,
		QUIT
	);

	@Override
	public List<String> getDescription() {
		return DESCRIPTION;
	}

	@Override
	public void executeCommand(String command, InteractionState interactionState) {
		// TODO
	}
}
