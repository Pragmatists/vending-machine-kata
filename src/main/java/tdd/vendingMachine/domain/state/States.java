package tdd.vendingMachine.domain.state;

import tdd.vendingMachine.domain.VendingMachine;
import tdd.vendingMachine.domain.display.Messages;
import tdd.vendingMachine.domain.money.Coins;
import tdd.vendingMachine.domain.money.MoneyBox;
import tdd.vendingMachine.domain.product.Products;
import tdd.vendingMachine.util.ChangeCalculator;

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
                    (float) product.getPrice() / 10
                )
            ).display();

            return TRAY_SELECTED;
        }

        @Override
        public State cancelSelected(VendingMachine context) {
            context.setSelectedTray(null);
            context.getDisplay().setMessage(Messages.CANCELLED.getMessage()).display();
            //real implementation would include transition state or timeout here
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
            if (context.getSelectedTray() != null) {
                return TRAY_SELECTED;
            } else {
                return BASE;
            }
        }

        @Override
        public State cancelSelected(VendingMachine context) {
            context.setSelectedTray(null);
            context.getDisplay().setMessage(Messages.CANCELLED.getMessage()).display();
            //real implementation would include transition state or timeout here
            context.getDisplay().setMessage(Messages.IDLE.getMessage()).display();

            return BASE;
        }

        @Override
        public State coinInserted(VendingMachine context, Coins coin) {
            context.getMoneyBuffer().insert(coin, 1);
            Products product = context.getProductBox().getTray(context.getSelectedTray()).getProduct();

            //over price, dispense
            if (context.getMoneyBuffer().getTotalAmount() >= product.getPrice()) {
                return processPurchase(context, product);
            }

            context.getDisplay().setMessage(
                String.format(
                    Messages.COINS_INSERTED.getMessage(),
                    (float) context.getMoneyBuffer().getTotalAmount() / 10,
                    (float) (product.getPrice() - context.getMoneyBuffer().getTotalAmount()) / 10
                )
            ).display();

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
    };

    private static State processPurchase(VendingMachine context, Products product) {
        int changeAmount = context.getMoneyBuffer().getTotalAmount() - product.getPrice();

        MoneyBox mergedMoneyBox = new MoneyBox(context.getMoneyBox()).mergeWith(context.getMoneyBuffer());

        MoneyBox changeBox = ChangeCalculator.calculateChange(mergedMoneyBox, changeAmount);
        if (changeBox == null) {
            context.getDisplay().setMessage(
                String.format(
                    Messages.NOT_ENOUGH_MONEY_TO_GIVE_BACK_CHANGE.getMessage(),
                    context.getMoneyBuffer().getTotalAmount())
            ).display();
            context.setSelectedTray(null);

            return BASE;
        }

        dispenseProduct(context, product);
        returnChange(context, changeAmount);

        return BASE;
    }

    private static void dispenseProduct(VendingMachine context, Products product) {
        context.getProductBox().getTray(context.getSelectedTray()).removeProduct(1);
        context.setSelectedTray(null);
        context.getDisplay().setMessage(
            String.format(Messages.DISPENSING.getMessage(), product.name())
        ).display();
    }

    private static void returnChange(VendingMachine context, int changeAmount) {
        context.getMoneyBox().mergeWith(context.getMoneyBuffer());
        context.getMoneyBuffer().reset();
        context.getDisplay().setMessage(
            String.format(Messages.GIVING_BACK_CHANGE.getMessage(), changeAmount)
        ).display();
    }
}
