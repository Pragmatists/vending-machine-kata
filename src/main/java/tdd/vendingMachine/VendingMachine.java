package tdd.vendingMachine;


public class VendingMachine {

    public static void main(String[] args) {

        new Configuration().loadConfig("dev");
        Shelf shelf = new Shelf();
        System.out.println(shelf.getShelfSize());
    }

}
