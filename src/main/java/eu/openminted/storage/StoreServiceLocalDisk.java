package eu.openminted.storage;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;

import eu.openminted.storage.config.Storage;
import eu.openminted.storage.index.StorageIndex;

/**
 * An {@link eu.openminted.storage.StoreService} implementation for local disk.
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
	public StoreServiceLocalDisk(StorageProperties storageProperties, IdGenerator idGen, StorageIndex storageIndex){		
		super(Storage.LOCAL, storageProperties, idGen, storageIndex);		 
	}		
	
}
