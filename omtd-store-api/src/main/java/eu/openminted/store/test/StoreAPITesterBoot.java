package eu.openminted.store.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"eu.openminted.store.config", "eu.openminted.store", "eu.openminted.store.test"})
public class StoreAPITesterBoot implements CommandLineRunner{
	
	private static final Logger log = LoggerFactory.getLogger(StoreAPITesterBoot.class);
	
	@Autowired
	private StoreAPITester tester;
	
	@Override
	public void run(String... args) throws Exception {
		//just call tests.
		tester.executeTests();
		log.info("FINISHED");
	}
}
