package eu.openminted.store.core;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.openminted.store.metadata.StoreMetadata;

/**
 * @author galanisd
 *
 */
public class Helper {

	private static final Logger log = LoggerFactory.getLogger(Helper.class);

	public static String getAbsolutePathForArchive(StoreMetadata storageIndex, String storageRoot, String archiveId){

		ArrayList<String> parents = storageIndex.getParentArchives(archiveId);
		
		String destinationFolderAbsolutePath = null;
		String rootLocation = null;
		
		// If needed build path with archives 
		if(parents != null){
			Collections.reverse(parents); // FIXME reverse here or inside "storageIndex.getParentArchives(archiveId);" ??
			String relativePath = String.join("/", parents);
			rootLocation = Paths.get(storageRoot, relativePath).toString();

		}else{
			rootLocation = storageRoot;
		}
		
		if(archiveId != null && !archiveId.isEmpty()){
			destinationFolderAbsolutePath = Paths.get(rootLocation, archiveId).toString();
		}else{
			destinationFolderAbsolutePath = rootLocation;
		}
			
		
		return destinationFolderAbsolutePath;
	}

	public static String appendToPath(String path, String elem){ return Paths.get(path, elem).toString(); }

}
