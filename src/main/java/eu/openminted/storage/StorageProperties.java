package eu.openminted.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {
	
    /** Root folder for Storage */
    private String storageRoot = "upload-dir";

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
