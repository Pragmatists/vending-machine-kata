package tdd.vendingMachine.domain.state;

import tdd.vendingMachine.domain.VendingMachine;
import tdd.vendingMachine.domain.display.Messages;
import tdd.vendingMachine.domain.money.Coins;
import tdd.vendingMachine.domain.product.Products;

public enum States implements State{

    BASE {
        @Override
        public State traySelected(VendingMachine context, int trayNo) {
            if (context.getSelectedTray() != null) {
                return TRAY_SELECTED;
            }

            if (context.getProductBox().getAvailableTrays() <= trayNo) {
                return BASE;
            }

            context.setSelectedTray(trayNo);
            Products product = context.getProductBox().getTray(context.getSelectedTray()).getProduct();

            context.getDisplay().setMessage(
                String.format(
                    Messages.PRODUCT_SELECTED.getMessage(),
                    product.name(),
                    (float) product.getPrice() / 100
                )
            ).display();

            return TRAY_SELECTED;
        }

        @Override
        public State cancelSelected(VendingMachine context) {
            context.setSelectedTray(null);
            context.getDisplay().setMessage(Messages.CANCELLED.getMessage()).display();
            //TODO -  real implementation would include transition state or timeout here
            context.getDisplay().setMessage(Messages.IDLE.getMessage()).display();

            return BASE;
        }

        @Override
        public State coinInserted(VendingMachine context, Coins coin) {
            return BASE;
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
