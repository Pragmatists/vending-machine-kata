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
            if (context.getProductBox().getAvailableTrays() <= trayNo) {
                return BASE;
            } else if (context.getProductBox().getTray(trayNo).isEmpty()) {
                context.getDisplay().setMessage(Messages.PRODUCT_TRAY_EMPTY.getMessage()).display();

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
            return COIN_INSERTED.coinInserted(context, coin);
        }
    },
    COIN_INSERTED {
        @Override
        public State traySelected(VendingMachine context, int trayNo) {
            return COIN_INSERTED;
        }

        @Override
        public State cancelSelected(VendingMachine context) {
            context.setSelectedTray(null);
            context.getDisplay().setMessage(
                String.format(
                    Messages.CANCELLED_WITH_RETURN.getMessage(),
                    (float) context.getMoneyBuffer().getTotalAmount()
                )
            ).display();

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

        private State processPurchase(VendingMachine context, Products product) {
            int changeAmount = context.getMoneyBuffer().getTotalAmount() - product.getPrice();

            MoneyBox mergedMoneyBox = new MoneyBox(context.getMoneyBox()).mergeWith(context.getMoneyBuffer());

            MoneyBox changeBox = ChangeCalculator.calculateChange(mergedMoneyBox, changeAmount);
            if (changeBox == null) {
                context.getDisplay().setMessage(
                    String.format(
                        Messages.NOT_ENOUGH_MONEY_TO_GIVE_BACK_CHANGE.getMessage(),
                        (float) context.getMoneyBuffer().getTotalAmount() / 10)
                ).display();
                context.setSelectedTray(null);

                context.getDisplay().setMessage(Messages.IDLE.getMessage()).display();

                return BASE;
            }

            dispenseProduct(context, product);
            returnChange(context, changeBox);

            context.getDisplay().setMessage(Messages.IDLE.getMessage()).display();

            return BASE;
        }

        private void dispenseProduct(VendingMachine context, Products product) {
            context.getProductBox().getTray(context.getSelectedTray()).removeProduct(1);
            context.setSelectedTray(null);
            context.getDisplay().setMessage(
                String.format(Messages.DISPENSING.getMessage(), product.name())
            ).display();
        }

        private void returnChange(VendingMachine context, MoneyBox changeBox) {
            context.getMoneyBox().mergeWith(context.getMoneyBuffer());
            context.getMoneyBuffer().reset();

            for (Coins coin : Coins.values()) {
                context.getMoneyBox().remove(coin, changeBox.getCoinCount(coin));
            }

            context.getDisplay().setMessage(
                String.format(Messages.GIVING_BACK_CHANGE.getMessage(), (float) changeBox.getTotalAmount() / 10)
            ).display();
        }
    }
}
