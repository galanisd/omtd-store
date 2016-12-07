package eu.openminted.storage.main;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import eu.openminted.storage.StoreService;
import eu.openminted.storage.StoreServiceLocalDisk;
import eu.openminted.storage.StoreServicePITHOS;
import eu.openminted.storage.config.AppConfig;

/**
 * @author galanisd
 *
 */
public class TesterRunner {

	StoreService store = null;

	public TesterRunner(int t) {

		if (t == 1) {
			System.setProperty("storeApplication.properties",
					"classpath:/eu/openminted/storage/config/configLocal.properties");
			ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
			store = (StoreService) ctx.getBean(StoreServiceLocalDisk.class);
		} else if (t == 2) {
			System.setProperty("storeApplication.properties",
					"classpath:/eu/openminted/storage/config/configPITHOS.properties");
			ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
			store = (StoreService) ctx.getBean(StoreServicePITHOS.class);
		}
	}

	public void executeTests() {
		try {
			ClassLoader classLoader = ClassLoader.getSystemClassLoader();
			File sampleAnnotatedFile = new File(classLoader
					.getResource("eu/openminted/storage/rizospastis_AVVd-4ehg3d853ySFFif.xml.gate").getFile());
			File samplePDFFile = new File(
					classLoader.getResource("eu/openminted/storage/2016_Kathaa_NAACL.pdf").getFile());

			int scenario = -1;
			// -- Scenario 0
			System.out.println("Scenario:" + (++scenario));
			store.deleteAll();

			long start = System.currentTimeMillis();
			// -- Scenario 1
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

			// -- Scenario 2
			System.out.println("Scenario:" + (++scenario));
			// Creating an archive.
			String archId = store.createArchive();
			// Store a PDF
			store.storeFile(archId, new FileInputStream(samplePDFFile), "pdfs/" + samplePDFFile.getName());

			// -- Scenario 3
			System.out.println("Scenario:" + (++scenario));
			// Creating an archive.
			archId = store.createArchive();
			// Store an annotation file.
			store.storeFile(archId, new FileInputStream(sampleAnnotatedFile),
					"annotations/" + sampleAnnotatedFile.getName());

			// -- Scenario 4
			System.out.println("Scenario:" + (++scenario));
			// Creating an archive.
			archId = store.createArchive();
			// Store an annotation file.
			store.storeFile(archId, new FileInputStream(sampleAnnotatedFile),
					"annotations/" + sampleAnnotatedFile.getName());
			store.deleteFile(archId, "annotations/" + sampleAnnotatedFile.getName());

			// -- Scenario 5
			System.out.println("Scenario:" + (++scenario));
			// Creating an archive with a big file.
			archId = store.createArchive();
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
			// success = store.deleteFile(archId, bigFile.getName());
			// System.out.println(" deleted:" + success + " " +
			// bigFile.getName());

			// -- Scenario 6
			System.out.println("Scenario:" + (++scenario));
			// Creating an archive with many files.
			archId = store.createArchive();
			// File inputDir = new
			// File("C:/Users/galanisd/Desktop/Data/CORE/repository_metadata_2015-09_abstractscleaned_toy/");
			// File inputDir = new
			// File("C:/Users/galanisd/Desktop/Data/CORE/repository_metadata_2015-09_abstractscleaned/");
			File inputDir = new File("C:/Users/galanisd/Desktop/Data/Parliament/ParliamentAll/_Prokopis/txtsOut/");
			File[] inFiles = inputDir.listFiles();
			int counter = 0;
			// int maxNumOfFilesToBeUploaded = Integer.MAX_VALUE;
			int maxNumOfFilesToBeUploaded = 0;
			// int threshold = 1000;
			for (File fileForUpload : inFiles) {
				if (counter < maxNumOfFilesToBeUploaded) {
					System.out.println("File to be uploaded:" + fileForUpload.getName());
					// long x = System.currentTimeMillis();
					boolean successFileUp = store.storeFile(archId, new FileInputStream(fileForUpload),
							"someFiles/" + counter + ".somefile");
					// long y = System.currentTimeMillis();
					System.out.println(counter + " Upload status:" + successFileUp + " size:" + fileForUpload.length()
							+ " Local path" + fileForUpload.getAbsolutePath());
					// System.out.println("Duration:" + ((y-x)/1000) +);
					long end = System.currentTimeMillis();
					System.out.println("Seconds so far:" + ((float) (end - start)) / 1000);
				}
				counter++;
			}

			System.out.println("Scenario:" + (++scenario));
			System.out.println("\n\n\n" + "FILE LIST");
			System.out.println(store.listAllFiles());
			long end = System.currentTimeMillis();

			System.out.println("===Summary===");
			System.out.println("Duration");
			System.out.println("Milliseconds:" + (end - start));
			System.out.println("Seconds:" + ((float) (end - start)) / 1000);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// == 
	
	public static void main(String args[]){
		int t = 2;			
		TesterRunner runner = new TesterRunner(t);
		runner.executeTests();					 			 
	}
}
