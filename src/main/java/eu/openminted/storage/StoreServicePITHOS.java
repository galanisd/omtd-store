package eu.openminted.storage;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

import eu.openminted.storage.fsconnector.FSConnectorBuilder;
import eu.openminted.storage.index.StorageIndex;


/**
 * An {@link eu.openminted.storage.StoreService} implementation for PITHOS storage.
 * @author galanisd
 *
 */
public class StoreServicePITHOS extends StoreServiceGeneric{
				
	@Autowired
	public StoreServicePITHOS(StoragePropertiesPITHOS storageProperties, IdGenerator idGen, StorageIndex storageIndex){		
		super(FSConnectorBuilder.PITHOS, storageProperties, idGen, storageIndex);
		
	}

}
