package tdd.vendingMachine.domain.state;

import tdd.vendingMachine.domain.VendingMachine;
import tdd.vendingMachine.domain.display.Messages;
import tdd.vendingMachine.domain.money.Coins;
import tdd.vendingMachine.domain.money.MoneyBox;
import tdd.vendingMachine.domain.product.Products;
import tdd.vendingMachine.util.ChangeCalculator;

import java.util.EnumMap;

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
                int changeAmount = context.getMoneyBuffer().getTotalAmount() - product.getPrice();

                MoneyBox mergedMoneyBox = new MoneyBox(context.getMoneyBox());
                for (Coins coinType : Coins.values()) {
                    mergedMoneyBox.insert(coinType, context.getMoneyBuffer().getCoinCount(coinType));
                }

                MoneyBox changeBox = ChangeCalculator.calculateChange(mergedMoneyBox, changeAmount);
                if (changeBox == null) {
                    //unable to give change back
                }

                context.getDisplay().setMessage(
                    String.format(Messages.DISPENSING.getMessage(), product.name())
                ).display();

                for (Coins coinType : Coins.values()) {
                    context.getMoneyBox().insert(coinType, context.getMoneyBox().getCoinCount(coinType));
                }
                context.getMoneyBuffer().reset();
            }

            context.getDisplay().setMessage(
                String.format(
                    Messages.COINS_INSERTED.getMessage(),
                    product.name(),
                    (float) product.getPrice() / 100
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
    }
}
