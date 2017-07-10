package tdd.vendingmachine.domain;

import lombok.ToString;
import tdd.vendingmachine.domain.dto.CoinDto;
import tdd.vendingmachine.domain.dto.ProductDto;
import tdd.vendingmachine.domain.dto.VendingMachineDto;

import java.util.Collection;
import java.util.Objects;

@ToString
class VendingMachine {

    private Shelves shelves;
    private ChangeDispenser changeDispenser;
    private ProductDispenser productDispenser;
    private MachineMoney machineMoney;
    private Display display;
    private TransactionState transactionState;
    private VendingMachineState state;

    private VendingMachine(Shelves shelves, MachineMoney machineMoney) {
        this.shelves = Objects.requireNonNull(shelves);
        this.machineMoney = Objects.requireNonNull(machineMoney);
        display = Display.empty();
        transactionState = TransactionState.clear();
        changeDispenser = ChangeDispenser.empty();
        productDispenser = ProductDispenser.empty();
        state = new IdleState();
    }

    static VendingMachine create(VendingMachineDto vendingMachineDto) {
        Shelves shelves = Shelves.create(vendingMachineDto.getShelves());
        Denominations acceptableDenominations = Denominations.create(vendingMachineDto.getAcceptableDenominations());
        Coins coins = Coins.create(vendingMachineDto.getCoins());
        MachineMoney machineMoney = MachineMoney.create(acceptableDenominations, coins);
        return new VendingMachine(shelves, machineMoney);
    }

    void selectShelfNumber(ShelfNumber shelfNumber) {
        state.selectShelfNumber(shelfNumber);
    }

    String showDisplay() {
        return display.show();
    }

    void insertCoin(CoinDto coinDto) {
        Coin coin = Coin.create(coinDto);
        state.insertCoin(coin);
    }

    Collection<CoinDto> returnChange() {
        Collection<CoinDto> dispensedChange = changeDispenser.dispense();
        changeDispenser = ChangeDispenser.empty();
        return dispensedChange;
    }

    Collection<ProductDto> giveProducts() {
        Collection<ProductDto> dispensedProducts = productDispenser.dispense();
        productDispenser = ProductDispenser.empty();
        return dispensedProducts;
    }

    private void handleSelectingShelf(ShelfNumber shelfNumber) {
        SelectedShelf selectedShelf = new ShelfSelector(shelves).select(shelfNumber);
        display = selectedShelf.display();
        transactionState = selectedShelf.newTransactionState();
        state = stateFrom(transactionState.phase());
    }

    private void handleInsertingCoin(Coin coin) {
        CoinInserted coinInserted = new CoinInserter(transactionState, machineMoney, shelves,
            changeDispenser, productDispenser).insert(coin);
        display = coinInserted.display();
        transactionState = coinInserted.transactionState();
        shelves = coinInserted.shelves();
        machineMoney = coinInserted.machineMoney();
        changeDispenser = coinInserted.changeDispenser();
        productDispenser = coinInserted.productDispenser();
        state = stateFrom(transactionState.phase());
    }

    void cancel() {
        state.cancel();
    }

    private void clear() {
        display = Display.empty();
        transactionState = TransactionState.clear();
        state = stateFrom(transactionState.phase());
    }

    private VendingMachineState stateFrom(TransactionPhase transactionPhase) {
        return new VendingMachineStateFactory().createFrom(transactionPhase);
    }

    private interface VendingMachineState {
        void selectShelfNumber(ShelfNumber shelfNumber);
        void insertCoin(Coin coin);
        void cancel();
    }

    private class VendingMachineStateFactory {
        VendingMachineState createFrom(TransactionPhase transactionPhase) {
            switch (transactionPhase) {
                case IDLE:
                    return new IdleState();
                case SHELF_SELECTED:
                    return new ShelfSelectedState();
                case COIN_INSERTED:
                    return new CoinInsertedState();
                default:
                    throw new UnsupportedOperationException();
            }
        }
    }

    private class IdleState implements VendingMachineState {
        @Override
        public void selectShelfNumber(ShelfNumber shelfNumber) {
            handleSelectingShelf(shelfNumber);
        }

        @Override
        public void insertCoin(Coin coin) {
            display = Display.selectProductFirst();
            transactionState = TransactionState.clear();
            changeDispenser = changeDispenser.put(coin);
        }

        @Override
        public void cancel() {
            // do nothing
        }
    }

    private class ShelfSelectedState implements VendingMachineState {
        @Override
        public void selectShelfNumber(ShelfNumber shelfNumber) {
            handleSelectingShelf(shelfNumber);
        }

        @Override
        public void insertCoin(Coin coin) {
            handleInsertingCoin(coin);
        }

        @Override
        public void cancel() {
            clear();
        }
    }

    private class CoinInsertedState implements VendingMachineState {
        @Override
        public void selectShelfNumber(ShelfNumber shelfNumber) {
            // do nothing
        }

        @Override
        public void insertCoin(Coin coin) {
            handleInsertingCoin(coin);
        }

        @Override
        public void cancel() {
            Coins transactionCoins = transactionState.coins();
            machineMoney = machineMoney.remove(transactionCoins);
            changeDispenser = changeDispenser.put(transactionCoins);
            clear();
        }
    }
}
