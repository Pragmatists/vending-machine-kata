package tdd.vendingMachine.validation;

import lombok.NonNull;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.Shelf;
import tdd.vendingMachine.domain.VendingMachineConfiguration;
import tdd.vendingMachine.domain.exception.InvalidShelfSizeException;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.Map;

/**
 * @author Agustin on 2/26/2017.
 * @since 1.0
 * This class provide validation for vending machine.
 */
public class VendingMachineValidator {

    private static void validateProductShelves(VendingMachineConfiguration vendingMachineConfiguration, Map<Integer, Shelf<Product>> productShelves) {
        if (productShelves.size() > vendingMachineConfiguration.getProductShelfCount()) {
            throw new InvalidShelfSizeException(VendingMachineMessages.PRODUCT_SHELF_SIZE_EXCEEDS_MAX.label,
                vendingMachineConfiguration.getProductShelfCount(), productShelves.size());
        }
        int maxShelfCountFound = productShelves.values().stream()
            .mapToInt(shelf -> shelf.capacity)
            .max().orElse(0);
        if (maxShelfCountFound > vendingMachineConfiguration.getProductShelfCapacity()) {
            throw new InvalidShelfSizeException(VendingMachineMessages.UNABLE_TO_CREATE_VENDING_MACHINE_EXCEEDED_PRODUCT_SHELF_CAPACITY.label,
                vendingMachineConfiguration.getProductShelfCapacity(),
                maxShelfCountFound);
        }
    }

    private static void validateCoinShelves(VendingMachineConfiguration vendingMachineConfiguration, Map<Coin, Shelf<Coin>> coinShelves) {
        int maxCoinShelvesSize = Coin.values().length;
        if (coinShelves.size() != maxCoinShelvesSize) {
            throw new InvalidShelfSizeException(VendingMachineMessages.COIN_SHELF_SIZE_INCOMPATIBLE.label,
                maxCoinShelvesSize, coinShelves.size());
        }
        int maxShelfCountFound = coinShelves.values().stream()
            .mapToInt(shelf -> shelf.capacity)
            .max().orElse(0);
        if (maxShelfCountFound > vendingMachineConfiguration.getCoinShelfCapacity()) {
            throw new InvalidShelfSizeException(VendingMachineMessages.UNABLE_TO_CREATE_VENDING_MACHINE_EXCEEDED_COIN_SHELF_CAPACITY.label,
                vendingMachineConfiguration.getCoinShelfCapacity(),
                maxShelfCountFound);
        }
    }

    /**
     * Validates input parameters to instantiate a vending machine, parameters should comply with vending machine
     * configuration.
     *
     * @param vendingMachineConfiguration current configuration to validate parameters from
     * @param productShelves              the given product shelves
     * @param coinShelves                 the given coin dispenser
     */
    public static void validateNewVendingMachineParameters(@NonNull VendingMachineConfiguration vendingMachineConfiguration,
                                                           @NonNull Map<Integer, Shelf<Product>> productShelves, @NonNull Map<Coin, Shelf<Coin>> coinShelves) {
        validateCoinShelves(vendingMachineConfiguration, coinShelves);
        validateProductShelves(vendingMachineConfiguration, productShelves);
    }

    /**
     * Validates if the given vending machine is in suitable state to be SoldOutState
     * @param vendingMachine the subject to validate
     */
    public static void validateToSoldOutState(VendingMachine vendingMachine) {
        if (!vendingMachine.isSoldOut()) throw new IllegalStateException("machine is not currently sold out: " +
            "existent products: " + vendingMachine.countTotalAmountProducts());
    }

    /**
     * Validates if the given vending machine is in suitable state to be InsufficientCreditState
     * @param vendingMachine the subject to validate
     */
    public static void validateToInsufficientCreditState(VendingMachine vendingMachine) {
        if (null == vendingMachine.getSelectedProduct()
            || vendingMachine.getCredit() >= vendingMachine.getSelectedProduct().getPrice()) {
            throw new IllegalStateException("machine not suitable for insufficientCreditState"
                + " hasSelectedProduct: " + vendingMachine.getSelectedProduct()
                + " credit: " + vendingMachine.getCredit()
                + " product price : " + (vendingMachine.getSelectedProduct() != null ? vendingMachine.getSelectedProduct().getPrice() : "--")
            );
        }
    }

    /**
     * Validates if the given vending machine is in suitable state to be NoCreditSelectedProductState
     * @param vendingMachine the subject to validate
     */
    public static void validateToNoCreditSelectedProductState(VendingMachine vendingMachine) {
        if (vendingMachine.getCredit() > 0
            || !vendingMachine.isCreditStackEmpty()
            || null == vendingMachine.getSelectedProduct()) {
            throw new IllegalStateException("machine not suitable for NoCreditSelectedProductState"
                + " credit: " + vendingMachine.getCredit()
                + " creditStackEmpty: " + vendingMachine.isCreditStackEmpty()
                + " product: " + (vendingMachine.getSelectedProduct() == null ? "--" : vendingMachine.getSelectedProduct().getType())
            );
        }

    }

    /**
     * Validates if the given vending machine is in suitable state to be CreditNotSelectedProductState
     * @param vendingMachine the subject to validate
     */
    public static void validateCreditNotSelectedProductState(VendingMachine vendingMachine) {
        if (vendingMachine.getCredit() == 0
            || vendingMachine.isCreditStackEmpty()
            || null != vendingMachine.getSelectedProduct()) {
            throw new IllegalStateException("machine not suitable for CreditNotSelectedProductState"
                + " credit: " + vendingMachine.getCredit()
                + " creditStackEmpty: " + vendingMachine.isCreditStackEmpty()
                + " product: " + (vendingMachine.getSelectedProduct() == null ? "--" : vendingMachine.getSelectedProduct().getType())
            );
        }
    }

    /**
     * Validates if the given vending machine is in suitable state to be ReadyState
     * @param vendingMachine the subject to validate
     * @throws IllegalStateException if vending machine does not qualifies to be on ReadyState
     */
    public static void validateToReadyState(@NonNull VendingMachine vendingMachine) throws IllegalStateException {
        boolean isMachineSoldOut = vendingMachine.isSoldOut();
        if (isMachineSoldOut
            || vendingMachine.getCredit() > 0
            || !vendingMachine.isCreditStackEmpty()
            || null != vendingMachine.getSelectedProduct()
            ) {
            throw new IllegalStateException("Current values don't qualify for ReadyState"
                + " isSoldOut: " + isMachineSoldOut
                + " currentCredit: " + vendingMachine.getCredit()
                + " isCreditStackEmpty: " + vendingMachine.isCreditStackEmpty()
                + " isProductSelected: " + (vendingMachine.getSelectedProduct() != null)
            );
        }
    }
}
