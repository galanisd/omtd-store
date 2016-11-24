package eu.openminted.storage.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import eu.openminted.storage.StoreService;

@Controller
public class StorageController {

    private final StoreService storageService;

    @Autowired
    public StorageController(StoreService storageService) {
        this.storageService = storageService;
    }
    
    /*
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String listUploadedFiles(Model model) throws IOException {
        
        return "uploadForm";
    }
    */
    
    /*
    @RequestMapping(value="/storage/uploadresource", method=RequestMethod.POST )
    @ResponseBody
    public String uploadFile(@RequestParam("file") MultipartFile file){
    	
    	String status = "Uploaded";
    	
    	try{
    		String name = file.getOriginalFilename();
    		System.out.println("name: " + name);
    		storageService.store(file);
    	}catch(Exception e){
    		status = "Failed to upload " + e.getMessage();	
    	}
    	
    	return status;
    }*/
        
    /*
    @RequestMapping(value="/storage/downloadresource", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam("file") String filename){    	
       	    	
        Resource file = storageService.retrieve(filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .body(file);    	
    }
        
    @RequestMapping(value="/storage/listresources", method=RequestMethod.GET)
    @ResponseBody
    public String listResources(){
    	String response = "---Resources list---\n\r";
    	try{
    		File[] resources = storageService.listResources();
    		for(int i = 0; i < resources.length; i++){
    			response = response + resources[i].getAbsolutePath() + "\n\r";
    		}
    	}catch(Exception e){    		
    		response = "An error has occured." + e.getMessage();
    	}
    	    		
    	return response;    	
    }
    */
}
