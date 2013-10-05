package net.ab.dal.nosql.appworld.downloads;

import net.ab.dal.nosql.hbase.HBaseUtil;
import net.ab.dal.nosql.hbase.ops.HBaseOperationException;
import net.ab.dal.nosql.hbase.ops.HbaseOpsImpl;

import org.apache.hadoop.hbase.client.Increment;
import org.apache.log4j.Logger;

public class HBDigitalLockerStatsWorker implements Runnable {
	static Logger log = Logger.getLogger(HBDigitalLockerStatsWorker.class);
	private static final long INC_ONE_LONG = 1l;
	//private static final long DEC_ONE_LONG = -1l;
	
	private static byte[] statsColFamily = HBaseUtil.getColumnfamily(new HBDigitalLockerStatsDAO());
	private static String tableName = HBaseUtil.getTableName(new HBDigitalLockerStatsDAO());
	
	
	
	private HBDigitalLockerDAO locker = null;
	
	
	public HBDigitalLockerStatsWorker (HBDigitalLockerDAO locker) {
		this.locker = locker;		
	}
	
	public void run() {
		if(this.locker!=null){
			Increment inc = new Increment(HBaseUtil.getBytes(this.locker.getCustomerId()));
			
			//Apps
			if(locker.getProductTypeId().longValue() == 1l){
				if(locker.getLicenseTypeId().longValue() == 1)
					inc.addColumn(statsColFamily, HBaseUtil.getBytes("freeApp"), INC_ONE_LONG);
				else {
					inc.addColumn(statsColFamily, HBaseUtil.getBytes("app"), INC_ONE_LONG);					
				}
			} else if(locker.getProductTypeId().longValue() == 2l){
				if(locker.getLicenseTypeId().longValue() == 1)
					inc.addColumn(statsColFamily, HBaseUtil.getBytes("freeGame"), INC_ONE_LONG);
				else {
					inc.addColumn(statsColFamily, HBaseUtil.getBytes("game"), INC_ONE_LONG);					
				}
			} else if(locker.getProductTypeId().longValue() == 3l){
				inc.addColumn(statsColFamily, HBaseUtil.getBytes("theme"), INC_ONE_LONG);
			} else if(locker.getProductTypeId().longValue() == 4l){
				inc.addColumn(statsColFamily, HBaseUtil.getBytes("rt"), INC_ONE_LONG);
			} else if(locker.getProductTypeId().longValue() == 5l){
				inc.addColumn(statsColFamily, HBaseUtil.getBytes("music"), INC_ONE_LONG);
			} else if(locker.getProductTypeId().longValue() == 6l){
				inc.addColumn(statsColFamily, HBaseUtil.getBytes("video"), INC_ONE_LONG);
			} else if(locker.getProductTypeId().longValue() == 7l){
				inc.addColumn(statsColFamily, HBaseUtil.getBytes("magazine"), INC_ONE_LONG);
			} else if(locker.getProductTypeId().longValue() == 8l){
				inc.addColumn(statsColFamily, HBaseUtil.getBytes("book"), INC_ONE_LONG);
			}
			
			try {
				HbaseOpsImpl.getInstance().incrementCounter(tableName, inc);
				
				/*if(log.isDebugEnabled())
					log.debug("Review Stats updated for Content: "+this.review.getContentId());*/
				
			} catch (HBaseOperationException e) {
				log.error("Error recording stats for review :"+this.locker,e);
			}
		}	
	}
}
