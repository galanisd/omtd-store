package eu.openminted.store.fsconnector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.openminted.utils.files.DirCompressor;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


/**
 * 
 * @author galanisd
 *
 */
public class FSConnectorLocal implements FSConnector{
	
	private static final Logger log = LoggerFactory.getLogger(FSConnectorLocal.class);	
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
			log.debug("deleting->" + fileName);
			Path path = Paths.get(fileName);
			return path.toFile().delete();
		}catch(Exception e){
			log.debug("ERROR:", e);
			return false;
		}
	}

	@Override
	public String listAllFiles() {
		String list = "";
		File root = new File(localRoot);
		ArrayList<File> allFiles = listFileTree(root);
		Collections.sort(allFiles);
		for(File f : allFiles){
			list = list + f.getAbsolutePath().substring(this.localRoot.length()) + "\n";
		}
		
		return list;
	}

	@Override
	public List<String> listFiles(String path, boolean listDirectories, boolean recursive, boolean ignoreZips) {
        List<String> list = new ArrayList<>();
        File dir = new File(localRoot+path);
        List<File> allFiles;
        if(recursive) {
            allFiles = listFileTree(dir); // returns all files in directory recursively
        } else {
            allFiles = Arrays.asList(dir.listFiles()); // returns files in directory
        }
        Collections.sort(allFiles);
        if(ignoreZips) {
            List<File> files = new ArrayList<>();
//            for(int i = 0; i<allFiles.size(); i++) {
//                if(!allFiles.get(i).toString().toLowerCase().endsWith(".zip")) {
//                        files.add(allFiles.get(i));
//                }
//            }
            allFiles.forEach(f -> {
                if (!f.toString().toLowerCase().endsWith(".zip")) {
                    files.add(f);
                }
            });
            allFiles = files;
        }
        if(listDirectories) { // if user wants to retrieve directories
            for (File f : allFiles) {
                list.add(f.getAbsolutePath().substring(this.localRoot.length()));
            }
        } else { // if user does not want to retrieve directories
            for (File f : allFiles) {
                if(f.isDirectory())
                    continue;
                list.add(f.getAbsolutePath().substring(this.localRoot.length()));
            }
        }

        return list;

	}

	@Override
	public List<String> listFiles(String path, int from, int size) {
        List<String> list = new ArrayList<>();
        File dir = new File(localRoot+path);
        File[] files = dir.listFiles();
        Arrays.sort(files);
        // get absolute values to avoid errors with negative numbers.
        from = Math.abs(from);
        size = Math.abs(size);
        if(files != null) {
            if((from + size) < files.length) {
                for (int i = from; i < from + size; i++) {
                    list.add(files[i].getName());
                }
            } else {
                for (int i = from; i < files.length; i++) {
                    list.add(files[i].getName());
                }
            }
        }

        return list;

	}

    public static ArrayList<File> listFileTree(File dir) {
		ArrayList<File> fileTree = new ArrayList<File>();
		
	    if(dir == null || dir.listFiles() == null){
	        return fileTree;
	    }
	    
	    for (File entry : dir.listFiles()) {
	        if (entry.isFile()) {
	        	fileTree.add(entry);
	        }else {
	        	fileTree.add(entry);
	        	fileTree.addAll(listFileTree(entry));
	        }
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
				if(!success){
					status = false;
					log.info("failed deleting:" + file.getAbsolutePath());
				}
			}else{
				status = file.delete();
				if(!status){
					log.info("failed deleting:" + file.getAbsolutePath());
				}
			}
		}
		
		return status;
	}

	@Override
	public InputStream download(String targetFileName) {
		FileInputStream fis = null;
		try{
			Path path = Paths.get(targetFileName);
			log.info("Opened FileInputStream:" + path.toFile().getAbsolutePath());
			fis = new FileInputStream(path.toFile());					
		}catch(Exception e){
			log.debug("Error on downloading", e);
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
				log.debug("ERROR:", e);
				status = false;
			}			
			return status;
		}else{
			status = Paths.get(folder).toFile().delete();
		}
		
		return status;
	}

	@Override
	public boolean compressDir(String dir, String zipFile) {
		try{
			DirCompressor compressor = new DirCompressor(localRoot);
			return compressor.zipDir(zipFile, dir);			
		}catch(Exception e){
			log.debug("ERROR", e);
			return false;	
		}
		
	}

	@Override
	public boolean isDir(String path) {
		File f = new File(path);
		if(f.exists() && f.isDirectory()){
			return true;
		}
		
		return false;
	}

	@Override
	public boolean exists(String path) {
		File f = new File(path);
		if(f.exists() ){
			return true;
		}
		
		return false;
	}

	@Override
	public boolean copyContent(String src, String dst) {
		File from = new File(src);
        File to = new File(dst);
        try {
            FileUtils.copyDirectory(from, to);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}

	@Override
	public boolean moveFile(String sourceFolderAbsolutePathForParent, String destinationFolderAbsolutePathForParent) {
		return false;
	}


}
