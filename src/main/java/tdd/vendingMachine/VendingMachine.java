package tdd.vendingMachine;


import tdd.vendingMachine.Config.MockMachineConfig1;
import tdd.vendingMachine.Service.MachineClientController;
import tdd.vendingMachine.Service.MainControllerImpl;
import tdd.vendingMachine.View.SimpleView;

public class VendingMachine {
    public static void main(String[] args) {
        MachineClientController controller = new MainControllerImpl(new MockMachineConfig1());
        SimpleView client = new SimpleView(controller);
        client.run();
    }

}
