package eu.openminted.store.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

//@ConfigurationProperties("storage")
/**
 * @author galanisd
 *
 */
public abstract class StoreProperties {
	
    /** Root folder for Storage */
    private String storageRoot;

    /** Storage type */
    private String storageType;
    

    
    public String getStorageRoot() {
        return storageRoot;
    }

    public void setStorageRoot(String location) {
        this.storageRoot = location;
    }

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}
    
    
}
