package tdd.vendingmachine.domain;

import lombok.ToString;
import tdd.vendingmachine.domain.dto.CoinDto;
import tdd.vendingmachine.domain.dto.VendingMachineDto;

import java.util.Collection;
import java.util.Objects;

@ToString
class VendingMachine {

    private final Shelves shelves;
    private final ChangeDispenser changeDispenser;
    private final MachineMoney machineMoney;
    private Display display;
    private TransactionState transactionState;
    private VendingMachineState state;

    private VendingMachine(Shelves shelves, MachineMoney machineMoney) {
        this.shelves = Objects.requireNonNull(shelves);
        this.machineMoney = Objects.requireNonNull(machineMoney);
        display = Display.empty();
        transactionState = TransactionState.clear();
        changeDispenser = ChangeDispenser.empty();
        state = new IdleState();
    }

    static VendingMachine create(VendingMachineDto vendingMachineDto) {
        Shelves shelves = Shelves.create(vendingMachineDto.getShelves());
        Denominations acceptableDenominations = Denominations.create(vendingMachineDto.getAcceptableDenominations());
        MachineMoney machineMoney = MachineMoney.createEmptyAndAcceptingDenominations(acceptableDenominations);
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
        return changeDispenser.dispense();
    }

    interface VendingMachineState {
        void selectShelfNumber(ShelfNumber shelfNumber);
        void insertCoin(Coin coin);
    }

    class IdleState implements VendingMachineState {
        @Override
        public void selectShelfNumber(ShelfNumber shelfNumber) {
            SelectedShelf selectedShelf = new ShelfSelector(shelves).select(shelfNumber);
            display = selectedShelf.display();
            transactionState = selectedShelf.newTransactionState();
            state = new ShelfSelectedState();
        }

        @Override
        public void insertCoin(Coin coin) {
            display = Display.selectProductFirst();
            transactionState = TransactionState.clear();
            changeDispenser.put(coin);
        }
    }

    class ShelfSelectedState implements VendingMachineState {
        @Override
        public void selectShelfNumber(ShelfNumber shelfNumber) {
            // it does nothing
        }

        @Override
        public void insertCoin(Coin coin) {
            if (isCoinNotAcceptable(coin)) {
                handleNotAcceptableCoin(coin);
                return;
            }
            transactionState = transactionState.add(coin);
            display = Display.money(transactionState.amountLeftToPay());
        }

        private boolean isCoinNotAcceptable(Coin coin) {
            return !machineMoney.isCoinAcceptable(coin);
        }

        private void handleNotAcceptableCoin(Coin coin) {
            display = Display.coinNotAcceptable();
            changeDispenser.put(coin);
        }
    }
}
