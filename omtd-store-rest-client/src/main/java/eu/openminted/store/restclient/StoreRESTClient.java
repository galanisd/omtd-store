package eu.openminted.store.restclient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import eu.openminted.store.common.OMTDStoreHandler;
import eu.openminted.store.common.StoreREST;
import eu.openminted.store.common.StoreResponse;

/**
 * A client for OMTD Store.
 * @author galanisd
 *
 */
@Component
public class StoreRESTClient implements OMTDStoreHandler{

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
		
		init();
	}

	/**
	 * Constructor.
	 */
	public StoreRESTClient() {				
		this.restTemplate = new RestTemplate();
		init();
	}
	
	private void init(){
		//List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		//messageConverters.add(new MappingJackson2HttpMessageConverter());
		//this.restTemplate.setMessageConverters(messageConverters);
	}
	
	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
	// == === ==
	// Implementation for OMTDStoreHandler
	// == === ==
	// == === ==
	// == === ==	
	// == === ==
	
	@Override
	public StoreResponse createArchive(String archiveId) {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add(StoreREST.archiveID, archiveId);
		
		return post(destination(endpoint, StoreREST.createArchive), map);
	}

	@Override
	public StoreResponse archiveExists(String archiveId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoreResponse fileExistsInArchive(String archiveId, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public StoreResponse listFiles() {		
		StoreResponse response = restTemplate.getForObject(destination(endpoint, StoreREST.listFiles), StoreResponse.class);
		return response;
	}

	@Override
	public StoreResponse deleteAll() {
		StoreResponse response = restTemplate.getForObject(destination(endpoint, StoreREST.deleteAll), StoreResponse.class);
		return response;
	}
	
	@Override
	public StoreResponse createArchive() {
		StoreResponse response = restTemplate.getForObject(destination(endpoint, StoreREST.createArchive), StoreResponse.class);
		return response;
	}

	@Override
	public StoreResponse deleteArchive(String archiveID) {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add(StoreREST.archiveID, archiveID);
		
		return post(destination(endpoint, StoreREST.deleteArchive), map);
	}
			
	@Override
	public StoreResponse createSubArchive(String archiveID, String archive) {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add(StoreREST.archiveID, archiveID);
		map.add("archiveName", archive);
				
		return post(destination(endpoint, StoreREST.createSubArchive), map);
	}
	
	@Override
	public StoreResponse finalizeArchive(String archiveID) {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("archiveID", archiveID);
		
		return post(destination(endpoint, StoreREST.finalizeArchive), map);
	}
		
	@Override
	public StoreResponse storeFile(File file, String archiveID, String fileName) {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("file", new FileSystemResource(file));
		map.add("fileName", fileName);
		map.add(StoreREST.archiveID, archiveID);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setBufferRequestBody(false);
		restTemplate.setRequestFactory(requestFactory);
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);	
		ResponseEntity<StoreResponse> st = restTemplate.postForEntity(destination(endpoint, StoreREST.uploadFile), requestEntity, StoreResponse.class);
		
		StoreResponse resp = st.getBody(); 
		return resp;
	}	
	
	@Override
	public StoreResponse downloadFile(String fileName, String destination) {		
		// Parameters
		MultiValueMap<String, Object> callParameters = new LinkedMultiValueMap<String, Object>();
		callParameters.add("fileName", fileName);
		
		boolean resp = downloadFromServer(callParameters, StoreREST.downloadFile, fileName, destination);
		
		return new StoreResponse(String.valueOf(resp), "");
	}
	
	@Override
	public StoreResponse downloadArchive(String archiveID, String localDestination) {		
		// Parameters
		MultiValueMap<String, Object> callParameters = new LinkedMultiValueMap<String, Object>();
		callParameters.add(StoreREST.archiveID, archiveID);
		
		boolean resp = downloadFromServer(callParameters, StoreREST.downloadArchive, archiveID, localDestination);
		
		return new StoreResponse(String.valueOf(resp), "");
	}

	// == === ==
	// == === ==	
	// == === ==
	
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
			restTemplate.execute(destination(endpoint, service), HttpMethod.POST, requestCallback, responseExtractor);	
		}catch(Exception e){
			log.debug("ERROR", e);
			return false;
		}
		
		return true;
	}
	
	private String destination(String endpoint, String service){
		return endpoint + service;
	}
	
	/**
	 * Create a post message to {@code serviceEndpoint}.
	 * @param serviceEndpoint
	 * @param map
	 * @return
	 */
	private StoreResponse post(String serviceEndpoint, MultiValueMap<String, Object> map){
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);	
		ResponseEntity<StoreResponse> st = restTemplate.postForEntity(serviceEndpoint, requestEntity, StoreResponse.class);
		return st.getBody();
	}
	
    private class DataRequestCallback<T> implements RequestCallback {
    	 
        private List<MediaType> mediaTypes = Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL);
    	//private List<MediaType> mediaTypes = Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON, MediaType.ALL);
    	
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
