package eu.openminted.storage.fsconnector;

import eu.openminted.storage.StorageProperties;
import eu.openminted.storage.StoragePropertiesPITHOS;
import eu.openminted.storage.config.Storage;

/**
 * A {@code FSConnector} builder.
 * @author galanisd
 *
 */
public class FSConnectorBuilder {
	
	/**
	 * Builds a new FS Connector
	 * @param type
	 * @param properties
	 * @return the {@code FSConnector}.
	 */
	public static FSConnector getConnector(String type, StorageProperties properties){
		if(type.equals(Storage.PITHOS)){
			gr.grnet.escience.commons.Utils.setDebug(false);
			StoragePropertiesPITHOS props = (StoragePropertiesPITHOS)properties;
			return	new FSConnectorPITHOS(props.getPithosURL(), props.getPithosToken(), props.getPithosUUID(), props.getStorageRoot().substring("pithos://".length()));				
		}else if(type.equals(Storage.LOCAL)){
			return  new FSConnectorLocal();
		}
		
		return null;
	}
}
