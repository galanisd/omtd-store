package eu.openminted.store.main;

import eu.openminted.store.config.Store;

public class AppTesterMain {
	// == = == = == == = == = == = == = == = == = == = == = ==
	public static void main(String args[]){
		
		String storeType = "";
		//storeType = Store.PITHOS;
		storeType = Store.LOCAL;
		
		AppTester runner = new AppTester(storeType);
		runner.executeTests();					 			 
	}
}
