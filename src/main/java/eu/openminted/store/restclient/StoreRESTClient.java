package eu.openminted.store.restclient;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author galanisd
 *
 */
public class StoreRESTClient {

	private static final Logger log = LoggerFactory.getLogger(ApplicationBoot.class);

	private RestTemplate restTemplate;
	private String endpoint;

	/**
	 * Constructor.
	 * 
	 * @param endpoint
	 */
	public StoreRESTClient(String endpoint) {
		this.restTemplate = new RestTemplate();
		this.endpoint = endpoint;
	}

	public String listFiles() {
		String response = restTemplate.getForObject(endpoint + "/store/listFiles/", String.class);
		return response;
	}

	public String createArchive() {
		String response = restTemplate.getForObject(endpoint + "/store/createArchive/", String.class);
		return response;
	}

	public String deleteAll() {
		String response = restTemplate.getForObject(endpoint + "/store/deleteAll", String.class);
		return response;
	}
	
	public String updload(File file, String archiveID, String fileName) {

		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		parts.add("file", new FileSystemResource(file));
		parts.add("fileName", fileName);
		parts.add("archiveID", archiveID);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(parts,
				headers);
	
		restTemplate.postForEntity(endpoint + "/store/uploadFile/", requestEntity, String.class);
		return "";
	}
		
}
