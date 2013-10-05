package net.ab.dal.nosql.appworld.reviews;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.ab.dal.nosql.hbase.HBaseUtil;
import net.ab.dal.nosql.hbase.InvalidHBaseBeanException;
import net.ab.dal.nosql.hbase.common.AWHBaseException;
import net.ab.dal.nosql.hbase.config.HBaseProperties;
import net.ab.dal.nosql.hbase.ops.AsyncHBaseOps;
import net.ab.dal.nosql.hbase.ops.HBaseOperationException;
import net.ab.dal.nosql.hbase.ops.HbaseOpsImpl;
import net.ab.dal.nosql.hbase.ops.IHBaseOps;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author abakle
 * @version BBW 4.4.0
 */
public class HBReviewsImpl implements IReviews{
	IHBaseOps hbOps = HbaseOpsImpl.getInstance();
	public static Properties exRes = HBaseProperties.getExceptionResource();	
	static IReviews hbReview = null;
	public static IReviews getInstance() {
		if (hbReview == null) {
			hbReview = new HBReviewsImpl();
		}		
		return hbReview;
	}
	
	public HBReviewDAO getReview(HBReviewParam nsparam) throws AWHBaseException {
		HBReviewParam param = (HBReviewParam) nsparam;
		if(null != param){
			if(!param.isRowKeyComplete())
				throw new AWHBaseException("1003",exRes.getProperty("1003"));
			else {
				try {
					return hbOps.get(HBaseUtil.getTableName(param), param.getRowKey(), HBReviewDAO.class);
				} catch (HBaseOperationException e) {
					throw new AWHBaseException("1000",exRes.getProperty("1000"),e);
				} catch (InvalidHBaseBeanException e) {
					throw new AWHBaseException("1001",exRes.getProperty("1001"),e);
				}
			}
		} else {
			throw new AWHBaseException("1002",exRes.getProperty("1002"));
		}
	}

	public List<HBReviewDAO> getReviews(HBReviewParam nsparam) throws AWHBaseException {
		HBReviewParam param = (HBReviewParam) nsparam;
		if(null==param)
			throw new AWHBaseException("1002",exRes.getProperty("1002"));
		Scan s = new Scan();
		FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL);
		try {
			s.setCaching(param.getNumRows().intValue());
			// Added RowKey filter to get specific records.
			fl.addFilter(HBaseUtil.getRowFilter(param));
			//fl.addFilter(HBaseUtil.getPrefixFilter(param));
			
			//Get Column Value based filter 
			FilterList colFl = HBaseUtil.getColumnFilter(param);
			
			if(colFl!=null)
				fl.addFilter(colFl);
			
			// for getting defined number of records.
			fl.addFilter(new PageFilter(param.getNumRows()));
			
			// incrementing by least byte to exclude the record in next page.
			if(param.getStartRowKey()!=null && !param.getStartRowKey().trim().isEmpty())
				s.setStartRow(Bytes.add(Bytes.toBytes(param.getStartRowKey()), Bytes.toBytes(0)));
			
			s.setFilter(fl);

			// TODO: How to do this without copying all ements into a new array?
			List<HBReviewDAO> w = new ArrayList<HBReviewDAO>();
			List<HBReviewDAO> w2= hbOps.scan(HBaseUtil.getTableName(param), HBReviewDAO.class, s);
			w.addAll(w2);
			return w;
			
		} catch (HBaseOperationException e) {
			throw new AWHBaseException("1000",exRes.getProperty("1000"),e);
		} catch (Exception e) {
			throw new AWHBaseException("9000",exRes.getProperty("9000"),e);
		}
	}

	public HBReviewDAO isReviewPresent(HBReviewParam nsparam) throws AWHBaseException {
		HBReviewParam param = (HBReviewParam) nsparam;
		if(null==param)
			throw new AWHBaseException("1002",exRes.getProperty("1002"));
		if(!param.isRowKeyComplete(false)){
			throw new AWHBaseException("1003",exRes.getProperty("1003"));
		} else {
			try{
				Scan s = new Scan();
				FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL);
				// Added RowKey filter to get specific records.
				fl.addFilter(HBaseUtil.getRowFilter(param));
				
				//Get Column Value based filter 
				FilterList colFl = HBaseUtil.getColumnFilter(param);
				if(colFl!=null)
					fl.addFilter(colFl);
				
				//if Client is not interested in the data then use below two filters.
				/*
				fl.addFilter(new KeyOnlyFilter());
				fl.addFilter(new FirstKeyOnlyFilter());
				*/
				
				s.setFilter(fl);
				List<HBReviewDAO> rt = hbOps.scan(HBaseUtil.getTableName(param), HBReviewDAO.class, s);
				
				if(rt!=null && rt.size()>0)
					return rt.get(0);
				else 
					return null;
			
			} catch (HBaseOperationException e) {
				throw new AWHBaseException("1000",exRes.getProperty("1000"),e);
			} catch (Exception e) {
				throw new AWHBaseException("9000",exRes.getProperty("9000"),e);
			}
		}
	}

	public void addReview(HBReviewDAO review) throws AWHBaseException {
		addReview(review,false);
	}
	
	private void addReview(HBReviewDAO nsreview, boolean update) throws AWHBaseException{
		HBReviewDAO review = (HBReviewDAO) nsreview;
		if(review!=null){
			if(!review.isRowKeyComplete(update)){
				throw new AWHBaseException("1003",exRes.getProperty("1003"));
			} else {
				if(null==review.getReverseTimeStamp())
					review.setReverseTimeStamp(Long.MAX_VALUE-System.currentTimeMillis());
				try {
					hbOps.addBean(HBaseUtil.getTableName(review), review);
					AsyncHBaseOps.getInstance().submitJob(new HBReviewStatsWorker(review, update));
				} catch (HBaseOperationException e) {
					throw new AWHBaseException("1000",exRes.getProperty("1000"),e);
				}
			}
		} else {
			throw new AWHBaseException("1002",exRes.getProperty("1002"));
		}
	}

	public void updateReview(HBReviewDAO review) throws AWHBaseException {
		addReview(review, true);
	}

	public HBReviewStatsDAO getReviewStats(HBReviewStatsDAO nsreviewStat) throws AWHBaseException {
		HBReviewStatsDAO reviewStat = (HBReviewStatsDAO) nsreviewStat;
		if(null!=reviewStat){
			if(!reviewStat.isRowKeyComplete()){
				throw new AWHBaseException("1003",exRes.getProperty("1003"));
			} else {
				try {
					return hbOps.get(HBaseUtil.getTableName(reviewStat), reviewStat.getRowKey(), HBReviewStatsDAO.class);
				} catch (HBaseOperationException e) {
					throw new AWHBaseException("1000",exRes.getProperty("1000"),e);
				} catch (InvalidHBaseBeanException e) {
					throw new AWHBaseException("1001",exRes.getProperty("1001"),e);
				}
			}
		}else {
			throw new AWHBaseException("1002",exRes.getProperty("1002"));
		}
	}

}
