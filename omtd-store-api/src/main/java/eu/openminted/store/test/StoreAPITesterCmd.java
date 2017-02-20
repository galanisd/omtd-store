package eu.openminted.store.test;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;

import eu.openminted.store.config.ApplicationConfigParams;
import eu.openminted.store.config.Store;


/**
 * @author galanisd
 *
 */
public class StoreAPITesterCmd {
	
	
	// == = == = == == = == = == = == = == = == = == = == = ==
	public static void main(String args[]){
		// Disable restarts.
		System.setProperty("spring.devtools.restart.enabled", "false");
		
		String storeType = "";
				
		System.out.println("Please select FS. Press");
		System.out.println("1. For LOCAL ");
		System.out.println("2. For PITHOS ");
				
		final Scanner console = new Scanner(System.in);		
		final String command = console.nextLine().trim();
		
		if(command.equals("1")){			
			System.setProperty(ApplicationConfigParams.storeApplicationCfg, "classpath:/eu/openminted/store/config/configLocalDefault.properties");
		}else if(command.equals("2")){
			System.setProperty(ApplicationConfigParams.storeApplicationCfg, "classpath:/eu/openminted/store/config/configPITHOS.properties");
		} else{
			System.out.println("Not a valid selection of FS.");
			return;
		}		
		console.close();
				
		//StoreAPITester runner = new StoreAPITester(storeType);		
		//runner.executeTests();		
		
		//Run app within a Spring Context.
		SpringApplication springApplication = new SpringApplication(StoreAPITesterBoot.class);
		springApplication.setBannerMode(Banner.Mode.OFF);
		springApplication.setWebEnvironment(false);
		springApplication.run(args);	
		System.out.println(".");
	}
}
