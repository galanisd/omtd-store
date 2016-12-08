package eu.openminted.storage;

import java.util.ArrayList;

import eu.openminted.storage.index.StorageIndex;

/**
 * @author galanisd
 *
 */
public class Helper {

	public static String separator = "/";
	
	public static String getAbsolutePathForArchive(StorageIndex storageIndex, String storageRoot, String archiveId){
		
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
		
		destinationFolderAbsolutePath = appendDirToPath(rootLocation, archiveId);
		
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
