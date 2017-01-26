package eu.openminted.store.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ApplicationConfigurator {
	
	private final static Logger log = LoggerFactory.getLogger(ApplicationConfigurator.class);
	
	/**
	 * Configure the app, i.e., locate and set the {@link ApplicationConfigParams#storeApplicationCfg} file.
	 */
	public static void configure(){
		// Retrieve applicationPropertiesFile location.		
		String applicationPropertiesFile = System.getProperty(ApplicationConfigParams.storeApplicationCfg);		
		// If is not provided load default applications properties (based on applicationPropertiesDefaultHolder).
		if(applicationPropertiesFile == null){						
			Properties props = new Properties();						
			try{				
				props.load(ApplicationConfigurator.class.getResourceAsStream(ApplicationConfigParams.storeApplicationCfgDefaultHolder));				
				log.info("==> Loading default config:" + props.getProperty(ApplicationConfigParams.storeApplicationCfg));
				System.setProperty(ApplicationConfigParams.storeApplicationCfg, props.getProperty(ApplicationConfigParams.storeApplicationCfg));				
			}catch(Exception e){
				log.info("ERROR:", e);
			}								
		}else{
			log.info("==> Config:" + System.getProperty(ApplicationConfigParams.storeApplicationCfg));
		}
	}
}
