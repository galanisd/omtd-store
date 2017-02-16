package eu.openminted.store.config;

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
	 * Configure the app, i.e., locate and set the {@link ApplicationConfigParams#storeApplicationCfg} file.
	 */
	public void configure(){
		// Retrieve applicationPropertiesFile location.		
		String applicationPropertiesFile = System.getProperty(ApplicationConfigParams.storeApplicationCfg);		
		// If is not provided load default applications properties (based on applicationPropertiesDefaultHolder).
		if(applicationPropertiesFile == null){						
			Properties props = new Properties();						
			try{
				log.info("==> Scanning "  + ApplicationConfigParams.storeApplicationCfgDefaultHolder + " for default config");
				props.load(ApplicationConfigurator.class.getClass().getResourceAsStream(ApplicationConfigParams.storeApplicationCfgDefaultHolder));				
				log.info("==> Loading default app config from " + props.getProperty(ApplicationConfigParams.storeApplicationCfg));
				System.setProperty(ApplicationConfigParams.storeApplicationCfg, props.getProperty(ApplicationConfigParams.storeApplicationCfg));				
			}catch(Exception e){
				log.info("ERROR:", e);
			}								
		}else{
			log.info("==> Config:" + System.getProperty(ApplicationConfigParams.storeApplicationCfg));
		}
	}
}
