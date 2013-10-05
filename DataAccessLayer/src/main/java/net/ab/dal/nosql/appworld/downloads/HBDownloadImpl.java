package net.ab.dal.nosql.appworld.downloads;

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

public class HBDownloadImpl {

	public static Properties exRes = HBaseProperties.getExceptionResource();	

	private IHBaseOps hbOps = HbaseOpsImpl.getInstance();
	
	
	
	public void addDownload(HBDownloadDAO nsDownload) throws AWHBaseException {
		HBDownloadDAO download = (HBDownloadDAO) nsDownload;
		if (download.getReverseTimeStamp() == null) {
			download.setReverseTimeStamp(Long.MAX_VALUE - System.currentTimeMillis());
		}
		try {
			hbOps.addBean(HBaseUtil.getTableName(download), download);
			AsyncHBaseOps.getInstance().submitJob(new HBDownloadStatsWorker(download));
		} catch (HBaseOperationException hbopex) {
			throw new AWHBaseException("1000", exRes.getProperty("1000"), hbopex);
		}
	}

	
	public void updateDigitalLockerRecord(HBDigitalLockerDAO lockerRec) throws AWHBaseException {
		addDigitalLockerRecord(lockerRec, true);
	}

	public void addDigitalLockerRecord(HBDigitalLockerDAO lockerRec) throws AWHBaseException {
		addDigitalLockerRecord(lockerRec, false);
	}
		
	private void addDigitalLockerRecord(HBDigitalLockerDAO nsLockerRec, boolean update) throws AWHBaseException {
		HBDigitalLockerDAO lockerRec = (HBDigitalLockerDAO) nsLockerRec;
		if (lockerRec != null) {
			if ( !lockerRec.isRowKeyComplete(update)){
				throw new AWHBaseException("1003", exRes.getProperty("1003"));
			} else {
				try {
					hbOps.addBean(HBaseUtil.getTableName(lockerRec), lockerRec);
					if(!update)
						AsyncHBaseOps.getInstance().submitJob(new HBDigitalLockerStatsWorker(lockerRec));
				} catch (HBaseOperationException hbopex) {
					throw new AWHBaseException("1000", exRes.getProperty("1000"), hbopex);
				}
			}
		} else {
			throw new AWHBaseException("1002", exRes.getProperty("1002"));
		}
	}


	public List<HBDigitalLockerDAO> getDigitalLocker(HBDigitalLockerParam nsparam) throws AWHBaseException {
		HBDigitalLockerParam param = (HBDigitalLockerParam) nsparam;
		if (param == null) {
			throw new AWHBaseException("1002", exRes.getProperty("1002"));
		}
		
		Scan s = new Scan();
		
		FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL);
		try {
			s.setCaching(param.getNumRows().intValue());
			// Added RowKey filter to get specific records.
			fl.addFilter(HBaseUtil.getRowFilter(param));
			
			//Get Column Value based filter 
			FilterList colFl = HBaseUtil.getColumnFilter(param);
			if (colFl != null) {
				fl.addFilter(colFl);
			}
			
			// for getting defined number of records.
			fl.addFilter(new PageFilter(param.getNumRows()));
			
			// incrementing by least byte to exclude the record in next page.
			if (param.getStartRowKey() != null && !param.getStartRowKey().trim().isEmpty()) {
				s.setStartRow(Bytes.add(Bytes.toBytes(param.getStartRowKey()), Bytes.toBytes(0)));
			}
			
			s.setFilter(fl);
			
			// TODO: How to do this without copying all ements into a new array?
			List<HBDigitalLockerDAO> w = new ArrayList<HBDigitalLockerDAO>();
			List<HBDigitalLockerDAO> w2= hbOps.scan(HBaseUtil.getTableName(param), HBDigitalLockerDAO.class, s);
			w.addAll(w2);
			return w;
			
		} catch (HBaseOperationException e) {
			throw new AWHBaseException("1000",exRes.getProperty("1000"),e);
		} catch (Exception e) {
			throw new AWHBaseException("9000",exRes.getProperty("9000"),e);
		}
	}


	public HBDownloadStatsDAO getDownloadStats(HBDownloadStatsDAO nsdStat)
			throws AWHBaseException {
		HBDownloadStatsDAO dStat = (HBDownloadStatsDAO) nsdStat;
		if(null!=dStat){
			if(!dStat.isRowKeyComplete()){
				throw new AWHBaseException("1003",exRes.getProperty("1003"));
			} else {
				try {
					return hbOps.get(HBaseUtil.getTableName(dStat), dStat.getRowKey(), HBDownloadStatsDAO.class);
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
	
	public HBDigitalLockerStatsDAO getCustomerLockerStats(HBDigitalLockerStatsDAO nsdlStat)
			throws AWHBaseException {
		HBDigitalLockerStatsDAO dlStat = (HBDigitalLockerStatsDAO) nsdlStat;
		if(null!=dlStat){
			if(!dlStat.isRowKeyComplete()){
				throw new AWHBaseException("1003",exRes.getProperty("1003"));
			} else {
				try {
					return hbOps.get(HBaseUtil.getTableName(dlStat), dlStat.getRowKey(), HBDigitalLockerStatsDAO.class);
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
