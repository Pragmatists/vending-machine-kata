package tdd.vendingMachine;

/**
 * @author Yevhen Sukhomud
 */
public class ButtonPanel implements UserPanel {

    private HardwareController controller;

    public ButtonPanel(HardwareController controller) {
        this.controller = controller;
    }

    @Override
    public void selectProduct(int index) {
        controller.processSelectionProductFlow(index);
    }

    @Override
    public void cancel() {
        controller.cancel();
    }

}
