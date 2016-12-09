package eu.openminted.store.controller;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"eu.openminted.store.controller", "eu.openminted.store.config"})
public class ApplicationBoot {
		
	public final static String applicationProperties = "storeApplication.properties";
	public final static String storagePropertiesFile = "storage.propertiesFile";
	
	public static void main(String[] args) {
		
		// If required load default applications properties.
		if(System.getProperty(applicationProperties) == null){
			
			Properties props = new Properties();						
			try{								
				props.load(ApplicationBoot.class.getResourceAsStream("myApp.properties"));
				System.setProperty(applicationProperties, props.getProperty(storagePropertiesFile));
			}catch(Exception e){
				e.printStackTrace();
			}								
		}
		
		// Run
		SpringApplication.run(ApplicationBoot.class, args);	
	}
	
	
}
