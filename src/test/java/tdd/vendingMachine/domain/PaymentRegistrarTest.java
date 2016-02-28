package tdd.vendingMachine.domain;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tdd.vendingMachine.domain.Money.createMoney;
import static tdd.vendingMachine.test_infrastructure.MethodCaller.callForEachArgument;

public class PaymentRegistrarTest {

    private static final Money ZERO = createMoney("0");

    private static final Money HALF = createMoney("0.5");

    private static final Money ONE = createMoney("1");

    private PaymentRegistrar paymentRegistrar;

    @Before
    public void setUp() throws Exception {
        paymentRegistrar = new PaymentRegistrar();
        paymentRegistrar.setAmountToBeCollected(ONE);
    }

    @Test
    public void should_register_payment() throws Exception {
        paymentRegistrar.register(ONE);

        assertThat(paymentRegistrar.getCollectedAmount()).isEqualTo(ONE);
    }

    @Test
    public void should_inform_that_sufficient_money_has_been_collected() throws Exception {
        callForEachArgument(money -> paymentRegistrar.register(money), HALF, HALF);

        assertThat(paymentRegistrar.hasSufficientMoneyBeenCollected()).isTrue();
    }

    @Test
    public void should_tell_how_much_more_needs_to_be_collected() throws Exception {
        paymentRegistrar.register(HALF);

        assertThat(paymentRegistrar.tellHowMuchMoreNeedsToBeCollected()).isEqualTo(HALF);
    }

    @Test
    public void should_return_zero_if_no_more_money_needs_to_be_collected() throws Exception {
        paymentRegistrar.register(createMoney("2"));

        assertThat(paymentRegistrar.tellHowMuchMoreNeedsToBeCollected()).isEqualTo(ZERO);
    }

    @Test
    public void should_reset_collected_amount() throws Exception {
        paymentRegistrar.register(HALF);
        paymentRegistrar.reset();

        assertThat(paymentRegistrar.getCollectedAmount()).isEqualTo(ZERO);
    }

    @Test
    public void should_reset_amount_to_be_collected() throws Exception {
        paymentRegistrar.register(HALF);
        paymentRegistrar.reset();

        assertThat(paymentRegistrar.getAmountToBeCollected()).isEqualTo(ZERO);
    }
}
