package eu.openminted.store.restclient.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import eu.openminted.store.restclient.StoreRESTClient;

/**
 * @author galanisd
 *
 */
@SpringBootApplication
public class ApplicationBoot implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(ApplicationBoot.class);

	@Override
	public void run(String... args) throws Exception {
		log.info("Starting");	
	}
	
	//@Bean
	public RestClientTester getTester() {
		RestClientTester tester = new RestClientTester(restClient("http://localhost/"));
		return tester;
	}

	//@Bean
	public StoreRESTClient restClient(String endpoint) {
		return new StoreRESTClient(endpoint);
	}

	//@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	// == === ==
	public static void main(String args[]) {
		SpringApplication.run(ApplicationBoot.class, args);
	}


}
