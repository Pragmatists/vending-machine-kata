package tdd.vendingMachine;

/**
 * @author macko
 * @since 2015-08-19
 */
public class GivingTray {

    public void giveProduct(Product product) {
        System.out.println(String.format("Product [%s] released", product.getName()));
    }

}
