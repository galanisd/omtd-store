package eu.openminted.store.controller;

import java.util.Properties;

import org.springframework.boot.SpringApplication;

/**
 * Starts the application. 
 * @author galanisd
 *
 */
public class Starter {
	
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
				System.out.println("==> Loading default config:" + props.getProperty(storagePropertiesFile));
				System.setProperty(applicationProperties, props.getProperty(storagePropertiesFile));				
			}catch(Exception e){
				e.printStackTrace();
			}								
		}else{
			System.out.println("==> Config:" + System.getProperty(applicationProperties));
		}
		
		// Run app within a Spring Context.
		SpringApplication springApplication = new SpringApplication(ApplicationBoot.class);
		springApplication.run();
	}

}
