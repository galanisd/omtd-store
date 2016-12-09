package eu.openminted.storage;

import org.springframework.beans.factory.annotation.Autowired;

import eu.openminted.storage.config.Storage;
import eu.openminted.storage.index.StoreIndex;


/**
 * An {@link eu.openminted.storage.StoreService} implementation for PITHOS storage.
 * @author galanisd
 *
 */
public class StoreServicePITHOS extends StoreServiceGeneric{
				
	@Autowired
	public StoreServicePITHOS(StorePropertiesPITHOS storageProperties, IdGenerator idGen, StoreIndex storageIndex){		
		super(Storage.PITHOS, storageProperties, idGen, storageIndex);
		
	}

}
