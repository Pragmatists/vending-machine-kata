package tdd.vendingMachine;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class VendingMachineInjector extends AbstractModule {

    private String key = "";

    @Override
    protected void configure() {
        bind(Display.class).to(DisplayImpl.class);
        bind(CashRegister.class).to(CashRegisterImpl.class);
        bind(ChangeDispenser.class).to(ChangeDispenserImpl.class);
        bind(ProductDispenser.class).to(ProductDispenserImpl.class);
        bind(VendingMachine.class).to(VendingMachineImpl.class);
        bind(PasswordEncoder.class).toInstance(new BCryptPasswordEncoder()); // Probably overkill
    }

    @Provides
    @Named("key")
    public String getKey() {
        return this.key;
    }

    void setKey(String key) {
        this.key = key;
    }
}
