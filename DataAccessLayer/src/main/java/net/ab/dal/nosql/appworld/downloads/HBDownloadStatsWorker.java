package net.ab.dal.nosql.appworld.downloads;

import java.util.ArrayList;
import java.util.List;

import net.ab.dal.nosql.hbase.HBaseUtil;
import net.ab.dal.nosql.hbase.ops.HBaseOperationException;
import net.ab.dal.nosql.hbase.ops.HbaseOpsImpl;

import org.apache.hadoop.hbase.client.Increment;
import org.apache.log4j.Logger;

public class HBDownloadStatsWorker implements Runnable {
	static Logger log = Logger.getLogger(HBDownloadStatsWorker.class);
	private static final long INC_ONE_LONG = 1l;
	//private static final long DEC_ONE_LONG = -1l;
	
	private static byte[] statsColFamily = HBaseUtil.getColumnfamily(new HBDownloadStatsDAO());
	private static String tableName = HBaseUtil.getTableName(new HBDownloadStatsDAO());
	private static byte[] stats_count = HBaseUtil.getBytes("count");
	
	
	private HBDownloadDAO download = null;
	
	
	public HBDownloadStatsWorker (HBDownloadDAO download) {
		this.download = download;		
	}
	
	public void run() {
		List<Increment> incs = null;
		if(this.download!=null){
			incs = new ArrayList<Increment>();
			
			String strRowKey = this.download.getContentId().toString();
			Increment inc = new Increment(HBaseUtil.getBytes(strRowKey));
			//Amount in cents so long value can be saved.
			inc.addColumn(statsColFamily, stats_count, INC_ONE_LONG);
			incs.add(inc);
			
			if(null!= this.download.getPlatformDeviceTypeMapId()){
				strRowKey += "-"+this.download.getPlatformDeviceTypeMapId();
				inc = new Increment(HBaseUtil.getBytes(strRowKey));
				inc.addColumn(statsColFamily, stats_count, INC_ONE_LONG);
				incs.add(inc);
			}
			try {
				HbaseOpsImpl.getInstance().incrementCounter(tableName, incs);
				
				/*if(log.isDebugEnabled())
					log.debug("Review Stats updated for Content: "+this.review.getContentId());*/
				
			} catch (HBaseOperationException e) {
				log.error("Error recording stats for review :"+this.download,e);
			}
		}	
	}
}
