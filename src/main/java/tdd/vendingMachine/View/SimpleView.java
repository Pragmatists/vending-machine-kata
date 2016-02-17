package tdd.vendingMachine.View;


import tdd.vendingMachine.Service.*;

import java.util.Scanner;

/**
 * Simple client sending requests to the MachineController.
 *
 * Due to the reactive nature of the machine, no mechanism for registering to the
 * vending machine (for updates) is envisaged, at this point.
 */
public class SimpleView implements Runnable {
    MachineClientController machine;

    public SimpleView(MachineClientController machineClientController) {
        this.machine = machineClientController;
    }



    @Override
    public void run() {
        String query;
        String usage = "------------------\nVending Machine : Client mode\n------------------\nAllowed commands:\n#{shelf_number}\t\t: select shelf\n" +
            "*{inserted_coin}\t: insert coin\nL\t: product list\nX\t: cancel selection\nQ\t: quit\n------------------\n";
        System.out.println(usage);
        Scanner s = new Scanner(System.in);
        while(true) {
            query = s.next();
            if (query.equals("L")) {
//                Schematic response
//                Response r = machine.getShelfContents();
//                if (!r.getStatus().equals(ResponseStatus.OK))
//                    System.out.println(r.getComment());
//                Iterable<ShelfTransferObject> content = (Iterable<ShelfTransferObject>) r.getResult();
//                for(ShelfTransferObject obj : content) System.out.println(obj);
            } else if (query.equals("X")) {

            } else if (query.equals("Q")) {
                break;
            } else if (query.startsWith("#")) {

            } else if (query.startsWith("*")) {

            } else System.out.println(usage);
        }
    }

    //---------

}
