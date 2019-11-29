package utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

public class PropertyHotReloader {
	 private static PropertiesConfiguration configuration = null;

	    static {
	        try {
	            configuration = new PropertiesConfiguration("C:\\ApacheWebServer2.4\\efficenz.properties");
//	            configuration = new PropertiesConfiguration("/resources/efficenz.properties");
	        } catch (ConfigurationException e) {
	            e.printStackTrace();
	        }
	        configuration.setReloadingStrategy(new FileChangedReloadingStrategy());
	    }

	    public static synchronized String getProperty(final String key) {
	        return (String) configuration.getProperty(key);
	    }
}
