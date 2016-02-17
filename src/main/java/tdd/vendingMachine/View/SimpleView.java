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
            System.out.println("your command? ");
            query = s.next();
            if (query.equals("L")) {
                Response r = machine.getShelfContents();
                if (!r.getStatus().equals(ResponseStatus.OK)) {
                    System.out.println(r.getComment());
                    continue;
                }
                Iterable<ShelfTransferObject> content = (Iterable<ShelfTransferObject>) r.getResult();
                for(ShelfTransferObject to : content) {
                    System.out.format("Shelf: %d\t Product:%s\t Price:%d\n",
                        to.getShelfnumber(), to.getProductname(), to.getPrice());
                }
            }
            else if (query.equals("X")) {
                Response r = machine.cancelTransaction();
                printResponse(r);
            }
            else if (query.equals("Q")) {
                break;
            }
            else if (query.startsWith("#")) {
                String rest = query.substring(1);
                Integer shelfNo = parseInt(rest);
                if (shelfNo==null) continue;
                Response r = machine.selectShelf(shelfNo);
                printResponse(r);

            }
            else if (query.startsWith("*")) {
                String rest = query.substring(1);
                Integer coin = parseInt(rest);
                if (coin==null) continue;
            } else System.out.println(usage);
        }
    }

    //---------
    private Integer parseInt(String s) {
        Integer res = null;
        try {
            res = Integer.valueOf(s);
        } catch (Exception e) {
            System.out.println("Error reading number!");
        }
        return res;
    }

    //--------
    private void printResponse(Response r) {
        if (!r.getStatus().equals(ResponseStatus.OK)) {
            System.out.println(r.getComment());
        } else {
            System.out.println(r.getResult());
        }
    }
}
