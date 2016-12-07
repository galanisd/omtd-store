package eu.openminted.storage.fsconnector;

import eu.openminted.storage.StorageProperties;
import eu.openminted.storage.StoragePropertiesPITHOS;

/**
 * A {@code FSConnector} builder.
 * @author galanisd
 *
 */
public class FSConnectorBuilder {
	
	public final static String PITHOS = "PITHOS";
	public final static String LOCAL = "LOCAL";
	
	/**
	 * Builds a new FS Connector
	 * @param type
	 * @param properties
	 * @return the {@code FSConnector}.
	 */
	public static FSConnector getConnector(String type, StorageProperties properties){
		if(type.equals(PITHOS)){
			gr.grnet.escience.commons.Utils.setDebug(false);
			StoragePropertiesPITHOS props = (StoragePropertiesPITHOS)properties;
			return	new FSConnectorPITHOS(props.getPithosURL(), props.getPithosToken(), props.getPithosUUID());				
		}else if(type.equals(LOCAL)){
			return  new FSConnectorLocal();
		}
		
		return null;
	}
}
