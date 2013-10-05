package net.ab.dal.nosql.hbase.config;

import java.net.URL;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.log4j.Logger;

public class BaseConfiguration {
	private static Configuration hbConfig = null;
	private static Logger log = Logger.getLogger(BaseConfiguration.class);
	public static final int TABLE_POOL_SIZE = Integer.parseInt(HBaseProperties.getInstance().getProperty("hbase.table.pool.size"));
	private static HTablePool hbTablePool = null;
	
	public static Configuration getInstance() {
		if(hbConfig==null){
			Configuration conf = new Configuration();
			URL ul = Thread.currentThread().getContextClassLoader().getResource(HBaseProperties.getInstance().getProperty("site.file.path"));
			conf.addResource(ul);
			if(log.isInfoEnabled())
				log.info("HBASE Config file path : "+ul.getPath());
			hbConfig = HBaseConfiguration.create(conf);
		}
		if(hbTablePool==null){
			hbTablePool = new HTablePool(hbConfig, TABLE_POOL_SIZE);
		}
		return hbConfig;
	}
	
	public static HTableInterface getTableFromPool(String tableName){
		if(hbTablePool==null){
			getInstance();
		}
		return hbTablePool.getTable(tableName);
	}
	
}
