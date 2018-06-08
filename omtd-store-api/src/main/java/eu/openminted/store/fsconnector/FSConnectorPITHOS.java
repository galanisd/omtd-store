package eu.openminted.store.fsconnector;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import eu.openminted.utils.files.DirCompressor;
import gr.grnet.escience.commons.Utils;
import gr.grnet.escience.fs.pithos.PithosFileSystem;
import gr.grnet.escience.fs.pithos.PithosObject;
import gr.grnet.escience.fs.pithos.PithosPath;
//import eu.openminted.storage.fsconnector.debug.HadoopPithosConnector;
import gr.grnet.escience.pithos.rest.HadoopPithosConnector;
import org.apache.commons.compress.compressors.FileNameUtil;
import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author galanisd
 *
 */
public class FSConnectorPITHOS implements FSConnector {
	
	private static final Logger log = LoggerFactory.getLogger(FSConnectorPITHOS.class);
	
	private HadoopPithosConnector connector;
	private String workingContainer = "";
	public final String PITHOSURI = "pithos://"; 			
	
	private String pithosRoot;

	/**
	 * Constructor.
	 * 
	 * @param pithosUrl
	 * @param pithosToken
	 * @param uuid
	 */
	public FSConnectorPITHOS(String pithosUrl, String pithosToken, String uuid, String pithosRoot) {
		try {
			this.pithosRoot = pithosRoot;
			this.workingContainer = pithosRoot.substring(PITHOSURI.length(), pithosRoot.length()-1);
			log.info("workingContainer->" + workingContainer);
			connector = new HadoopPithosConnector(pithosUrl, pithosToken, uuid);
			log.info(connector.getUrl());
			// workaround.
			PithosFileSystem.setHadoopPithosConnector(connector);

		} catch (Exception e) {
			log.info("Error on initialization", e);
		}
	}

