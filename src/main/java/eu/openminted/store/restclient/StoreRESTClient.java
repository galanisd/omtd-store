package eu.openminted.store.restclient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

/**
 * A client for OMTD Store.
 * @author galanisd
 *
 */
public class StoreRESTClient {

	private static final Logger log = LoggerFactory.getLogger(StoreRESTClient.class);

	private RestTemplate restTemplate;
	private String endpoint;

	/**
	 * Constructor.
	 * @param endpoint
	 */
	public StoreRESTClient(String endpoint) {		
		this.endpoint = endpoint;
		this.restTemplate = new RestTemplate();	
	}

	/**
	 * List all files of the Store.
	 * @return
	 */
	public String listFiles() {
		String response = restTemplate.getForObject(endpoint + "/store/listFiles/", String.class);
		return response;
	}

	/**
	 * Delete all files in the Store.
	 * @return
	 */
	public String deleteAll() {
		String response = restTemplate.getForObject(endpoint + "/store/deleteAll/", String.class);
		return response;
	}
	
	/**
	 * Creates an archive
	 * @return the id of the archive.
	 */
	public String createArchive() {
		String response = restTemplate.getForObject(endpoint + "/store/createArchive/", String.class);
		return response;
	}

	/**
	 * Deletes an archive.
	 * @return the id of the archive.
	 */
	public String deleteArchive(String archiveID) {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("archiveID", archiveID);
		
		return post(endpoint + "/store/deleteArchive/", map);
	}
			
	/**
	 * Creates an subarchive
	 * @return the id of the subarchive.
	 */
	public String createSubArchive(String archiveID, String archive) {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("archiveID", archiveID);
		map.add("archiveName", archive);
				
		return post(endpoint + "/store/createSubArchive/", map);
	}
	
	/**
	 * Finalize archive.
	 * @return the id of the archive.
	 */
	public String finalizeArchive(String archiveID) {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("archiveID", archiveID);
		
		return post(endpoint + "/store/finalizeArchive/", map);
	}
	
	/**
	 * Create a post message to {@code serviceEndpoint}.
	 * @param serviceEndpoint
	 * @param map
	 * @return
	 */
	private String post(String serviceEndpoint, MultiValueMap<String, Object> map){
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);	
		ResponseEntity<String> st = restTemplate.postForEntity(serviceEndpoint, requestEntity, String.class);
		return st.getBody();
	}
	
	/**
	 * Uploads a file in a archive.
	 * @param file
	 * @param archiveID
	 * @param fileName
	 * @return
	 */
	public String updload(File file, String archiveID, String fileName) {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("file", new FileSystemResource(file));
		map.add("fileName", fileName);
		map.add("archiveID", archiveID);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setBufferRequestBody(false);
		restTemplate.setRequestFactory(requestFactory);
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);	
		ResponseEntity<String> st = restTemplate.postForEntity(endpoint + "/store/uploadFile/", requestEntity, String.class);
		
		return st.getBody();
	}	
	
	/**
	 * Downloads a file.
	 * @param fileName
	 * @param destination
	 * @return
	 */
	public boolean downloadFile(String fileName, String destination) {		
		// Parameters
		MultiValueMap<String, Object> callParameters = new LinkedMultiValueMap<String, Object>();
		callParameters.add("fileName", fileName);
		
		return downloadFromServer(callParameters, "/store/downloadFile/", fileName, destination); 		
	}
	
	/**
	 * Downloads a file.
	 * @param fileName
	 * @param destination
	 * @return
	 */
	public boolean downloadArchive(String archiveID, String destination) {		
		// Parameters
		MultiValueMap<String, Object> callParameters = new LinkedMultiValueMap<String, Object>();
		callParameters.add("archiveID", archiveID);
		
		return downloadFromServer(callParameters, "/store/downloadArchive/", archiveID, destination);		
	}
	
	/**
	 * Downloads from server.
	 * @param parameters
	 * @param service
	 * @param fileName
	 * @param destination
	 * @return
	 */
	private boolean downloadFromServer(MultiValueMap<String, Object> parameters, String service, String fileName, String destination){
		try{			
			// Callback
			RequestCallback requestCallback = new DataRequestCallback(parameters);			
			// Streams the response.
			ResponseExtractor<Void> responseExtractor = response -> {
			    // Write the response to a file.
			    Path path = Paths.get(destination);
			    Files.copy(response.getBody(), path, StandardCopyOption.REPLACE_EXISTING);
			    return null;
			};			
			restTemplate.execute(endpoint + service, HttpMethod.POST, requestCallback, responseExtractor);	
		}catch(Exception e){
			log.debug("ERROR", e);
			return false;
		}
		
		return true;
	}
	
	// == === ==
    private class DataRequestCallback<T> implements RequestCallback {
    	 
        private List<MediaType> mediaTypes = Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL);
 
        private HttpEntity<T> requestEntity;
 
        public DataRequestCallback(T entity) {
            requestEntity = new HttpEntity<>(entity);
        }
 
        @SuppressWarnings("unchecked")
        @Override
        public void doWithRequest(ClientHttpRequest httpRequest) throws IOException {
        	log.debug("doWithRequest");
            httpRequest.getHeaders().setAccept(mediaTypes);
            T requestBody = requestEntity.getBody();
            Class<?> requestType = requestBody.getClass();
            HttpHeaders requestHeaders = requestEntity.getHeaders();
            MediaType requestContentType = requestHeaders.getContentType();
 
            for (HttpMessageConverter<?> messageConverter : restTemplate.getMessageConverters()) {
                if (messageConverter.canWrite(requestType, requestContentType)) {
                    if (!requestHeaders.isEmpty()) {
                        httpRequest.getHeaders().putAll(requestHeaders);
                    }
                    ((HttpMessageConverter<Object>) messageConverter).write(requestBody, requestContentType, httpRequest);
                    return;
                }
            }                     
        }
    }
	
}
