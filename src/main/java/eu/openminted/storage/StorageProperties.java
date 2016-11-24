package eu.openminted.storage;

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {
	
    /**
     * Root folder for Storage.
     */
    private String storageRoot = "upload-dir";    

    public String getStorageRoot() {
        return storageRoot;
    }

    public void setStorageRoot(String location) {
        this.storageRoot = location;
    }
    
    
}
