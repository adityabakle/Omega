package com.cellmania.cmreports.db.sqlsession;

import java.io.Reader;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionConfig
{
	static private Log log = LogFactory.getLog(SqlSessionConfig.class);
	private static SqlSessionFactory sqlSessionFactory;
	
	static {
	  Properties prop = null;
	  String devMode = null;
	  String resource = null;
      try {
    	  prop = Resources.getResourceAsProperties("OracleConfig.properties");
    	  devMode = prop.getProperty("jndi.mode");
    	  resource = prop.getProperty("sqlsession.config.resource.xml");
    	  log.debug("sqlsession_resource: " + resource);
      } catch(Exception e) {
    	  log.error(e);
    	  e.printStackTrace();
      }
      
      Reader reader= null;
      try
      {
    	  reader = Resources.getResourceAsReader(resource);
    	  SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
    	  if(devMode != null && "Y".equals(devMode)) {
             log.info("IN Dev MODE");
             sqlSessionFactory = builder.build(reader, "development");
          } else {
        	  sqlSessionFactory = builder.build(reader, "production");
          }  
    	  
    	  if(sqlSessionFactory!=null)
    		  log.info("sqlSessionFactory : "+sqlSessionFactory.getConfiguration().getEnvironment().getId());
      } catch(Exception e) {
    	  log.error(e);
    	  e.printStackTrace();
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
   
   public static final SqlSessionFactory getSqlMapClient() {
      return sqlSessionFactory;
   }
   
}
