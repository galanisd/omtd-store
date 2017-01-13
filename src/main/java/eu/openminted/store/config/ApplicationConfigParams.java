package eu.openminted.store.config;

/**
 * @author galanisd
 *
 */
public class ApplicationConfigParams {
	
	public final static String storageRoot = "storage.storageRoot";
	
	public final static String pithosURL = "storage.pithosURL";
	public final static String pithosToken = "storage.pithosToken";
	public final static String pithosUUID = "storage.pithosUUID";
	
	/** Specifies the file that holds configuration of the store service app */
	public final static String storeApplicationCfg = "storeApplicationCfg";
	
	/** Specifies the file that contains a reference to the default {@code storeApplicationCfg} */
	public final static String storeApplicationCfgDefaultHolder = "/eu/openminted/store/config/storeApplicationCfgDefaultHolder.properties";
}
