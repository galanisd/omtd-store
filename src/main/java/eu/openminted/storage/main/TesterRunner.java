package eu.openminted.storage.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import eu.openminted.storage.StoreService;
import eu.openminted.storage.StoreServiceLocalDisk;
import eu.openminted.storage.StoreServicePITHOS;
import eu.openminted.storage.config.AppConfig;
import eu.openminted.storage.config.Storage;

/**
 * @author galanisd
 *
 */
public class TesterRunner {

	int scenario = -1;
	long start = 0;
	StoreService store = null;

	/**
	 * Constructor.
	 * @param t
	 */
	public TesterRunner(String t) {
		
		if (t.equalsIgnoreCase(Storage.LOCAL)) {
			System.setProperty("storeApplication.properties",
					"classpath:/eu/openminted/storage/config/configLocal.properties");
			ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
			store = (StoreService) ctx.getBean(StoreServiceLocalDisk.class);
		} else if (t.equalsIgnoreCase(Storage.PITHOS)) {
			System.setProperty("storeApplication.properties",
					"classpath:/eu/openminted/storage/config/configPITHOS.properties");
			ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
			store = (StoreService) ctx.getBean(StoreServicePITHOS.class);
		}
	}

	/**
	 * Execute Tests.
	 */
	public void executeTests() {
		try {
			ClassLoader classLoader = ClassLoader.getSystemClassLoader();
			File sampleAnnotatedFile = new File(classLoader
					.getResource("eu/openminted/storage/rizospastis_AVVd-4ehg3d853ySFFif.xml.gate").getFile());
			File samplePDFFile = new File(
					classLoader.getResource("eu/openminted/storage/2016_Kathaa_NAACL.pdf").getFile());

			start = System.currentTimeMillis();

			listAllFilesAndThenDeleteAll();
			
			createAHierarchyOfArchivesAndStoreAFileInTheLastOne(sampleAnnotatedFile);
			
			createArchiveWithAFolderThatContainsAPDFFile(samplePDFFile);
			
			createArchiveWithAFolderThatContainsAnAnnotationFileThenDeleteTheAnnotationFile(sampleAnnotatedFile);
						
			createArchiveWithLargeFileAndDownloadTheFile();
			
			createArchiveWithManyFilesAndDownloadEachOfThem();
			
			listFiles();
						
			long end = System.currentTimeMillis();
			System.out.println("===Summary===");
			System.out.println("Duration");
			System.out.println("Milliseconds:" + (end - start));
			System.out.println("Seconds:" + ((float) (end - start)) / 1000);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * List All Files And Then Delete Them.
	 * @throws Exception
	 */
	public void listAllFilesAndThenDeleteAll() throws Exception{
		System.out.println("Scenario:" + (++scenario));
		System.out.println("\n\n\n" + "FILE LIST");
		System.out.println(store.listAllFiles());			
		store.deleteAll();
	}
	
	/**
	 * Create A Hierarchy Of Archives.
	 * @param sampleAnnotatedFile
	 * @throws Exception
	 */
	public void createAHierarchyOfArchivesAndStoreAFileInTheLastOne(File sampleAnnotatedFile) throws Exception{
		System.out.println("Scenario:" + (++scenario));
		// Creating a hierarchy of archives.
		String level0ArchId = store.createArchive();
		System.out.println("Created archive level 0:" + level0ArchId);
		String level1ArchId = store.createArchive(level0ArchId, "");
		System.out.println("Created archive level 1:" + level1ArchId);
		String level2ArchId = store.createArchive(level1ArchId, "");
		System.out.println("Created archive level 2:" + level2ArchId);
		// Store a file in the last archive
		store.storeFile(level2ArchId, new FileInputStream(sampleAnnotatedFile), sampleAnnotatedFile.getName());
	}
	
	/**
	 * Create Archive With A Folder That Contains A PDF File.
	 * @param samplePDFFile
	 * @throws Exception
	 */
	public void createArchiveWithAFolderThatContainsAPDFFile(File samplePDFFile) throws Exception{
		System.out.println("Scenario:" + (++scenario));
		// Creating an archive.
		String archId = store.createArchive();
		// Store a PDF
		store.storeFile(archId, new FileInputStream(samplePDFFile), "pdfs/" + samplePDFFile.getName());
	}
	
	/**
	 * CreateArchiveWithAFolderThatContainsAnAnnotationFileThenDeleteTheAnnotationFile
	 * @param sampleAnnotatedFile
	 * @throws Exception
	 */
	public void createArchiveWithAFolderThatContainsAnAnnotationFileThenDeleteTheAnnotationFile(File sampleAnnotatedFile) throws Exception{
		System.out.println("Scenario:" + (++scenario));
		// Creating an archive.
		String archId = store.createArchive();
		// Store an annotation file.
		store.storeFile(archId, new FileInputStream(sampleAnnotatedFile),
				"annotations/" + sampleAnnotatedFile.getName());
		// Delete the annotation file.
		store.deleteFile(archId, "annotations/" + sampleAnnotatedFile.getName());
	}
	
	/**
	 * createArchiveWithLargeFileAndDownloadTheFile
	 * @throws Exception
	 */
	public void createArchiveWithLargeFileAndDownloadTheFile() throws Exception{
		System.out.println("Scenario:" + (++scenario));
		// Creating an archive with a big file.
		String archId = store.createArchive();
		// File bigFile = new
		// File("C:/Users/galanisd/Desktop/Data/CORE/repository_text_2015-09.tar.gz");
		// File bigFile = new
		// File("C:/Users/galanisd/Desktop/Data/CORE/repository_metadata_2015-09.tar.gz");
		File bigFile = new File(
				"C:/Users/galanisd/Desktop/Data/OpenAIRE/openairemetadata/stelios_metadata/authors.json");
		// File bigFile = new
		// File("C:/Users/galanisd/Desktop/Data/OpenAIRE/openairemetadata/stelios_metadata/keywords.json");
		boolean success = store.storeFile(archId, new FileInputStream(bigFile), bigFile.getName());
		System.out.println(" uploaded:" + success + " " + bigFile.getAbsolutePath());	
		InputStream is = store.downloadFile(archId + "/" + bigFile.getName());
		FileOutputStream fos = new FileOutputStream("C:/Users/galanisd/Desktop/Data/_AppTestData/Downloaded/" + bigFile.getName() + ".txt");
		TesterRunner.store(is, fos);
		fos.close();
	}

	/**
	 * Creating an archive with many files.
	 * Then each of them is downloaded in the local folder.
	 * @throws Exception
	 */
	public void createArchiveWithManyFilesAndDownloadEachOfThem() throws Exception{
		
		System.out.println("Scenario:" + (++scenario));
		// Creating an archive with many files.
		String archId = store.createArchive();
		// File inputDir = new
		// File("C:/Users/galanisd/Desktop/Data/CORE/repository_metadata_2015-09_abstractscleaned_toy/");
		// File inputDir = new
		// File("C:/Users/galanisd/Desktop/Data/CORE/repository_metadata_2015-09_abstractscleaned/");
		File inputDir = new File("C:/Users/galanisd/Desktop/Data/Parliament/ParliamentAll/_Prokopis/txtsOut/");
		File[] inFiles = inputDir.listFiles();
		int fileIndex = 0;
		// int maxNumOfFilesToBeUploaded = Integer.MAX_VALUE;
		int maxNumOfFilesToBeUploaded = 5;
		// int threshold = 1000;
		for (File fileForUpload : inFiles) {
			if (fileIndex < maxNumOfFilesToBeUploaded) {
				System.out.println("File to be uploaded:" + fileForUpload.getName());
				// long x = System.currentTimeMillis();
				String dest = "someFiles/" + fileIndex + ".somefile";
				boolean successFileUp = store.storeFile(archId, new FileInputStream(fileForUpload), dest);
				// long y = System.currentTimeMillis();
				System.out.println(fileIndex + " Upload status:" + successFileUp + " size:" + fileForUpload.length()
						+ " Local path" + fileForUpload.getAbsolutePath());
				// System.out.println("Duration:" + ((y-x)/1000) +);
				long end = System.currentTimeMillis();
				System.out.println("Seconds so far:" + ((float) (end - start)) / 1000);					
				
				InputStream is = store.downloadFile(archId + "/" + dest);						
				FileOutputStream fos = new FileOutputStream("C:/Users/galanisd/Desktop/Data/_AppTestData/Downloaded/" + fileForUpload.getName() + ".txt");
				TesterRunner.store(is, fos);
				fos.close();					
			}
			fileIndex++;
		}
	}
	
	/**
	 * List all storage files
	 * @throws Exception
	 */
	public void listFiles() throws Exception{
		System.out.println("Scenario:" + (++scenario));
		System.out.println("\n\n\n" + "FILE LIST");
		System.out.println(store.listAllFiles());
	}
	
	public static void store(InputStream is, FileOutputStream fos){
		try{
			int read = 0;
			byte[] bytes = new byte[1024 * 1024 * 10];
			while ((read = is.read(bytes)) != -1) {
				fos.write(bytes, 0, read);
				fos.flush();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// == 
	
	public static void main(String args[]){
		
		String t = "";
		//t = Storage.PITHOS;
		t = Storage.LOCAL;
		
		TesterRunner runner = new TesterRunner(t);
		runner.executeTests();					 			 
	}
}
