import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.init.VendingMachineInitializer;

import java.util.Scanner;

public class ManualTest {

    public static final String SYSTEM_PREFIX = "SYSTEM: ";
    public static final String WELCOME_MESSAGE = "Welcome to VendingMachine: what you want to do?";
    public static final String OPTIONS_MESSAGE = "E - exit I - insert Coin S - Select product C - Cancel L - List shelfs";
    public static final String COIN_VALUE_QUESTION_MESSAGE = "How much you want to insert? [DOUBLE]";
    public static final String INCORRECT_DOUBLE_VALUE_MESSAGE = "Incorrect double value.";
    public static final String SHELF_NUMBER_QUESTION_MESSAGE = "Which shelf number you will choose? [INTEGER]";
    public static final String INCORRECT_INT_VALUE_MESSAGE = "Incorrect int value.";

    public static void main(String[] args) {
        VendingMachine vendingMachine = createInitializedMachine();
        Scanner sc = new Scanner(System.in);
        print(WELCOME_MESSAGE);
        String userInput;
        Boolean shouldReadNextValue = true;
        while (shouldReadNextValue) {
            print(OPTIONS_MESSAGE);
            userInput = sc.next().toLowerCase();
            switch (userInput) {
                case "i":
                    print(COIN_VALUE_QUESTION_MESSAGE);
                    if (sc.hasNextDouble()) {
                        vendingMachine.insertCoinForCurrentRequest(new Coin(sc.nextDouble()));
                    } else {
                        print(INCORRECT_DOUBLE_VALUE_MESSAGE);
                    }
                    break;
                case "s":
                    print(SHELF_NUMBER_QUESTION_MESSAGE);
                    if (sc.hasNextInt()) {
                        vendingMachine.selectProduct(sc.nextInt());
                    } else {
                        print(INCORRECT_INT_VALUE_MESSAGE);
                    }
                    break;
                case "c":
                    vendingMachine.cancelRequest();
                    break;
                case "l":
                    vendingMachine.displayMachineShelfsInformation();
                    break;
                case "e":
                    shouldReadNextValue = false;
                    break;
            }
        }
    }

    private static VendingMachine createInitializedMachine() {
        VendingMachine vendingMachine = new VendingMachine();
        VendingMachineInitializer vendingMachineInitializer = new VendingMachineInitializer();
        vendingMachineInitializer.init(vendingMachine);
        return vendingMachine;
    }

    private static void print(String welcomeMessage) {
        System.out.println(SYSTEM_PREFIX + welcomeMessage);
    }
}
