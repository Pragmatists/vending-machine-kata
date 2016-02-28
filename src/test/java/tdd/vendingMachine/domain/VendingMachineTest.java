package tdd.vendingMachine.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import tdd.vendingMachine.external_interface.Display;

import static org.mockito.Mockito.only;

@RunWith(MockitoJUnitRunner.class)
public class VendingMachineTest {

    @InjectMocks
    private VendingMachine machine;

    @Mock
    private Display displayMock;

    @Test
    public void should_display_welcome_message() throws Exception {
        Mockito.verify(displayMock, only()).displayMessage("Welcome! Please choose product:");
    }
}
