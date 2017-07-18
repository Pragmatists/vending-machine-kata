package tdd.vendingMachine;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.springframework.security.crypto.password.PasswordEncoder;
import tdd.vendingMachine.dto.Coin;
import tdd.vendingMachine.dto.Product;

import java.math.BigDecimal;
import java.security.KeyException;
import java.util.ArrayList;
import java.util.List;

class VendingMachineImpl implements VendingMachine {
    private final List<Shelf> shelves = new ArrayList<>();
    private final Display display;
    private final CashRegister cashRegister;
    private final ChangeDispenser changeDispenser;
    private final ProductDispenser productDispenser;
    private final PasswordEncoder passwordEncoder;
    private final String encodedKey;
    private int chosenShelf;

    @Inject
    public VendingMachineImpl(Display display,
                              CashRegister cashRegister,
                              ChangeDispenser changeDispenser,
                              ProductDispenser productDispenser,
                              PasswordEncoder passwordEncoder,
                              @Named("key") String key) {
        this.display = display;
        this.cashRegister = cashRegister;
        this.changeDispenser = changeDispenser;
        this.productDispenser = productDispenser;
        this.passwordEncoder = passwordEncoder;
        this.encodedKey = this.passwordEncoder.encode(key);
    }

    public void loadShelf(String key, Shelf shelf) throws KeyException {
        if (this.passwordEncoder.matches(key, this.encodedKey)) {
            this.shelves.add(shelf);
        } else {
            throw new KeyException("Key is incorrect");
        }
    }

    public void insertCoin(Coin coin) {
        if (this.cashRegister.insertCoin(coin)) {
            this.productDispenser.ejectProduct(this.shelves.get(this.chosenShelf).dispense());
        }
    }

    public String lookAtDisplay() {
        return this.display.view();
    }

    public void typeShelfNumber(int shelfNumber) {
        if (this.shelves.size() > shelfNumber &&
            this.shelves.get(shelfNumber) != null &&
            !this.shelves.get(shelfNumber).isEmpty()) {
            BigDecimal productPrice = this.shelves.get(shelfNumber).getProductPrice();
            this.cashRegister.setProductPrice(productPrice);
            this.chosenShelf = shelfNumber;
        } else {
            this.display.writeWarning("Invalid shelf number or shelf is empty. Please enter a different shelf number.");
        }
    }

    public void pressCancel() {
        this.cashRegister.reset();
    }

    public List<Coin> retrieveChange() {
        return this.changeDispenser.retrieveCoins();
    }

    public List<Product> retrieveProducts() {
        return this.productDispenser.retrieveProducts();
    }
}
