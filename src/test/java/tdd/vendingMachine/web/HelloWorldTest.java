package tdd.vendingMachine.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import tdd.vendingMachine.VendingMachine;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(HelloWorld.class)
@ContextConfiguration(classes = VendingMachine.class)
public class HelloWorldTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void says_hello() throws Exception {
		this.mvc.perform(get("/").accept(MediaType.TEXT_PLAIN))
			.andExpect(status().isOk()).andExpect(content().string("Hello world!"));
	}

}
