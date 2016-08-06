package tdd.vendingMachine;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import tdd.vendingMachine.machine.MachineFacade;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = VendingMachineApplication.class)
public class VendingMachingApplicationTest {

    @Autowired
    private MachineFacade machineFacade;

    @Test
    public void application_run() {
        Assertions.assertThat(machineFacade).isNotNull();
    }

}
