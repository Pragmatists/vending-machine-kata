package tdd.vendingMachine.View;


import tdd.vendingMachine.Service.MachineClientController;

/**
 * Simple client sending requests to the MachineController.
 *
 * Due to the reactive nature of the machine, no mechanism for registering to the
 * vending machine (for updates) is envisaged, at this point.
 */
public class SimpleView {
    MachineClientController machineClientController;

    public SimpleView(MachineClientController machineClientController) {
        this.machineClientController = machineClientController;
    }

    //event loop
        //get input
        //reactTo Input (String)
            //call machine
            //display message

}
