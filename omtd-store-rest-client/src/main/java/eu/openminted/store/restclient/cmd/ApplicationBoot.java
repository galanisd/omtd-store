package eu.openminted.store.restclient.cmd;

import java.io.File;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import eu.openminted.store.common.StoreResponse;
import eu.openminted.store.restclient.StoreRESTClient;

/**
 * @author galanisd
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"eu.openminted.store.restclient"})
public class ApplicationBoot implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(ApplicationBoot.class);

	private String endpoint;
	public final static String defaultEndpoint = "http://localhost:8080/";
	
	@Autowired
	private StoreRESTClient store;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("\n\nStarting Store Command Line Client ");
		setEndpoint(defaultEndpoint);
		System.out.println("Using default endpoint: " + defaultEndpoint + "\n\n");

		printHelp();

		final Scanner console = new Scanner(System.in);
		boolean notQuiting = true;
		while (notQuiting) {
			System.out.print("\nStoreClient\\> ");

			try {
				final String command = console.nextLine().trim().replaceAll("\\s{2,}", " ");
				if (command.equals(Commands.quit)) {
					System.out.println("Bye!");
					notQuiting = false;
				} else if (command.startsWith(Commands.setEndpoint)) {
					final String[] allCMDArgs = command.split(" ");
					if (allCMDArgs.length != 2) {
						checkCMDSyntax();
						this.printHelp();
					} else {
						setEndpoint(allCMDArgs[1]);
					}
				} else if (command.equals(Commands.printEndpoint)) {
					this.printEndpoint();
				} else if (command.equals(Commands.createArch)) {
					System.out.println(store.createArchive());
				}  else if (command.startsWith(Commands.deleteArch)) {
					final String[] allCMDArgs = command.split(" ");
					if (allCMDArgs.length != 2) {
						checkCMDSyntax();
						this.printHelp();
					} else {						
						String archiveID = allCMDArgs[1];						
						messageGenerator(store.deleteArchive(archiveID));
					}	
				} 				
				else if (command.equals(Commands.listFiles)) {
					System.out.println(store.listFiles());
				} else if (command.startsWith(Commands.uploadFileToArch)) {
					final String[] allCMDArgs = command.split(" ");
					if (allCMDArgs.length != 3) {
						checkCMDSyntax();
						this.printHelp();
					} else {
						String fileName = allCMDArgs[1];
						String archiveID = allCMDArgs[2];
						File file = new File(fileName);
						messageGenerator(store.updload(file, archiveID, file.getName()));
					}															
				} else if (command.startsWith(Commands.downloadArch)) {
					final String[] allCMDArgs = command.split(" ");
					if (allCMDArgs.length != 3) {
						checkCMDSyntax();
						this.printHelp();
					} else {
						String archiveID = allCMDArgs[1];
						String destination = allCMDArgs[2];
						messageGenerator(store.downloadArchive(archiveID, destination));
					}
				} else if (command.equals(Commands.deleteAll)) {
					System.out.println(store.deleteAll());
				} else if (command.startsWith(Commands.finalizeArch)) {
					final String[] allCMDArgs = command.split(" ");
					if (allCMDArgs.length != 2) {
						checkCMDSyntax();
						this.printHelp();
					} else {
						String archiveID = allCMDArgs[1];
						messageGenerator(store.finalizeArchive(archiveID));
					}
				} else if (command.equals(Commands.help)) {
					this.printHelp();
				} else {
					this.unknownCommand();
					this.printHelp();
				}
			} catch (Exception e) {
				e.printStackTrace();
				error();
				//printHelp();
			}
		}//
		
		console.close();
	}

	/**
	 * Sets endpoint.
	 * @param endpoint
	 */
	private void setEndpoint(String endpoint) {
		this.endpoint = endpoint;		
		store.setEndpoint(endpoint);
	}

	private void printEndpoint() {
		System.out.println("\nendpoint: " + endpoint);
	}

	private void checkCMDSyntax() {
		System.out.println("\nCheck the syntax of the command...");
	}

	private void unknownCommand() {
		System.out.println("\nUnknown command!");
	}

	private void error() {
		System.out.println("\nERR0R!");
	}
	
	/**
	 * Prints all available commands.
	 */
	private void printHelp() {
		System.out.println("\nAvailable commands:\n-- --- -- --- -- --- -- ---");
		final String format = "%-25s%s%n";
		// == ===
		System.out.printf(format, Commands.help, " => Displays help.");
		System.out.printf(format, Commands.quit, " => Quit Store Client.");
		// == ===
		System.out.printf(format, Commands.setEndpoint + " [endpoint] ", " => Set store endpoint. ");
		System.out.printf(format, Commands.printEndpoint, " => Prints endpoint.");
		// == ===
		System.out.printf(format, Commands.createArch, " => Creates archive and returns its id.");
		System.out.printf(format, Commands.deleteArch + " [archiveID] ", " => Deletes archive.");		
		System.out.printf(format, Commands.listFiles, " => List all files in the store.");
		System.out.printf(format, Commands.deleteAll , " => Delete all files in the store.");
		System.out.printf(format, Commands.uploadFileToArch + " [filePath] [archiveID] ", " => Uploads a file to an archive");
		System.out.printf(format, Commands.downloadArch + " [archiveID] [destinationPath]  ",
				" => Downloads an archive zip to destinationPath");
		System.out.printf(format, Commands.finalizeArch+ " [archiveID]", " => Finalize Archive.");

	}

	private void messageGenerator(StoreResponse storeResponse){
		messageGenerator(storeResponse.getResponse(), storeResponse.getReport());
	}
	
	private void messageGenerator(boolean serviceResponse){
		if(serviceResponse){
			System.out.println("DONE!");
		}else{
			System.out.println("ERROR :-(");
		}
	}
	
	private void messageGenerator(String serviceResponse, String report){
		String msg = "";
		if(serviceResponse != null){
			if(serviceResponse.startsWith("true")){
				msg = "DONE!";
			}else{
				msg = "ERROR :-(";
			}
		}else{
			msg = "ERROR :-(";
		}
		
		System.out.print(msg);
	}	
	// == === ==
	public static void main(String args[]) {
		log.info("...");
		SpringApplication app = new SpringApplication(ApplicationBoot.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
		log.info("DONE!");
	}
}
