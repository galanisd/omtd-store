package eu.openminted.storage;

/**
 * 
 * @author galanisd
 *
 */
public class StoragePropertiesPITHOS extends StorageProperties{
	
	private String pithosToken;
	private String pithosUUID;
	private String pithosURL;
	
	
	public String getPithosToken() {
		return pithosToken;
	}
	
	public void setPithosToken(String pithosToken) {
		this.pithosToken = pithosToken;
	}
	
	public String getPithosUUID() {
		return pithosUUID;
	}
	
	public void setPithosUUID(String pithosUUID) {
		this.pithosUUID = pithosUUID;
	}
	
	public String getPithosURL() {
		return pithosURL;
	}
	
	public void setPithosURL(String pithosURL) {
		this.pithosURL = pithosURL;
	}	
}
