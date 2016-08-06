package tdd.vendingMachine.machine;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tdd.vendingMachine.machine.cli.util.AnsiColorDecorator;
import tdd.vendingMachine.machine.cli.util.CommandLinePrinter;

import java.util.List;

@Service
public class MachineFacade {

	private CommandLinePrinter commandLinePrinter;

	@Autowired
	public MachineFacade(CommandLinePrinter commandLinePrinter) {
		this.commandLinePrinter = commandLinePrinter;
	}

	public List<String> getState() {
		return Lists.newArrayList(AnsiColorDecorator.green("Vending machine is ready."));
	}

	public void executeCommand(String command) {
		if (command.equals("q")) {
			commandLinePrinter.print(AnsiColorDecorator.green("Exiting."));
			commandLinePrinter.exit(0);
		}
	}

}
