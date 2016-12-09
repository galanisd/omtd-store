package eu.openminted.store;

import org.springframework.beans.factory.annotation.Autowired;

import eu.openminted.store.config.Storage;
import eu.openminted.store.index.StoreIndex;


/**
 * An {@link eu.openminted.store.StoreService} implementation for PITHOS storage.
 * @author galanisd
 *
 */
public class StoreServicePITHOS extends StoreServiceGeneric{
				
	@Autowired
	public StoreServicePITHOS(StorePropertiesPITHOS storageProperties, IdGenerator idGen, StoreIndex storageIndex){		
		super(Storage.PITHOS, storageProperties, idGen, storageIndex);
		
	}

}
