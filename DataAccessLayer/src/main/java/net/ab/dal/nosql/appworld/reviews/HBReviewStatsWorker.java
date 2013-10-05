package net.ab.dal.nosql.appworld.reviews;

import java.util.ArrayList;
import java.util.List;

import net.ab.dal.nosql.hbase.HBaseUtil;
import net.ab.dal.nosql.hbase.ops.HBaseOperationException;
import net.ab.dal.nosql.hbase.ops.HbaseOpsImpl;

import org.apache.hadoop.hbase.client.Increment;
import org.apache.log4j.Logger;

public class HBReviewStatsWorker implements Runnable {
	static Logger log = Logger.getLogger(HBReviewStatsWorker.class);
	private static final long INC_ONE_LONG = 1l;
	private static final long DEC_ONE_LONG = -1l;
	private static final int REVIEW_APPROVED = 1;
	
	private static byte[] statsColFamily = HBaseUtil.getColumnfamily(new HBReviewStatsDAO());
	private static String tableName = HBaseUtil.getTableName(new HBReviewStatsDAO());
	private static byte[] stats_totalReviews = HBaseUtil.getBytes("totalReviews");
	private static byte[] stats_totalRatings = HBaseUtil.getBytes("totalRatings");
	private static byte[] stats_total1Star = HBaseUtil.getBytes("total1Star");
	private static byte[] stats_total2Star = HBaseUtil.getBytes("total2Star");
	private static byte[] stats_total3Star = HBaseUtil.getBytes("total3Star");
	private static byte[] stats_total4Star = HBaseUtil.getBytes("total4Star");
	private static byte[] stats_total5Star = HBaseUtil.getBytes("total5Star");
	
	
	private HBReviewDAO review = null;
	private boolean update = false;
	// increment counter when new review is added from CAPI or status of review is approved from Portal.
	private boolean incCounter = true;
	
	
	
	public HBReviewStatsWorker (HBReviewDAO review) {
		this.review = review;
		this.incCounter = true;
		statsColFamily = HBaseUtil.getColumnfamily(new HBReviewStatsDAO());
		
	}
	
	public HBReviewStatsWorker (HBReviewDAO review, boolean update) {
		this.review = review;
		this.update = update;
		if(this.update && this.review.getStatus().intValue()!= REVIEW_APPROVED){
			incCounter = false;
		}
		statsColFamily = HBaseUtil.getColumnfamily(new HBReviewStatsDAO());
	}
	
	public void run() {
		List<Increment> incs = null;
		if(this.review!=null){
			incs = new ArrayList<Increment>();
			
			String strRowKey = this.review.getContentId().toString();
			Increment inc = new Increment(HBaseUtil.getBytes(strRowKey));
			inc.addColumn(statsColFamily, stats_totalReviews, this.incCounter?INC_ONE_LONG:DEC_ONE_LONG);
			inc.addColumn(statsColFamily, stats_totalRatings, (this.incCounter?1l:-1l)*this.review.getRatingValue());
			getStarIncrement(inc,this.incCounter);
			incs.add(inc);
			
			if(null!= this.review.getReleaseId()){
				strRowKey += "-"+this.review.getReleaseId().toString();
				inc = new Increment(HBaseUtil.getBytes(strRowKey));
				inc.addColumn(statsColFamily, stats_totalReviews, this.incCounter?INC_ONE_LONG:DEC_ONE_LONG);
				inc.addColumn(statsColFamily, stats_totalRatings, (this.incCounter?1l:-1l)*this.review.getRatingValue());
				getStarIncrement(inc,this.incCounter);
				incs.add(inc);
				
				if(null!=this.review.getPlatformDeviceMapId()){
					strRowKey += "-"+this.review.getPlatformDeviceMapId().toString();
					inc = new Increment(HBaseUtil.getBytes(strRowKey));
					inc.addColumn(statsColFamily, stats_totalReviews, this.incCounter?INC_ONE_LONG:DEC_ONE_LONG);
					inc.addColumn(statsColFamily, stats_totalRatings, (this.incCounter?1l:-1l)*this.review.getRatingValue());
					getStarIncrement(inc,this.incCounter);
					incs.add(inc);
					
					if(null!=this.review.getCountryId()){
						strRowKey += "-"+this.review.getCountryId().toString();
						inc = new Increment(HBaseUtil.getBytes(strRowKey));
						inc.addColumn(statsColFamily, stats_totalReviews, this.incCounter?INC_ONE_LONG:DEC_ONE_LONG);
						inc.addColumn(statsColFamily, stats_totalRatings, (this.incCounter?1l:-1l)*this.review.getRatingValue());
						getStarIncrement(inc,this.incCounter);
						incs.add(inc);
						
						if(null!=this.review.getLanguageId()){
							strRowKey += "-"+this.review.getLanguageId().toString();
							inc = new Increment(HBaseUtil.getBytes(strRowKey));
							inc.addColumn(statsColFamily, stats_totalReviews, this.incCounter?INC_ONE_LONG:DEC_ONE_LONG);
							inc.addColumn(statsColFamily, stats_totalRatings, (this.incCounter?1l:-1l)*this.review.getRatingValue());
							getStarIncrement(inc, this.incCounter);
							incs.add(inc);
						}
					}
				}
			}
			try {
				HbaseOpsImpl.getInstance().incrementCounter(tableName, incs);
				
				/*if(log.isDebugEnabled())
					log.debug("Review Stats updated for Content: "+this.review.getContentId());*/
				
			} catch (HBaseOperationException e) {
				log.error("Error recording stats for review :"+this.review,e);
			}
		}
		
	}


	private void getStarIncrement(Increment inc, boolean increment) {
		if(this.review.getRatingValue()!=null && this.review.getRatingValue().intValue()>0){
			switch(this.review.getRatingValue().intValue()/2){
			case 1:
				inc.addColumn(statsColFamily, stats_total1Star, increment?INC_ONE_LONG:DEC_ONE_LONG);
				break;
			case 2:
				inc.addColumn(statsColFamily, stats_total2Star, increment?INC_ONE_LONG:DEC_ONE_LONG);
				break;
			case 3:
				inc.addColumn(statsColFamily, stats_total3Star, increment?INC_ONE_LONG:DEC_ONE_LONG);
				break;
			case 4:
				inc.addColumn(statsColFamily, stats_total4Star, increment?INC_ONE_LONG:DEC_ONE_LONG);
				break;
			case 5:
				inc.addColumn(statsColFamily, stats_total5Star, increment?INC_ONE_LONG:DEC_ONE_LONG);
				break;
			}
		}
	}

}
