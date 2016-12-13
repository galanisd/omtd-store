package eu.openminted.store;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;

import eu.openminted.store.config.Store;
import eu.openminted.store.config.StorePropertiesLocal;
import eu.openminted.store.idgenerator.IdGenerator;
import eu.openminted.store.index.StoreIndex;

/**
 * An {@link eu.openminted.store.StoreService} implementation for local disk.
 * @author galanisd
 *
 */
public class StoreServiceLocalDisk extends StoreServiceGeneric{
		
	/**
	 * Constructor.
	 * @param storageProperties
	 * @param idGen
	 * @param storageIndex
	 */
	@Autowired
	public StoreServiceLocalDisk(StorePropertiesLocal storageProperties, IdGenerator idGen, StoreIndex storageIndex){		
		super(Store.LOCAL, storageProperties, idGen, storageIndex);		 
	}		
	
}
