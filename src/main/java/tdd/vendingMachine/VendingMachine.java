package tdd.vendingMachine;

import com.google.common.util.concurrent.AtomicDouble;
import org.apache.log4j.Logger;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.Shelf;
import tdd.vendingMachine.state.NoCreditState;
import tdd.vendingMachine.state.SoldOutState;
import tdd.vendingMachine.state.State;

import java.util.Map;
import java.util.Observable;

public class VendingMachine extends Observable implements State {

    private static final Logger logger = Logger.getLogger(VendingMachine.class);

    //States
    public final SoldOutState soldOutState;
    public final NoCreditState noCreditState;

    private Product selectedProduct;
    private AtomicDouble credit;
    private Map<String, Shelf<Product>> productShelves;
    private final Map<Coin, Shelf<Coin>> coinShelves;
    private Map<String, String> shelfLabelsToIds;

    private State currentState;

    public VendingMachine(Map<String, Shelf<Product>> productShelves, Map<Coin, Shelf<Coin>> coinShelves) {
        this.productShelves = productShelves;
        this.coinShelves = coinShelves;
        this.credit = new AtomicDouble(0.0);
        this.selectedProduct = null;

        this.soldOutState = new SoldOutState(this);
        this.noCreditState = new NoCreditState(this);

        if (productShelves == null || productShelves.size() == 0) {
            this.currentState = soldOutState;
        } else {
            int availableProducts = productShelves.values().stream().map(Shelf::getItemCount).reduce(0, (a, b) -> a + b);
            this.currentState = availableProducts > 0 ? noCreditState : soldOutState;
        }
    }

    @Override
    public void insertCoin(Coin money) {
        currentState.insertCoin(money);
    }

    @Override
    public void selectProduct(String shelfLabel) {
        currentState.selectProduct(shelfLabel);
    }

    @Override
    public void cancel() {
        currentState.cancel();
    }

    /**
     * Returns the available credit on the vending machine
     * @return double must be greater or equal to zero.
     */
    public double getCredit() {
        return credit.get();
    }

    /**
     * Provision cash to vending machine if space is available
     * @param coin the coin shelf to provision
     * @param amount the amount of coins
     */
    public void provisionCoinShelf(Coin coin, int amount) {
    }

    public State getCurrentState() {
        return currentState;
    }
}
