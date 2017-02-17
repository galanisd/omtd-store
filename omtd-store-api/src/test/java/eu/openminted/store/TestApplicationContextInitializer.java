package eu.openminted.store;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import eu.openminted.store.config.ApplicationConfigurator;

public class TestApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext){
        ApplicationConfigurator c = new ApplicationConfigurator(); 
        c.configure();
    }
}