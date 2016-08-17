package tdd.vendingMachine.machine.state;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tdd.vendingMachine.machine.cli.util.AnsiColorDecorator;
import tdd.vendingMachine.machine.cli.util.CommandLinePrinter;

import java.util.List;

@Service
class UnknownCommandState extends AbstractState implements State {

	private CommandLinePrinter commandLinePrinter;

	@Autowired
	public UnknownCommandState(CommandLinePrinter commandLinePrinter) {
		this.commandLinePrinter = commandLinePrinter;
	}

	@Override
	public List<String> getDescription() {
		return Lists.newArrayList();
	}

	@Override
	public void executeCommand(String command, InteractionState interactionState) {
		commandLinePrinter.print(AnsiColorDecorator.red("Unknown command: " + command));
	}
}
