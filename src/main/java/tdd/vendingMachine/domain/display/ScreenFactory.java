package tdd.vendingMachine.domain.display;

import tdd.vendingMachine.domain.display.screen.impl.*;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author kdkz
 */
public class ScreenFactory {

    public enum ScreenType {

        CANCEL_SCREEN,
        INSERTED_COINS_STATUS_SCREEN,
        PRODUCT_SOLD_SCREEN,
        SELECTED_SHELF_SCREEN,
        SELECT_SHELF_FIRST_SCREEN,
        UNABLE_TO_COUNT_REST_SCREEN,
        IDLE_SCREEN
    }

    private Display display = new Display();

    public void displayScreen(ScreenType screenType, Supplier<List<String>> messageArguments) {

        switch (screenType){
            case CANCEL_SCREEN:
                display.show(new CancelScreen());
                break;
            case INSERTED_COINS_STATUS_SCREEN:
                display.show(new InsertedCoinsStatusScreen(messageArguments.get()));
                break;
            case PRODUCT_SOLD_SCREEN:
                display.show(new ProductSoldScreen(messageArguments.get()));
                break;
            case SELECTED_SHELF_SCREEN:
                display.show(new SelectedShelfScreen(messageArguments.get()));
                break;
            case SELECT_SHELF_FIRST_SCREEN:
                display.show(new SelectShelfFirstScreen());
                break;
            case UNABLE_TO_COUNT_REST_SCREEN:
                display.show(new UnableToCountRestScreen());
                break;
            default:
                display.show(new IdleScreen());
        }

    }

}
