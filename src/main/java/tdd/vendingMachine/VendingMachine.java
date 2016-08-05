package tdd.vendingMachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan("tdd.vendingMachine")
public class VendingMachine {

	public static void main(String[] args) {
		SpringApplication.run(VendingMachine.class, args);
	}

}
