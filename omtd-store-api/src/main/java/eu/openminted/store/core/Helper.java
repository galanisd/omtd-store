package eu.openminted.store.core;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.openminted.store.metadata.StoreMetadata;

/**
 * @author galanisd
 *
 */
public class Helper {

	private static final Logger log = LoggerFactory.getLogger(Helper.class);
	
	public static String separator = "/";
		
	public static String getAbsolutePathForArchive(StoreMetadata storageIndex, String storageRoot, String archiveId){
		
		ArrayList<String> parents = storageIndex.getParentArchives(archiveId);
		
		String destinationFolderAbsolutePath = null;
		String rootLocation = null;
		
		// If needed build path with archives 
		if(parents != null){
		  			  
		  String relativePath = "";		  
		  for(int i = parents.size() - 1; i >= 0; i--){
			  relativePath = appendDirToPath(relativePath, parents.get(i));
		  }
		  
		  if(!storageRoot.endsWith(separator)){
			  rootLocation = storageRoot + relativePath.toString();
		  }else{
			  rootLocation = storageRoot + relativePath.toString().substring(1);
		  }
		  
		}else{
			rootLocation = storageRoot;			
		}		
		
		if(archiveId != null && !archiveId.isEmpty()){
			destinationFolderAbsolutePath = appendDirToPath(rootLocation, archiveId);
		}else{
			destinationFolderAbsolutePath = rootLocation;
		}
			
		
		return destinationFolderAbsolutePath;
	}
	
	public static String appendDirToPath(String path, String dir){
		String finalPath = "";
		
		if(path.endsWith(separator)){
			finalPath = path + dir + separator;
		}else{
			finalPath = path + separator + dir + separator;	
		}		
		
		return finalPath;
	}
		
	public static String appendFileToPath(String path, String file){

		String finalPath = "";
		if(path.endsWith(separator)){			
			finalPath = path + file;
		}else{
			finalPath = path + separator + file ;	
		}		
		
		return finalPath;
	}

}
