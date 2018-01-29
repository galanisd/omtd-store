package eu.openminted.store.restclient.cmd;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

import eu.openminted.store.restclient.Corpus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;

import eu.openminted.store.common.StoreResponse;
import eu.openminted.store.restclient.StoreRESTClient;

/**
 * @author galanisd
 *
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = {StoreRESTClient.class})
public class ApplicationBoot implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(ApplicationBoot.class);

	private String endpoint;
	public final static String defaultEndpoint = "http://localhost:8080/";
	
	@Autowired
	private StoreRESTClient store;

	private Corpus mycorpus;

	boolean notQuiting = true;
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("\n\nStarting Store Command Line Client ");

		if(args.length > 0 && args[0].equalsIgnoreCase("url")){
			setEndpoint(args[1]);
			String [] newArray = Arrays.copyOfRange(args, 2, args.length);
			final String command = concat(newArray);
			executeParsedCommand(command);
		}else{
			setEndpoint(defaultEndpoint);
			System.out.println("Using default endpoint: " + defaultEndpoint + "\n\n");
			System.out.println("interactive");
			interactive();
		}
	}

	public String concat(String[] args) {
		  String result = args[0];
		  for (int i = 1; i < args.length; i++) {
		    result = result + " " + args[i];
		  }
		  return result;
		}
	
	private void interactive(){
		printHelp();

		final Scanner console = new Scanner(System.in);
		while (notQuiting) {
			System.out.print("\nStoreClient\\> ");

			try{
				final String command = console.nextLine().trim().replaceAll("\\s{2,}", " ");
				executeParsedCommand(command);
			}catch(Exception e){
				e.printStackTrace();
			}
		}//while
		console.close();		
	}
	
	private void executeParsedCommand(String command){
        boolean flag;
        boolean listDirs;
        boolean recursive;
        flag = false;
        listDirs = false;
        recursive = false;
        int from = 0;
        int size = 100000;
		try {
			
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
				responsePrinterRaw(store.createArchive());
			}  else if (command.startsWith(Commands.deleteArch)) {
				final String[] allCMDArgs = command.split(" ");
				if (allCMDArgs.length != 2) {
					checkCMDSyntax();
					this.printHelp();
				} else {						
					String archiveID = allCMDArgs[1];						
					responsePrinter(store.deleteArchive(archiveID));
				}	
			}
			else if (command.startsWith(Commands.listFiles)) {
				final String[] allCMDArgs = command.split(" ");
				if (allCMDArgs.length > 4) {
					checkCMDSyntax();
					this.printHelp();
				} else if (allCMDArgs.length == 4) {
                    String archiveID = allCMDArgs[1];
                    if (command.contains(" -R")) {
                        flag = true;
                        recursive = true;
                        System.out.println("contains -R");
                    }
                    if (command.contains(" -d")) {
                        flag = true;
                        listDirs = true;
                        System.out.println("contains -d");
                    }
                    try {
                        from = Integer.parseInt(allCMDArgs[2]);
                        size = Integer.parseInt(allCMDArgs[3]);
                    } catch (Exception e) {

                    } finally {
                        System.out.println("from: "+ from + " size: " + size);
                    }
                    if (flag) {
                        System.out.println(store.listFiles(archiveID, listDirs, recursive).toString());
                    } else {
                        System.out.println(store.listFiles(archiveID, from, size).toString());
                    }
                } else if (allCMDArgs.length == 3) {
                    String archiveID = allCMDArgs[1];
                    if (command.contains(" -R")) {
                        flag = true;
                        recursive = true;
                        System.out.println("contains -R");
                    } else if (command.contains(" -d")) {
                        flag = true;
                        listDirs = true;
                        System.out.println("contains -d");
                    } else {
                        try {
                            size = Integer.parseInt(allCMDArgs[2]);
                        } catch (Exception e) {

                        } finally {
                            System.out.println("from: "+ from + " size: " + size);
                        }
                    }
                    if (flag) {
                        System.out.println(store.listFiles(archiveID, listDirs, recursive).toString());
                    } else {
                        System.out.println(store.listFiles(archiveID, 0, size).toString());
                    }
                } else if (allCMDArgs.length == 2) {
					String archiveID = allCMDArgs[1];
//                    responsePrinterRaw(store.listFiles(archiveID));
                    mycorpus = new Corpus(archiveID);
//                    System.out.println(store.listFiles(archiveID, false, false).toString());
				} else {
                    responsePrinterRaw(store.listFiles());


                }

//			} // FIXME: fix listCorpus
//			else if (command.startsWith(Commands.listCorpus)) {
//				final String[] allCMDArgs = command.split(" ");
//				if (allCMDArgs.length > 4) {
//					checkCMDSyntax();
//					this.printHelp();
//				} else if (allCMDArgs.length == 4) {
//                    String archiveID = allCMDArgs[1];
//                    int from = Integer.parseInt(allCMDArgs[2]);
//                    int size = Integer.parseInt(allCMDArgs[3]);
//                    responsePrinterRaw(store.listCorpus(archiveID, from, size));
//                } else if (allCMDArgs.length == 3) {
//                    String archiveID = allCMDArgs[1];
//                    int size = Integer.parseInt(allCMDArgs[2]);
//                    responsePrinterRaw(store.listCorpus(archiveID, 0, size));
//                } else {
//                    checkCMDSyntax();
//                }

			} else if (command.startsWith(Commands.uploadFileToArch)) {
				final String[] allCMDArgs = command.split(" ");
				if (allCMDArgs.length != 3) {
					checkCMDSyntax();
					this.printHelp();
				} else {
					String fileName = allCMDArgs[1];
					String archiveID = allCMDArgs[2];
					File file = new File(fileName);
					responsePrinter(store.storeFile(file, archiveID, file.getName()));
				}															
			} else if (command.startsWith(Commands.downloadArch)) {
				final String[] allCMDArgs = command.split(" ");
				if (allCMDArgs.length != 3) {
					checkCMDSyntax();
					this.printHelp();
				} else {
					String archiveID = allCMDArgs[1];
					String destination = allCMDArgs[2];
					responsePrinter(store.downloadArchive(archiveID, destination));
				}
			} else if (command.startsWith(Commands.downloadFile)) {
				final String[] allCMDArgs = command.split(" ");
				if (allCMDArgs.length != 3) {
					checkCMDSyntax();
					this.printHelp();
				} else {
					String archiveID = allCMDArgs[1];
					String destination = allCMDArgs[2];
					responsePrinter(store.downloadFile(archiveID, destination));
				}
			} else if (command.equals(Commands.deleteAll)) {
				responsePrinter(store.deleteAll());
			} else if (command.startsWith(Commands.finalizeArch)) {
				final String[] allCMDArgs = command.split(" ");
				if (allCMDArgs.length != 2) {
					checkCMDSyntax();
					this.printHelp();
				} else {
					String archiveID = allCMDArgs[1];
					responsePrinter(store.finalizeArchive(archiveID));
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
		System.out.printf(format, Commands.downloadFile + " [filePath] [destinationPath]  ",
				" => Downloads a specified file to destinationPath");
		System.out.printf(format, Commands.downloadArch + " [archiveID] [destinationPath]  ",
				" => Downloads an archive zip to destinationPath");
		System.out.printf(format, Commands.finalizeArch+ " [archiveID]", " => Finalize Archive.");

	}

	private void responsePrinterRaw(StoreResponse storeResponse){
		System.out.println(storeResponse.getResponse());
	}
	
	private void responsePrinter(StoreResponse storeResponse){
		responsePrinter(storeResponse.getResponse(), storeResponse.getReport());
	}
	
	private void responsePrinter(boolean serviceResponse){
		if(serviceResponse){
			System.out.println("DONE!");
		}else{
			System.out.println("ERROR :-(");
		}
	}
	
	private void responsePrinter(String serviceResponse, String report){
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
