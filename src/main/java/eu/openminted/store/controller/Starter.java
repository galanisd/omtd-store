package eu.openminted.store.controller;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Starts the application. 
 * @author galanisd
 *
 */
public class Starter {
	
	private static final Logger log = LoggerFactory.getLogger(Starter.class);
	
	public final static String applicationProperties = "storeApplicationCfg";
	public final static String storagePropertiesFile = "storage.propertiesFile";
	public final static String defaultApplicationProperties = "myApp.properties";
	
	public static void main(String[] args) {
		
		// Disable restarts.
		System.setProperty("spring.devtools.restart.enabled", "false");
		
		// If required load default applications properties.
		String appPropFile = System.getProperty(applicationProperties);
		if(appPropFile == null){						
			Properties props = new Properties();						
			try{								
				props.load(ApplicationBoot.class.getResourceAsStream(defaultApplicationProperties));
				log.info("==> Loading default config:" + props.getProperty(storagePropertiesFile));
				System.setProperty(applicationProperties, props.getProperty(storagePropertiesFile));				
			}catch(Exception e){
				log.info("ERROR:", e);
			}								
		}else{
			log.info("==> Config:" + System.getProperty(applicationProperties));
		}
		
		// Run app within a Spring Context.
		SpringApplication springApplication = new SpringApplication(ApplicationBoot.class);
		springApplication.run();
	}

}
