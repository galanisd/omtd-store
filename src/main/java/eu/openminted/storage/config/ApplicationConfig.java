package eu.openminted.storage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import eu.openminted.storage.IdGenerator;
import eu.openminted.storage.SimpleIdGenerator;
import eu.openminted.storage.StoreProperties;
import eu.openminted.storage.StorePropertiesLocal;
import eu.openminted.storage.StorePropertiesPITHOS;
import eu.openminted.storage.StoreServiceGeneric;
import eu.openminted.storage.StoreServiceLocalDisk;
import eu.openminted.storage.StoreServicePITHOS;
import eu.openminted.storage.index.StoreIndex;
import eu.openminted.storage.index.StoreIndexDefault;

@Configuration
@PropertySource("${storeApplication.properties}")
public class ApplicationConfig {
		
	@Autowired
	private Environment environment;
	
	@Bean
	public StoreServiceGeneric getStorageService(){		
		
		StoreServiceGeneric storageService = null;		
		String type = environment.getProperty("storage.type");
				
		if(type.equalsIgnoreCase(Storage.PITHOS)){
			// Read PITHOS Storage properties.
			StorePropertiesPITHOS sp = new StorePropertiesPITHOS(); 				
			sp.setStorageRoot(environment.getProperty("storage.storageRoot"));
			sp.setPithosURL(environment.getProperty("storage.pithosURL"));		
			sp.setPithosToken(environment.getProperty("storage.pithosToken"));
			sp.setPithosUUID(environment.getProperty("storage.pithosUUID"));
			// Init storageService
			storageService = new StoreServicePITHOS(sp, getIdGenerator(), getStorageIndex());
		}else if(type.equalsIgnoreCase(Storage.LOCAL)){
			// Read Local Storage properties.
			StorePropertiesLocal sp = new StorePropertiesLocal();			
			sp.setStorageRoot(environment.getProperty("storage.storageRoot"));
			// Init storageService
			storageService = new StoreServiceLocalDisk(sp, getIdGenerator(), getStorageIndex());
		}		
				
		return storageService;
	}
	
	@Bean 
	public IdGenerator getIdGenerator(){
		return new SimpleIdGenerator();
	}		
	
	@Bean 
	public StoreIndex getStorageIndex(){
		return new StoreIndexDefault();
	}
	
}
