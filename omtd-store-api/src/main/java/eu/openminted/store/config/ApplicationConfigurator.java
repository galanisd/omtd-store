package eu.openminted.store.config;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author galanisd
 *
 */
public class ApplicationConfigurator {
	
	private final static Logger log = LoggerFactory.getLogger(ApplicationConfigurator.class);
	
	/**
	 * Configure the app, i.e., locate and set the required properties.
	 * (I.e., {@link ApplicationConfigParams#storeApplicationCfg}).
	 */
	public void configure(){
		
		// Disable restarts.
		System.setProperty("spring.devtools.restart.enabled", "false");
				
		// Retrieve applicationPropertiesFile location.		
		String applicationPropertiesFile = System.getProperty(ApplicationConfigParams.storeApplicationCfg);		
		// If is not provided load default applications properties (based on applicationPropertiesDefaultHolder).
		if(applicationPropertiesFile == null){						
			Properties props = new Properties();						
			try{
				log.info("==> Scanning "  + ApplicationConfigParams.storeApplicationCfgDefaultHolder + " for default config");
				
				InputStream is = ApplicationConfigurator.class.getResourceAsStream(ApplicationConfigParams.storeApplicationCfgDefaultHolder);
				
				if(is != null){	
					props.load(is);				
					log.info("==> Loading default app config from " + props.getProperty(ApplicationConfigParams.storeApplicationCfg));
					System.setProperty(ApplicationConfigParams.storeApplicationCfg, props.getProperty(ApplicationConfigParams.storeApplicationCfg));
				}else{
					throw new Exception("Stream for " + ApplicationConfigParams.storeApplicationCfgDefaultHolder + " is null");
				}
			}catch(Exception e){
				log.info("ERROR:", e);
			}								
		}else{
			log.info("==> Config:" + System.getProperty(ApplicationConfigParams.storeApplicationCfg));
		}
	}
}
