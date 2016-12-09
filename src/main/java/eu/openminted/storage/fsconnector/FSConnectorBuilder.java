package eu.openminted.storage.fsconnector;

import eu.openminted.storage.StoreProperties;
import eu.openminted.storage.StorePropertiesLocal;
import eu.openminted.storage.StorePropertiesPITHOS;
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
	public static FSConnector getConnector(String type, StoreProperties properties){
		if(type.equals(Storage.PITHOS)){
			gr.grnet.escience.commons.Utils.setDebug(false);
			StorePropertiesPITHOS props = (StorePropertiesPITHOS)properties;
			return	new FSConnectorPITHOS(props.getPithosURL(), props.getPithosToken(), props.getPithosUUID(), props.getStorageRoot());				
		}else if(type.equals(Storage.LOCAL)){
			StorePropertiesLocal props = (StorePropertiesLocal)properties;
			return  new FSConnectorLocal(props.getStorageRoot());
		}
		
		return null;
	}
}
