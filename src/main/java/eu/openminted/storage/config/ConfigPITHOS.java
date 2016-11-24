package eu.openminted.storage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import eu.openminted.storage.IdGenerator;
import eu.openminted.storage.SimpleIdGenerator;
import eu.openminted.storage.StorageProperties;
import eu.openminted.storage.StoragePropertiesPITHOS;
import eu.openminted.storage.StoreServiceLocalDisk;
import eu.openminted.storage.StoreServicePITHOS;
import eu.openminted.storage.index.StorageIndex;
import eu.openminted.storage.index.StorageIndexBasic;

@Configuration
@PropertySource("classpath:/eu/openminted/storage/config/configPITHOS.properties")
public class ConfigPITHOS {	
	
	@Autowired
	private Environment environment;
	
	@Bean
	public StoreServicePITHOS getStorageServicePITHOS(){		
		StoragePropertiesPITHOS sp = new StoragePropertiesPITHOS(); 		
		
		sp.setStorageRoot(environment.getProperty("storage.storageRoot"));
		sp.setPithosURL(environment.getProperty("storage.pithosURL"));		
		sp.setPithosToken(environment.getProperty("storage.pithosToken"));
		sp.setPithosUUID(environment.getProperty("storage.pithosUUID"));
		
		StoreServicePITHOS storageService = new StoreServicePITHOS(sp, getIdGenerator(), getStorageIndex());		
		return storageService;
	}
	
	@Bean 
	public IdGenerator getIdGenerator(){
		return new SimpleIdGenerator();
	}		
	
	@Bean 
	public StorageIndex getStorageIndex(){
		return new StorageIndexBasic();
	}

}
