package eu.openminted.store.fsconnector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

import org.apache.commons.io.FileUtils;

/**
 * 
 * @author galanisd
 *
 */
public class FSConnectorLocal implements FSConnector{

	private String localRoot;
		
	public FSConnectorLocal(String localRoot) {
		super();
		this.localRoot = localRoot;
	}

	public boolean makeFolder(String fileName){
    	// Make folder			
		Path destinationFolderAboslutePath = Paths.get(fileName);
    	boolean status = destinationFolderAboslutePath.toFile().mkdirs();    	
    	return status;
	}

	@Override
	public boolean storeFile(String targetFileName, InputStream is) {
		
		try{
			Path path = Paths.get(targetFileName);
			path.toFile().getParentFile().mkdirs();		
			Files.copy(is, path);
		}catch(Exception e){
			return false;
		}
		
		return true;
	}

	@Override
	public boolean deleteFile(String fileName) {
		try{			
			Path path = Paths.get(fileName);
			return path.toFile().delete();
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public String listAllFiles() {
		String list = "";
		File root = new File(localRoot);
		ArrayList<File> allFiles = listFileTree(root);
		for(File f : allFiles){
			list = list + f.getAbsolutePath() + "\n";
		}
		
		return list;
	}

	public static ArrayList<File> listFileTree(File dir) {
		ArrayList<File> fileTree = new ArrayList<File>();
	    if(dir==null || dir.listFiles()==null){
	        return fileTree;
	    }
	    for (File entry : dir.listFiles()) {
	        if (entry.isFile()) fileTree.add(entry);
	        else fileTree.addAll(listFileTree(entry));
	    }
	    return fileTree;
	}
	
	@Override
	public boolean deleteAll() {
		boolean status = true;
		
		File root = new File(localRoot);
		File [] files = root.listFiles();		
		for(File file : files){
			if(file.isDirectory()){
				boolean success = deleteFolder(file.getAbsolutePath(), true);
				if(!status){
					status = true;
				}
			}else{
				status = file.delete();
			}
		}
		
		return status;
	}

	@Override
	public InputStream download(String targetFileName) {
		FileInputStream fis = null;
		
		try{
			Path path = Paths.get(targetFileName);
			fis = new FileInputStream(path.toFile());					
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return fis;		
	}

	@Override
	public boolean deleteFolder(String folder, boolean recursively) {
		boolean status = true;
		if(recursively){
			
			try{
				FileUtils.forceDelete(new File(folder));
			}catch(Exception e){
				status = false;
			}
			
			return false;
		}else{
			status = Paths.get(folder).toFile().delete();			
		}
		
		return status;
	}
}
