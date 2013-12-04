package com.cellmania.carriers.sqlSession;

import java.io.Reader;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.cellmania.cmreports.db.masters.CarrierMasterDTO;
import com.cellmania.cmreports.web.util.CMDBService;
import com.cellmania.cmreports.web.util.ServerSettingsConstants;

public class CarrierSqlSessionConfig
{
	static private Log log = LogFactory.getLog(CarrierSqlSessionConfig.class);
	private static Hashtable<String, SqlSessionFactory> carrierSqlSessionFactory = new Hashtable<String, SqlSessionFactory>();
	private static String devMode = null;
	private static String sqlConfigPath = null;
	private static Properties prop = null;
	static {
      try {
    	  prop = Resources.getResourceAsProperties("OracleConfig.properties");
    	  devMode = prop.getProperty("dev.mode");
    	  sqlConfigPath = CMDBService.getServerSettingsValue(ServerSettingsConstants._CARRIER_SQLCONFIG_PATH);
      } catch(Exception e) {
    	  log.error("Error loading Static varaibles in CarrierSqlSessionConfig",e);
      }
      
     
   }
   public static void reloadStaticConfig(){
	   carrierSqlSessionFactory = new Hashtable<String, SqlSessionFactory>();
	   try {
		sqlConfigPath = CMDBService.getServerSettingsValue(ServerSettingsConstants._CARRIER_SQLCONFIG_PATH);
	} catch (Exception e) {
		log.error("Error re-loading Static varaibles in CarrierSqlSessionConfig",e);
	}
   }
   public static synchronized SqlSessionFactory getSqlMapClient(CarrierMasterDTO cDto) {
	   SqlSessionFactory sqlSessionFactory = carrierSqlSessionFactory.get(cDto.getName());
	   
	   if(sqlSessionFactory!=null){
		   // check if the sqlfactroy is still valid to hold connection.
		   SqlSession ses = null;
 		  try{
 			  log.info("Checking If SqlFactory is still valid ["+cDto.getName()+"].");
 			  ses = sqlSessionFactory.openSession();
 			 log.info("Yes The session is still valid ["+cDto.getName()+"].");
 			
 		  } catch(Exception e){
 			  // if exception raising connection with TNS go for JDBC.
 			  log.info("Error opening connection with TNS so switching to JDBC :"+cDto.getDisplayName());
 			  log.error("Error creating connection with TNS : "+e.getMessage(), e);
 			  sqlSessionFactory = null;
 		  } finally {
 			  if(null != ses)
 				  ses.close();
 		  }
	   }
	   
	   if(sqlSessionFactory == null){
		   Reader reader= null;
		      try {
		    	  System.setProperty("oracle.net.tns_admin",cDto.getTnsFile());
		    	  reader = Resources.getResourceAsReader(sqlConfigPath + cDto.getSqlMapFile());
		    	  SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
		    	  
		    	  
		    	  Properties carProp = new Properties();
		    	  System.out.println("DB DRIVER : "+ prop.getProperty("driver"));
		    	  carProp.put("driver",prop.getProperty("driver"));
		    	  carProp.put("dbUserName", cDto.getDbUserName());
		    	  carProp.put("dbPassword", cDto.getDbPassword());
		    	  carProp.put("dbServerName",cDto.getDbServerName());
		    	  carProp.put("dbServiceId", cDto.getDbServiceId());
		    	  carProp.put("dbPort", cDto.getDbPort().toString());
		    	  carProp.put("dbTnsName", cDto.getDbTnsName());
		    	  
		    	  if(devMode != null && "Y".equals(devMode)) 
		             sqlSessionFactory = builder.build(reader, "development",carProp);		    	  	
		          else {
		        	  if(cDto.getTnsLookup()!=null && cDto.getTnsLookup().booleanValue()){
		        		  sqlSessionFactory = builder.build(reader, "productionTNS",carProp);
		        		  SqlSession ses = null;
		        		  try{
		        			  ses = sqlSessionFactory.openSession();
		        		  } catch(Exception e){
		        			  // if exception raising connection with TNS go for JDBC.
		        			  log.info("Error opening connection with TNS so switching to JDBC :"+cDto.getDisplayName());
		        			  log.error("Error creating connection with TNS : "+e.getMessage(), e);
		        			  sqlSessionFactory = builder.build(reader, "production",carProp);
		        		  } finally {
		        			  if(null != ses)
		        				  ses.close();
		        		  }
		        	  }
		        	  else {
		        		  sqlSessionFactory = builder.build(reader, "production",carProp);
		        	  }
		          }
		          if(sqlSessionFactory!=null){
		    		  log.info("SqlSessionFactory for :("+cDto.getSqlMapFile()+") "+sqlSessionFactory.getConfiguration().getEnvironment().getId());
		    		  carrierSqlSessionFactory.put(cDto.getName(), sqlSessionFactory);
		          }
		      } catch(Exception e) {
		    	  log.error(e);
		      }
		      finally {
		    	  if(reader != null) {
		    		  try {
		    			  reader.close();
		    		  } catch(Exception ex) {
		    			  log.error(ex);
		    		  }
		    	  }
		      }   
	   } 
      return sqlSessionFactory;
   }
   
}
