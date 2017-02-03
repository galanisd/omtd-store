package eu.openminted.store.config;

import java.io.File;

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
			sp.setStorageRoot(getStorageRoot());
			sp.setPithosURL(environment.getProperty(ApplicationConfigParams.pithosURL));		
			sp.setPithosToken(environment.getProperty(ApplicationConfigParams.pithosToken));
			sp.setPithosUUID(environment.getProperty(ApplicationConfigParams.pithosUUID));
			// Init storageService
			storageService = new StoreServicePITHOS(sp, getIdGenerator(), getStoreMetadata());
		}else if(type.equalsIgnoreCase(Store.LOCAL)){
			// Read Local Storage properties.
			StorePropertiesLocal sp = new StorePropertiesLocal();			
			sp.setStorageRoot(getStorageRoot());
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
	
	private String getStorageRoot(){
		String p = environment.getProperty(ApplicationConfigParams.storageRoot);
		if(p != null){
			return p;
		}else{
			String homeDir = System.getProperty("user.home");
			String OMTDStoreRoot =  homeDir + "/" + "OMTD/";
			File fStore = new File(OMTDStoreRoot);
			fStore.mkdir();
			log.info("Use home dir as storage root:" + fStore.getAbsolutePath());			
			return fStore.getAbsolutePath();
		}
	}
}
