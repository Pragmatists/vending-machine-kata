package tdd.vendingMachine.inventory;


import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import tdd.vendingMachine.domain.Money;
import tdd.vendingMachine.domain.Product;

import java.util.Map;

public class Shelfs {

    private final Map<Integer, Product> productOnShelfs = Maps.newHashMap();

    private Integer selectedShelf;

    public static Shelfs of(Product... products) {
        return new Shelfs(products);
    }

    private Shelfs(Product... products) {
        placeProducts(products);
    }

    public void setSelectedShelf(Integer selectedShelf) {
        if (isProductExistOnShelf(selectedShelf)) {
            this.selectedShelf = selectedShelf;
        }
    }

    public Integer getFirstEmptyShelfNumber() {
        return productOnShelfs.size() + 1;
    }

    public boolean isProductExistOnShelf(Integer shelfNumber) {
        return productOnShelfs.containsKey(shelfNumber);
    }

    public boolean isProductExistOnAnyShelf(Product product) {
        for (Integer shelfNumber : productOnShelfs.keySet()) {
            Product productOnShelf = productOnShelfs.get(shelfNumber);
            if (productOnShelf.equals(product)) {
                return true;
            }
        }
        return false;
    }

    public Optional<Product> takeProduct() {
        if (isShelfWasSelected() && isProductExistOnShelf(selectedShelf)) {
            Product product = productOnShelfs.get(selectedShelf);
            productOnShelfs.remove(selectedShelf);
            return Optional.of(product);
        }
        return Optional.absent();
    }

    public boolean isShelfWasSelected() {
        return selectedShelf != null;
    }

    public Money getProductPrice() {
        if (isShelfWasSelected()) {
            return productOnShelfs.get(selectedShelf).price();
        }
        return Money.ZERO;
    }

    private void placeProducts(Product... products) {
        for (Product product : products) {
            placeProduct(product);
        }
    }

    private void placeProduct(Product product) {
        productOnShelfs.put(getFirstEmptyShelfNumber(), product);
    }

}
