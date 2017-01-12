package eu.openminted.store.fsconnector;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URISyntaxException;

import gr.grnet.escience.commons.Utils;
import gr.grnet.escience.fs.pithos.PithosFileSystem;
import gr.grnet.escience.fs.pithos.PithosInputStream;
import gr.grnet.escience.fs.pithos.PithosObject;
import gr.grnet.escience.fs.pithos.PithosOutputStream;
import gr.grnet.escience.fs.pithos.PithosPath;
//import eu.openminted.storage.fsconnector.debug.HadoopPithosConnector;
import gr.grnet.escience.pithos.rest.HadoopPithosConnector;
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
			log.debug("workingContainer:" + workingContainer);
			connector = new HadoopPithosConnector(pithosUrl, pithosToken, uuid);
			// workaround.
			PithosFileSystem.setHadoopPithosConnector(connector);

		} catch (Exception e) {
			log.debug("Error on initialization", e);
		}
	}

	public boolean makeFolder(String fileName) {
		String resultCode = "-1";
		log.info("uploading:" + fileName);
		String relativePath = fileName.substring(pithosRoot.length(), fileName.length()-1);
		log.info("final:" + relativePath);		
		resultCode = connector.uploadFileToPithos(workingContainer, relativePath, true);

		log.debug("resultCode:" + resultCode);
		if (resultCode != null && resultCode.equals("201")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean storeFile(String targetFileName, InputStream is) {

		try {
			String relativeTarget = targetFileName.substring(pithosRoot.length());
			
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



	@Override
	public boolean deleteAll() {
		String result = connector.getFileList(workingContainer);
		
		String[] filePaths = result.split("\\s+");
		for (String file : filePaths) {
			result = result + file + "\n";			
			log.debug("DELETE:" + file + " " + deleteFile(file));
		}

		return true;
	}
	
	@Override
	public boolean deleteFolder(String folder, boolean recursively) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteFile(String fileName) {
		log.debug("deleting:" + fileName);
		String resultCode = connector.deletePithosObject(workingContainer, fileName);
		log.debug("resultCode:" + resultCode);

		if (resultCode.contains("204")) {
			return true;
		}
		return false;
	}
	
	@Override
	public InputStream download(String targetFileName) {
		String target = targetFileName.substring(pithosRoot.length());
		log.debug("target:" + target);
		PithosPath pithosPath = new PithosPath(workingContainer, target);
		log.info("parent:" + pithosPath.getParent());
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
}
