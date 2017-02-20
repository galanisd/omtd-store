package eu.openminted.store.core;

import org.springframework.beans.factory.annotation.Autowired;

import eu.openminted.store.config.Store;
import eu.openminted.store.config.StorePropertiesPITHOS;
import eu.openminted.store.idgenerator.IdGenerator;
import eu.openminted.store.metadata.StoreMetadata;


/**
 * An {@link eu.openminted.store.core.StoreService} implementation for PITHOS storage.
 * @author galanisd
 *
 */
public class StoreServicePITHOS extends StoreServiceGeneric{
				
	/**
	 * Constructor.
	 * @param storageProperties
	 * @param idGen
	 * @param storageIndex
	 */
	@Autowired
	public StoreServicePITHOS(StorePropertiesPITHOS storageProperties, IdGenerator idGen, StoreMetadata storageIndex){		
		super(Store.PITHOS, storageProperties, idGen, storageIndex);
		
	}

}
