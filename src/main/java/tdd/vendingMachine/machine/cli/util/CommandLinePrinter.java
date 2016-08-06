package tdd.vendingMachine.machine.cli.util;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandLinePrinter {

	private static final String EOL = System.lineSeparator();

	public static final String EMPTY_LINE = "";

	public void print(List<String> messages) {
		System.out.println(EOL + Joiner.on(EOL).join(messages) + EOL);
	}

	public void print(String messages) {
		System.out.println(EOL + Joiner.on(EOL).join(Lists.newArrayList(messages)) + EOL);
	}

	public void exit(int exitCode) {
		System.exit(exitCode);
	}

}
