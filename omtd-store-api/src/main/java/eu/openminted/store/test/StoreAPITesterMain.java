package eu.openminted.store.test;

import java.util.Scanner;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;

import eu.openminted.store.config.ApplicationConfigParams;
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
			//System.setProperty(ApplicationConfigParams.storeApplicationCfg, "classpath:/eu/openminted/store/config/configLocalDefault.properties");
		}else if(command.equals("2")){
			storeType = Store.PITHOS;
			//System.setProperty(ApplicationConfigParams.storeApplicationCfg, "classpath:/eu/openminted/store/config/configPITHOS.properties");
		} else{
			System.out.println("Not a valid selection of FS.");
			return;
		}		
		console.close();
				
		StoreAPITester runner = new StoreAPITester(storeType);		
		runner.executeTests();
		
		// TO-DO: Replace the above block with this.
		// Run app within a Spring Context.
		//SpringApplication springApplication = new SpringApplication(StoreAPITester.class);
		//springApplication.setBannerMode(Banner.Mode.OFF);
		//springApplication.run();
		
	}
}
