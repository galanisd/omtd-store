package eu.openminted.store.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import eu.openminted.store.config.ApplicationConfig;
import eu.openminted.store.config.ApplicationConfigParams;
import eu.openminted.store.config.ApplicationConfigTestParams;
import eu.openminted.store.config.ApplicationConfigurator;
import eu.openminted.store.config.Store;
import eu.openminted.store.core.StoreService;
import eu.openminted.store.core.StoreServiceGeneric;
import eu.openminted.store.core.StoreServiceLocalDisk;
import eu.openminted.store.core.StoreServicePITHOS;

/**
 * @author galanisd
 * A tester for the omtd-store-api. 
 * 
 */
@Component
public class StoreAPITester{
	private static final Logger log = LoggerFactory.getLogger(StoreAPITester.class);
				
	private int scenario = -1;
	private long start = 0;
	
	@Autowired
	private StoreServiceGeneric store;
	
	private Properties testFiles;	
	
	/**
	 * Constructor. Creates a store service using a config file (LOCAL OR PITHOS).
	 * @param storeType
	 */
	/*
	public StoreAPITester(String storeType) {		
		if (storeType.equalsIgnoreCase(Store.LOCAL)) {
			System.setProperty(ApplicationConfigParams.storeApplicationCfg, "classpath:/eu/openminted/store/config/configLocalDefault.properties");
			ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
			store = (StoreService) ctx.getBean(StoreServiceLocalDisk.class);
		} else if (storeType.equalsIgnoreCase(Store.PITHOS)) {
			System.setProperty(ApplicationConfigParams.storeApplicationCfg, "classpath:/eu/openminted/store/config/configPITHOS.properties");
			ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
			store = (StoreService) ctx.getBean(StoreServicePITHOS.class);
		}		
	}
	*/
	
	/**
	 * Constructor.
	 * @param storeType
	 */
	public StoreAPITester(StoreServiceGeneric store) {
		this.store = store;		
	}
		
	/**
	 * Initialize test files locations.
	 */
	private void init(){		
		testFiles = new Properties();
		
		try{
			String testFileLocationsFile = System.getProperty(ApplicationConfigTestParams.testFilesCfg);		
			// If is not provided load default applications properties (based on applicationPropertiesDefaultHolder).
			if(testFileLocationsFile == null){																			
				testFiles.load(StoreAPITester.class.getResourceAsStream(ApplicationConfigTestParams.testFilesDefault));
				log.info("Loaded default test properties:" + ApplicationConfigTestParams.testFilesDefault);
			}else{
				testFiles.load(new FileInputStream(testFileLocationsFile));
			}
		}catch(Exception e){
			log.debug("ERROR", e);
		}
	}
	
	/**
	 * Execute all tests.
	 */
	public void executeTests() {
		try {
			// Load Test files
			TestFiles fileForTests = new TestFiles();;			
			// Init
			init();						
			// Start clock.
			start = System.currentTimeMillis();
			// Run Tests.
			log.info("Scenario:" + (++scenario));
			listAllFilesAndThenDeleteAll();		
			log.info("Scenario:" + (++scenario));
			createAHierarchyOfArchivesAndStoreAFileInTheLastOne(fileForTests.getSampleAnnotatedFile());
			log.info("Scenario:" + (++scenario));
			createArchiveWithAFolderThatContainsAPDFFile(fileForTests.getSamplePDFFile());
			log.info("Scenario:" + (++scenario));
			createArchiveWithAFolderThatContainsAnAnnotationFileThenDeleteTheAnnotationFile(fileForTests.getSampleAnnotatedFile());
			log.info("Scenario:" + (++scenario));
			createArchiveWithAFileAndDownloadTheFile();
			log.info("Scenario:" + (++scenario));
			createArchiveWithManyFilesAndDownloadEachOfThem();
			log.info("Scenario:" + (++scenario));
			listFiles();					
			// Done.
			long end = System.currentTimeMillis();
			
			log.info("===Summary===");
			log.info("Duration");
			log.info("Milliseconds:" + (end - start));
			log.info("Seconds:" + ((float) (end - start)) / 1000);

		} catch (Exception e) {
			log.error("ERROR:", e);
		}
	}
	
	// == = == All tests below. 
	
	/**
	 * List All Files And Then Delete Them.
	 * @throws Exception
	 */
	public void listAllFilesAndThenDeleteAll() throws Exception{
		log.info("\n\n\n" + "FILE LIST");
		log.info(store.listAllFiles());			
		store.deleteAll();
	}
	
	/**
	 * Create A Hierarchy Of Archives And Store A File In The Last One.
	 * @param sampleAnnotatedFile
	 * @throws Exception
	 */
	public boolean createAHierarchyOfArchivesAndStoreAFileInTheLastOne(File sampleAnnotatedFile) throws Exception{
		// Creating a hierarchy of archives.
		String level0ArchId = store.createArchive();
		log.info("Created archive level 0:" + level0ArchId);
		String level1ArchId = store.createArchive(level0ArchId, "level1");
		log.info("Created archive level 1:" + level1ArchId);
		String level2ArchId = store.createArchive(level1ArchId, "level2");
		log.info("Created archive level 2:" + level2ArchId);
		// Store a file in the last archive
		boolean status = store.storeFile(level2ArchId, new FileInputStream(sampleAnnotatedFile), sampleAnnotatedFile.getName());
		return status;
	}
	
