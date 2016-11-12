package tdd.vendingMachine.domain.display.strategy;

public class ConsoleDisplayStrategy implements DisplayStrategy {

    @Override
    public void display(String message) {
        System.out.println(message);
    }
}
