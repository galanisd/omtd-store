package eu.openminted.store.restclient.cmd;

import java.io.File;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import eu.openminted.store.restclient.StoreRESTClient;

/**
 * @author galanisd
 *
 */
@SpringBootApplication
public class ApplicationBoot implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(ApplicationBoot.class);

	private StoreRESTClient store;
	private String endpoint;
	public final static String defaultEndpoint = "http://localhost:8080/";

	@Override
	public void run(String... args) throws Exception {
		System.out.println("\n\nStarting Store Command Line Client ");
		setEndpoint(defaultEndpoint);
		System.out.println("Using default endpoint: " + defaultEndpoint + "\n\n");

		printHelp();

		final Scanner console = new Scanner(System.in);

		while (true) {
			System.out.print("StoreClient\\> ");

			try {
				final String command = console.nextLine();
				if (command.equals("quit")) {
					System.out.println("Bye!");
					break;
				} else if (command.startsWith("setEndpoint")) {
					final String[] allCMDArgs = command.split(" ");
					if (allCMDArgs.length != 2) {
						check();
						this.printHelp();
					} else {
						setEndpoint(allCMDArgs[1]);
					}
				} else if (command.equals("printEndpoint")) {
					this.printEndpoint();
				} else if (command.equals("createArchive")) {
					System.out.println(store.createArchive());
				} else if (command.equals("listFiles")) {
					System.out.println(store.listFiles());
				} else if (command.startsWith("uploadFileToArchive")) {
					final String[] allCMDArgs = command.split(" ");
					if (allCMDArgs.length != 3) {
						check();
						this.printHelp();
					} else {
						String fileName = allCMDArgs[1];
						String archiveID = allCMDArgs[2];
						File file = new File(fileName);

						System.out.println(store.updload(file, archiveID, file.getName()));
					}
				} else if (command.startsWith("downloadArchive")) {
					final String[] allCMDArgs = command.split(" ");
					if (allCMDArgs.length != 3) {
						check();
						this.printHelp();
					} else {
						String archiveID = allCMDArgs[1];
						String destination = allCMDArgs[2];
						System.out.println(store.downloadArchive(archiveID, destination));
					}
				} else if (command.equals("deleteAll")) {
					System.out.println(store.deleteAll());
				} else if (command.startsWith("finalizeArchive")) {
					final String[] allCMDArgs = command.split(" ");
					if (allCMDArgs.length != 2) {
						check();
						this.printHelp();
					} else {
						String archiveID = allCMDArgs[1];
						System.out.println(store.finalizeArchive(archiveID));
					}
				} else if (command.equals("help")) {
					this.printHelp();
				} else {
					this.unknownCommand();
					this.printHelp();
				}
			} catch (Exception e) {
				e.printStackTrace();
				error();
				printHelp();
			}
		}
	}

	private void setEndpoint(String enpoint) {
		this.endpoint = enpoint;
		buildRESTClient();
	}

	private void printEndpoint() {
		System.out.println("endpoint: " + endpoint);
	}

	private void check() {
		System.out.println("Check the syntax of the command...");
	}

	private void unknownCommand() {
		System.out.println("Unknown command!");
	}

	private void error() {
		System.out.println("ERR0R!");
	}

	private void printHelp() {
		System.out.println("Available commands:");
		final String format = "%-25s%s%n";
		// == ===
		System.out.printf(format, "help", " => Displays help.");
		System.out.printf(format, "quit", " => Quit Store Client.");
		// == ===
		System.out.printf(format, "setEndpoint [endpoint] ", " => Set store endpoint. ");
		System.out.printf(format, "printEndpoint ", " => Prints endpoint.");
		// == ===
		System.out.printf(format, "createArchive ", " => Creates archive and returns its id.");
		System.out.printf(format, "listFiles ", " => List all files in the store.");
		System.out.printf(format, "deleteAll ", " => Delete all files in the store.");
		System.out.printf(format, "uploadFileToArchive [filePath] [archiveID] ", " => Uploads a file to an archive");
		System.out.printf(format, "downloadArchive [archiveID] [destinationPath]  ",
				" => Downloads an archive zip to destinationPath");
		System.out.printf(format, "finalizeArchive [archiveID]", " => Finalize Archive.");

	}

	private void buildRESTClient() {
		store = new StoreRESTClient(endpoint);
	}

	// == === ==
	public static void main(String args[]) {
		SpringApplication app = new SpringApplication(ApplicationBoot.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}
}
