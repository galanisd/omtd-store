package eu.openminted.store.test;

import java.util.Scanner;

import eu.openminted.store.config.Store;

/**
 * @author galanisd
 *
 */
public class StoreAPITesterMain {
	
	// == = == = == == = == = == = == = == = == = == = == = ==
	public static void main(String args[]){
		
		String storeType = "";
				
		System.out.println("Please select FS. Press");
		System.out.println("1. For LOCAL ");
		System.out.println("2. For PITHOS ");
				
		final Scanner console = new Scanner(System.in);		
		final String command = console.nextLine().trim();
		
		if(command.equals("1")){
			storeType = Store.LOCAL;
		}else if(command.equals("2")){
			storeType = Store.PITHOS;
		} else{
			System.out.println("Not a valid selection of FS.");
			return;
		}
		
		StoreAPITester runner = new StoreAPITester(storeType);
		runner.init();
		runner.executeTests();		
		
		console.close();
	}
}