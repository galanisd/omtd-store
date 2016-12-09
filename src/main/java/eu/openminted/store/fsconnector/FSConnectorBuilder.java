package eu.openminted.store.fsconnector;

import eu.openminted.store.StoreProperties;
import eu.openminted.store.StorePropertiesLocal;
import eu.openminted.store.StorePropertiesPITHOS;
import eu.openminted.store.config.Storage;

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
