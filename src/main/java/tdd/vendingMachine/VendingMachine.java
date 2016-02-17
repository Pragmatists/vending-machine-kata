package tdd.vendingMachine;


import tdd.vendingMachine.Service.MachineClientController;
import tdd.vendingMachine.Service.MainControllerImpl;
import tdd.vendingMachine.View.SimpleView;

public class VendingMachine {
    public static void main(String[] args) {
        MachineClientController controller = new MainControllerImpl();
        SimpleView client = new SimpleView(controller);
        client.run();
    }

}