	public boolean makeFolder(String fileName) {
		String resultCode = "-1";
		log.debug("makeFolder->" + fileName);
		String relativePath = fileName.substring(pithosRoot.length()-1, fileName.length());
		log.debug("final->" + relativePath);
		resultCode = connector.uploadFileToPithos(workingContainer, relativePath, true);

		log.debug("resultCode->" + resultCode);

		if (resultCode != null && resultCode.equals("201")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean storeFile(String targetFileName, InputStream is) {
		try {
			String relativeTarget = targetFileName.substring(pithosRoot.length()-1);
			
			connector.storePithosObject(workingContainer, new PithosObject(relativeTarget, null));
			org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
			long blocksize = connector.getPithosBlockDefaultSize(workingContainer);
			PithosPath pithosTargetPath = new PithosPath(workingContainer, relativeTarget);
			eu.openminted.store.fsconnector.debug.PithosOutputStream pithosOutputStream = new eu.openminted.store.fsconnector.debug.PithosOutputStream(conf,
					pithosTargetPath, blocksize, 1 * 1024 * 1024);

			int read = 0;
			byte[] bytes = new byte[1024 * 1024 * 10];
			while ((read = is.read(bytes)) != -1) {
				pithosOutputStream.write(bytes, 0, read);
				pithosOutputStream.flush();
			}
			is.close();
			pithosOutputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public String listAllFiles() {
		String result = connector.getFileList(workingContainer);
		
		/*
		String[] filePaths = result.split("\\s+");				
		for (String file : filePaths) {
			result = result + file + "\n";
		}*/

		return result;
	}

	@Override // FIXME: currently is the same with "listAllFiles()" just to avoid compilation error
	public List<String> listFiles(String fileName, boolean listDirectories, boolean recursive, boolean ignoreZips) {
		String result = connector.getFileList(workingContainer);


		String resultSplitted[] = result.split("\n");
		List<String> results = new ArrayList<>();
		for (String file : resultSplitted) {
			if (file.contains(fileName))
				results.add(file);
		}
		if(!listDirectories){
			results.removeIf(file -> FilenameUtils.getExtension(file).isEmpty());
		}

		if(!recursive){
			results.removeIf(file -> file.split("/").length!=2);
		}

		if(ignoreZips){
			results.removeIf(file -> FilenameUtils.getExtension(file).equals("zip"));
		}

		return results;
	}

	@Override // FIXME: currently is the same with "listAllFiles()" just to avoid compilation error
	public List<String> listFiles(String fileName, int from, int size) {
		String result = connector.getFileList(workingContainer);

		/*
		String[] filePaths = result.split("\\s+");
		for (String file : filePaths) {
			result = result + file + "\n";
		}*/

//		return result;
		return null;
	}

//    @Override // TODO: remove
//    public ArrayList<Publication> listCorpus(String corpusId, int from, int size) {
//        return null; // TODO write code
//    }

	@Override
	public boolean deleteAll() {
		String result = connector.getFileList(workingContainer);
		
		String[] filePaths = result.split("\\s+");
		for (String file : filePaths) {
			result = result + file + "\n";			
			log.debug("DELETE->" + file + " " + deleteFile(file));
		}

		return true;
	}
	
	@Override
	public boolean deleteFolder(String folder, boolean recursively) {
		return deleteFile(folder.substring(pithosRoot.length()-1));
	}

	@Override
	public boolean deleteFile(String fileName) {
		fileName = fileName.substring(pithosRoot.length()-1);
		log.debug("deleting->" + fileName);
		String resultCode = connector.deletePithosObject(workingContainer, fileName);
		log.debug("resultCode->" + resultCode);
		if (resultCode.contains("204")) {
			return true;
		}
		return false;
	}
	
	@Override
	public InputStream download(String targetFileName) {
		String target = targetFileName.substring(pithosRoot.length()-1);
		log.debug("target->" + target);
		PithosPath pithosPath = new PithosPath(workingContainer, target);
		log.debug("parent->" + pithosPath.getParent());
		String pathEsc = null;

		try {
			pathEsc = Utils.urlEscape(null, null, pithosPath.getObjectAbsolutePath(), null);
			log.debug("escape:" + pathEsc);
		} catch (URISyntaxException e) {
			log.debug("Error on downloading", e);
		}
		eu.openminted.store.fsconnector.debug.PithosInputStream pithosInputStream = new eu.openminted.store.fsconnector.debug.PithosInputStream(workingContainer, pathEsc);

		return pithosInputStream;
		
	}

	@Override
	public boolean compressDir(String dir, String zipFile) {
		try {
//			deleteFile(zipFile.substring(pithosRoot.length()-1));
			File tempFile = Files.createTempFile(System.currentTimeMillis()+"",".zip").toFile();
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(tempFile.getPath()));
			for(String file: listFiles(dir.substring(pithosRoot.length()),false,true,true)) {
				out.putNextEntry(new ZipEntry(file));
				InputStream in = connector.pithosObjectInputStream(workingContainer,file);
				byte[] b = new byte[1024*1024*10];
				int count;

				while ((count = in.read(b)) > 0) {
					out.write(b, 0, count);
				}
				in.close();
				out.closeEntry();
			}
			out.close();
			storeFile(zipFile, new FileInputStream(tempFile));
			tempFile.delete();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean isDir(String path) {
		return connector.getFileList(workingContainer).contains(path.substring(pithosRoot.length()));
	}

	@Override
	public boolean exists(String path) {
		return connector.getFileList(workingContainer).contains(path.substring(pithosRoot.length()));
	}

	@Override
	public boolean copyContent(String src, String dst) {
		src = src.substring(pithosRoot.length()-1);
		dst = dst.substring(pithosRoot.length()-1);

		for(String file: listFiles(src.substring(pithosRoot.length()), true, true, true)) {
			try {
				connector.copy_object(workingContainer, file, workingContainer, file.replace(src,dst), new HashMap<>(), new HashMap<>());
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}
}
