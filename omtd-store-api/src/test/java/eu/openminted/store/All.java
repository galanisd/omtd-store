package eu.openminted.store;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import eu.openminted.store.config.ApplicationConfigurator;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	StoreTests.class
})
public class All {
	static{
		ApplicationConfigurator appConfigtr = new ApplicationConfigurator();
		appConfigtr.configure();
	}
}
