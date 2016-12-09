package eu.openminted.store.controller;

import java.util.Properties;

import org.springframework.boot.SpringApplication;

public class Starter {
	
	public final static String applicationProperties = "storeApplicationCfg";
	public final static String storagePropertiesFile = "storage.propertiesFile";
	
	public static void main(String[] args) {
				
		// If required load default applications properties.
		String appPropFile = System.getProperty(applicationProperties);
		if(appPropFile == null){
						
			Properties props = new Properties();						
			try{								
				props.load(ApplicationBoot.class.getResourceAsStream("myApp.properties"));
				System.out.println("Loading default config:" + props.getProperty(storagePropertiesFile));
				System.setProperty(applicationProperties, props.getProperty(storagePropertiesFile));				
			}catch(Exception e){
				e.printStackTrace();
			}								
		}else{
			System.out.println("--Config:" + System.getProperty(applicationProperties));
		}
		
		// Run
		SpringApplication.run(ApplicationBoot.class, args);
	}

}
