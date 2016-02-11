package tdd.vendingMachine;

import com.google.common.collect.Lists;
import tdd.vendingMachine.display.DefaultDisplayFactory;
import tdd.vendingMachine.product.Cola;
import tdd.vendingMachine.shelve.DefaultShelve;
import tdd.vendingMachine.shelve.Shelve;
import tdd.vendingMachine.shelve.ShelveKeyMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by okraskat on 09.02.16.
 */
public class Application {
    public static void main(String[] args) throws IOException, InterruptedException {

        Shelve shelve1 = new DefaultShelve<>(new ArrayList<>(), BigDecimal.ZERO, "Product");

        Cola cola = new Cola();
        Shelve shelve2 = new DefaultShelve<>(Lists.newArrayList(cola, new Cola(), new Cola()), cola.getPrice(), cola.getName());
        List<Shelve> shelves = Lists.newArrayList(shelve1, shelve2);
        VendingMachine vendingMachine = new VendingMachine(shelves, new DefaultDisplayFactory(), new ShelveKeyMapper(), new ChangeCalculator());
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        System.out.println("Starting vending machine");
        System.out.println("Available products");
        showShelves(vendingMachine);
        showHelp();
        while(!(input = reader.readLine()).equals("e")){
            switch (input) {
                case "s":
                    selectShelve(vendingMachine, reader);
                    break;
                case "i":
                    insertCoin(vendingMachine, reader);
                    break;
                case "p":
                    showShelves(vendingMachine);
                    break;
                case "h":
                    showHelp();
                    break;
                case "c":
                    vendingMachine.cancel();
            }
            Thread.sleep(500);
        }
        System.out.println("Vending machine stop");
    }

    private static void insertCoin(VendingMachine vendingMachine, BufferedReader reader) throws IOException {
        String input;
        System.out.println("Insert coin");
        input = reader.readLine();
        try {
            VendingMachineReturnItems returned = vendingMachine.insertCoin(new BigDecimal(input));
            System.out.println(returned.toString());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number: " + input);
        }
    }

    private static void selectShelve(VendingMachine vendingMachine, BufferedReader reader) throws IOException {
        String input;
        System.out.println("Select shelve");
        input = reader.readLine();
        try {
            vendingMachine.selectShelve(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            System.out.println("Invalid number: " + input);
        }
    }

    private static void showHelp() {
        System.out.println("h - show help");
        System.out.println("p - present shelves");
        System.out.println("s - select shelve");
        System.out.println("i - insert coin");
        System.out.println("c - cancel");
        System.out.println("e - exit");
    }

    private static void showShelves(VendingMachine vendingMachine) {
        System.out.println(vendingMachine.getShelves().toString());
    }
}
