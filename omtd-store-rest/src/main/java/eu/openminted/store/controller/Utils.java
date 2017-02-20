package eu.openminted.store.controller;

import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import eu.openminted.store.core.StoreException;

/**
 * @author galanisd
 *
 */
public class Utils {
	
	public static ResponseEntity<Resource> download(InputStream fileInputStream, String fname){
        
        Resource resource  = null;
        
        try {            
            resource = new InputStreamResource(fileInputStream);
            if(!resource.exists() || !resource.isReadable()) {
            	throw new StoreException("Could not read file: " + fname);
            }
            
        } catch (StoreException e) {
            throw new StoreException("Could not read file: " + fname, e);
        }
            
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fname + "\"")
                .body(resource);     
	}
	
}
