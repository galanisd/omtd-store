package eu.openminted.store.metadata;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A basic implementation of {@link eu.openminted.store.metadata.StoreMetadata}
 * @author galanisd
 *
 */
public class StoreMetadataDefault implements StoreMetadata{

	public HashMap<String, String> archivesStructure;
	
	public StoreMetadataDefault(){
		archivesStructure = new HashMap<String, String>();
	}
	
	@Override
	public String getParentArchive(String archiveId) {
		return archivesStructure.get(archiveId);
	}

	@Override
	public void addChildArchive(String parentId, String childId) {
		archivesStructure.put(childId, parentId);		
	}

	@Override
	public ArrayList<String> getParentArchives(String archiveId) {
		ArrayList<String> archivesList = new ArrayList<String>();
		
		String current = archiveId;
		
		String parent = archivesStructure.get(current);		
		while(parent != null){
			archivesList.add(parent);
			current = parent;
			parent = archivesStructure.get(current);
		}
		
		if(archivesList.size() == 0){
			return null;
		}else{
			return archivesList;
		}
	}

}