	/**
	 * Create Archive With A Folder That Contains A PDF File.
	 * @param samplePDFFile
	 * @throws Exception
	 */
	public boolean createArchiveWithAFolderThatContainsAPDFFile(File samplePDFFile) throws Exception{
		// Creating an archive.
		String archId = store.createArchive();
		// Store a PDF
		boolean status = store.storeFile(archId, new FileInputStream(samplePDFFile), "pdfs/" + samplePDFFile.getName());
		
		return status;
	}
	
	/**
	 * Create Archive With A Folder That Contains An Annotation File. Then Delete The Annotation File.
	 * @param sampleAnnotatedFile
	 * @throws Exception
	 */
	public boolean createArchiveWithAFolderThatContainsAnAnnotationFileThenDeleteTheAnnotationFile(File sampleAnnotatedFile) throws Exception{
		//log.info("Scenario:" + (++scenario));
		// Creating an archive.
		String archId = store.createArchive();
		// Store an annotation file.
		boolean status = store.storeFile(archId, new FileInputStream(sampleAnnotatedFile),
				"annotations/" + sampleAnnotatedFile.getName());
		// Delete the annotation file.
		boolean status2 = store.deleteFile(archId, "annotations/" + sampleAnnotatedFile.getName());
		if(archId != null && status && status2){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Create Archive With A File And Download The File.
	 * @throws Exception
	 */
	public boolean createArchiveWithAFileAndDownloadTheFile() throws Exception{
		//log.info("Scenario:" + (++scenario));
		// Creating an archive with a big file.
		String archId = store.createArchive();
		File bigFile = new File(testFiles.getProperty(ApplicationConfigTestParams.fileForStore));				
		boolean successUpload = store.storeFile(archId, new FileInputStream(bigFile), bigFile.getName());
		log.info(" uploaded:" + successUpload + " " + bigFile.getAbsolutePath());	
		InputStream is = store.downloadFile(archId + "/" + bigFile.getName());
		FileOutputStream fos = new FileOutputStream(testFiles.getProperty(ApplicationConfigTestParams.downloadFolder) + bigFile.getName());
		boolean down = StoreAPITester.store(is, fos);
		fos.close();
		
		if(archId != null & successUpload && down){ 
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Creating an archive with many files.
	 * Then each of them is downloaded in a local folder.
	 * @throws Exception
	 */
	public boolean createArchiveWithManyFilesAndDownloadEachOfThem() throws Exception{
		boolean status = true;
		
		// Creating an archive with many files.
		String archId = store.createArchive();
		if(archId == null){
			status = false; 	
		}
		
		File inputDir = new File(testFiles.getProperty(ApplicationConfigTestParams.folderWithManyFiles));
		File[] inFiles = inputDir.listFiles();
		int fileIndex = 0;
		// int maxNumOfFilesToBeUploaded = Integer.MAX_VALUE;
		int maxNumOfFilesToBeUploaded = 5;
		// int threshold = 1000;
		for (File fileForUpload : inFiles) {
			if (fileIndex < maxNumOfFilesToBeUploaded) {
				log.info("File to be uploaded:" + fileForUpload.getName());
				// long x = System.currentTimeMillis();
				String dest = "someFiles/" + fileIndex + ".somefile";
				boolean successFileUp = store.storeFile(archId, new FileInputStream(fileForUpload), dest);
				
				if(!successFileUp){
					status = false;
				}
				
				// long y = System.currentTimeMillis();
				log.info(fileIndex + " Upload status:" + successFileUp + " size:" + fileForUpload.length()
						+ " Local path" + fileForUpload.getAbsolutePath());
				// log.info("Duration:" + ((y-x)/1000) +);
				long end = System.currentTimeMillis();
				log.info("Seconds so far:" + ((float) (end - start)) / 1000);					
				
				InputStream is = store.downloadFile(archId + "/" + dest);	
				
				FileOutputStream fos = new FileOutputStream(testFiles.getProperty(ApplicationConfigTestParams.downloadFolder) + fileForUpload.getName() + ".txt");
				boolean statusDown = StoreAPITester.store(is, fos);
				if(!statusDown)
					status = false;
				fos.close();					
			}
			fileIndex++;
		}
		
		return status;
	}
	
	/**
	 * List all storage files
	 * @throws Exception
	 */
	public void listFiles() throws Exception{
		//log.info("Scenario:" + (++scenario));
		log.info("\n\n\n" + "FILE LIST");
		log.info(store.listAllFiles());
	}
	
	// == = ==  End of tests.	
	public static boolean store(InputStream is, FileOutputStream fos){
		try{
			int read = 0;
			byte[] bytes = new byte[1024 * 1024 * 10];
			while ((read = is.read(bytes)) != -1) {
				fos.write(bytes, 0, read);
				fos.flush();
			}
			
			return true;
		}catch(Exception e){
			log.error("ERROR:", e);
			return false;
		}
	}
}
