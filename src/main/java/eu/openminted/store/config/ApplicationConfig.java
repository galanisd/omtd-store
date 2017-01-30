package eu.openminted.store.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import eu.openminted.store.StoreServiceGeneric;
import eu.openminted.store.StoreServiceLocalDisk;
import eu.openminted.store.StoreServicePITHOS;
import eu.openminted.store.idgenerator.IdGenerator;
import eu.openminted.store.idgenerator.SimpleIdGenerator;
import eu.openminted.store.metadata.StoreMetadata;
import eu.openminted.store.metadata.StoreMetadataDefault;

/**
 * @author galanisd
 *
 */
@Configuration
@PropertySource("${storeApplicationCfg}")
public class ApplicationConfig {
			
	private final static Logger log = LoggerFactory.getLogger(ApplicationConfig.class);
	
	@Autowired
	private Environment environment;
	
	@Bean
	public StoreServiceGeneric getStorageService(){						
		StoreServiceGeneric storageService = null;		
		String type = environment.getProperty("storage.type");
		
		log.info("storage.type:" + type);
		
		if(type.equalsIgnoreCase(Store.PITHOS)){
			// Read PITHOS Storage properties.
			StorePropertiesPITHOS sp = new StorePropertiesPITHOS(); 				
			sp.setStorageRoot(environment.getProperty(ApplicationConfigParams.storageRoot));
			sp.setPithosURL(environment.getProperty(ApplicationConfigParams.pithosURL));		
			sp.setPithosToken(environment.getProperty(ApplicationConfigParams.pithosToken));
			sp.setPithosUUID(environment.getProperty(ApplicationConfigParams.pithosUUID));
			// Init storageService
			storageService = new StoreServicePITHOS(sp, getIdGenerator(), getStoreMetadata());
		}else if(type.equalsIgnoreCase(Store.LOCAL)){
			// Read Local Storage properties.
			StorePropertiesLocal sp = new StorePropertiesLocal();			
			sp.setStorageRoot(environment.getProperty(ApplicationConfigParams.storageRoot));
			// Init storageService
			storageService = new StoreServiceLocalDisk(sp, getIdGenerator(), getStoreMetadata());
		}
				
		return storageService;
	}
	
	@Bean 
	public IdGenerator getIdGenerator(){
		return new SimpleIdGenerator();
	}		
	
	@Bean 
	public StoreMetadata getStoreMetadata(){
		return new StoreMetadataDefault();
	}
	
}
