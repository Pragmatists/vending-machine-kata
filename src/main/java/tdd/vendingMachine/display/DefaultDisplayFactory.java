package tdd.vendingMachine.display;

/**
 * Created by okraskat on 06.02.16.
 */
public class DefaultDisplayFactory implements DisplayFactory {

    @Override
    public Display createDisplay() {
        return new DefaultDisplay();
    }
}
