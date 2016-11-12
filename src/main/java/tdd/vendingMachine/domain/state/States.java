package tdd.vendingMachine.domain.state;

import tdd.vendingMachine.domain.VendingMachine;
import tdd.vendingMachine.domain.money.Coins;

public enum States implements State{

    BASE {
        @Override
        public State traySelected(VendingMachine context, int trayNo) {
            return TRAY_SELECTED;
        }

        @Override
        public State cancelSelected(VendingMachine context) {
            return BASE;
        }

        @Override
        public State coinInserted(VendingMachine context, Coins coin) {
            return this;
        }
    },
    TRAY_SELECTED {
        @Override
        public State traySelected(VendingMachine context, int trayNo) {
            return this;
        }

        @Override
        public State cancelSelected(VendingMachine context) {
            return BASE;
        }

        @Override
        public State coinInserted(VendingMachine context, Coins coin) {
            return COIN_INSERTED;
        }
    },
    COIN_INSERTED {
        @Override
        public State traySelected(VendingMachine context, int trayNo) {
            return this;
        }

        @Override
        public State cancelSelected(VendingMachine context) {
            return BASE;
        }

        @Override
        public State coinInserted(VendingMachine context, Coins coin) {
            return this;
        }
    }
}
