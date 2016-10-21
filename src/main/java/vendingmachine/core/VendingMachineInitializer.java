package vendingmachine.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import vendingmachine.config.xsd.classes.ProductsInVendingMachine;
import vendingmachine.model.VendingMachine;

@Dependent
public class VendingMachineInitializer {

	private static final Logger LOG = LogManager.getLogger(VendingMachineInitializer.class);

	@Inject
	private VendingMachine vendingMachine;

	@Inject
	private ConfigToModelAssembler configToModelAssembler;

	public boolean init(String configPath) {
		return validateConfigFileAgainstXsd(configPath) && loadConfigFileAndCreateProducts(configPath);
	}

	private boolean validateConfigFileAgainstXsd(String configPath) {
		Source xmlFile = new StreamSource(new File(configPath));
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			Schema schema = schemaFactory.newSchema(new StreamSource(VendingMachineInitializer.class.getResourceAsStream("/xsd/VendingMachineConfig.xsd")));
			Validator validator = schema.newValidator();
			validator.validate(xmlFile);
			LOG.info("Config file '{}' conforms to xsd", configPath);
		} catch (SAXException e) {
			LOG.error("Config file '{}' does not conform to xsd", configPath, e);
			return false;
		} catch (FileNotFoundException e) {
			LOG.error("Config file '{}' not found", configPath, e);
			return false;
		} catch (IOException e) {
			LOG.error("I/O error during checking config file xsd validity", e);
			return false;
		}

		return true;
	}

	private boolean loadConfigFileAndCreateProducts(String configPath) {

		ProductsInVendingMachine productsInVendingMachine = null;
		try {
			productsInVendingMachine = loadConfigFileToJaxbObject(configPath);
		} catch (JAXBException e) {
			LOG.error("JAXB exception", e);
			return false;
		}

		configToModelAssembler.assemble(productsInVendingMachine, vendingMachine.getProducts());
		return true;
	}

	private static ProductsInVendingMachine loadConfigFileToJaxbObject(String configPath) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(ProductsInVendingMachine.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		ProductsInVendingMachine productsInVendingMachine = (ProductsInVendingMachine) jaxbUnmarshaller.unmarshal(new File(configPath));
		return productsInVendingMachine;

	}

}
