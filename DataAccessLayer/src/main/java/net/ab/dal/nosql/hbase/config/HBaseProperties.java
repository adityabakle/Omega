package net.ab.dal.nosql.hbase.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class HBaseProperties {
	private static Properties prop = null;
	private static Properties exceptionRes = null;
	private static Logger log = Logger.getLogger(HBaseProperties.class);
	
	public static Properties getInstance(){
		if(prop==null){
			prop = new Properties();
			
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("awHBase-config.properties");
			try {
				prop.load(in);
			} catch (IOException e) {
				log.fatal("Error loading resource file.",e);
			}
		}
		return prop;
	}
	
	public static Properties reload(){
		prop = null;
		return getInstance();
	}
	
	public static Properties getExceptionResource(){
		if(exceptionRes==null){
			exceptionRes = new Properties();
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("awHBase-exceptions.properties");
			if(null==in){
				log.warn("Unable to locate \"awHBase-exceptions.properties\" file in classPath.");
			} else {
				try {
					exceptionRes.load(in);
				} catch (IOException e) {
					log.fatal("Error loading resource file. (awHBaseException-resource.properties)",e);
				}
			}
		}
		return exceptionRes;
	}
}
