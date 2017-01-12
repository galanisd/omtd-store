package eu.openminted.store.metadata;

import java.util.ArrayList;

/**
 * An interface for a storage index.
 * @author galanisd
 *
 */
public interface StoreMetadata {

	/**
	 * Find parent archive.
	 * @param archiveId
	 * @return
	 */
	public String getParentArchive(String archiveId);

	/**
	 * Find the parent archives.
	 * @param archiveId
	 * @return a bottom-to-top list of the parent archives. 
	 */
	public ArrayList<String> getParentArchives(String archiveId);
	
	/**
	 * Add child archive.
	 * @param parentId
	 * @param childId
	 */
	public void addChildArchive(String parentId, String childId);
}
