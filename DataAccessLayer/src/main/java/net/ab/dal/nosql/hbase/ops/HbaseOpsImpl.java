package net.ab.dal.nosql.hbase.ops;

import java.io.IOException;
import java.util.List;

import net.ab.dal.nosql.hbase.HBaseBean;
import net.ab.dal.nosql.hbase.InvalidHBaseBeanException;
import net.ab.dal.nosql.hbase.config.BaseConfiguration;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;

public class HbaseOpsImpl implements IHBaseOps {

	static IHBaseOps hbOps = null;
	public static IHBaseOps getInstance() {
		if (hbOps == null) {
			hbOps = new HbaseOpsImpl();
		}		
		return hbOps;
	}

	public void addBean(String tableName, HBaseBean data)
			throws HBaseOperationException {
		HTableInterface table = null;
		if (data != null) {
			try {
				table = BaseConfiguration.getTableFromPool(tableName);
				table.put(data.toPut());
			} catch (IOException e) {
				throw new HBaseOperationException(e);
			} catch (InvalidHBaseBeanException e) {
				throw new HBaseOperationException(e);
			} finally{
				if(null!=table)
					try {
						table.close();
					} catch (IOException e) {
						throw new HBaseOperationException(e);
					}
			}
		} else {
			throw new HBaseOperationException("Data cannot be null.");
		}

	}


	public <T extends HBaseBean> void addBeans(String tableName,
			List<T> dataCol) throws HBaseOperationException {
		if (dataCol != null) {
			HTableInterface table = null;
			try {
				table = BaseConfiguration.getTableFromPool(tableName);
				table.batch(HBaseBean.getPutList(dataCol));
			} catch (IOException e) {
				throw new HBaseOperationException(e);
			} catch (InvalidHBaseBeanException e) {
				throw new HBaseOperationException(e);
			} catch (InterruptedException e) {
				throw new HBaseOperationException(e);
			} finally{
				if(null!=table)
					try {
						table.close();
					} catch (IOException e) {
						throw new HBaseOperationException(e);
					}
			}
		} else {
			throw new HBaseOperationException("Data cannot be null.");
		}
	}


	public <T extends HBaseBean> List<T> scan(String tableName,
			Class<T> returnType, List<Filter> filters, boolean passAllFilters)
			throws HBaseOperationException {
		return scan(tableName, returnType, filters, passAllFilters, null, null);
	}


	public <T extends HBaseBean> List<T> scan(String tableName,
			Class<T> returnType, List<Filter> filters, boolean passAllFilters,
			byte[] startRow, byte[] stopRow) throws HBaseOperationException {
		Scan s = new Scan();
		
		if (startRow != null)
			s.setStartRow(startRow);
		if (stopRow != null)
			s.setStopRow(stopRow);

		if (filters != null) {
			if (passAllFilters)
				s.setFilter(new FilterList(FilterList.Operator.MUST_PASS_ALL,
						filters));
			else
				s.setFilter(new FilterList(FilterList.Operator.MUST_PASS_ONE,
						filters));
		}
		return scan(tableName, returnType, s);
	}

	public <T extends HBaseBean> List<T> scan(String tableName,
			Class<T> returnType, Scan scan) throws HBaseOperationException {
		Scan s = null;
		ResultScanner rs = null;
		HTableInterface table = null;
		try {
			s = new Scan(scan);
			table = BaseConfiguration.getTableFromPool(tableName);
			rs = table.getScanner(s);
			return HBaseBean.toObject(rs, returnType);
		} catch (IOException e) {
			throw new HBaseOperationException(e);
		} catch (InvalidHBaseBeanException e) {
			throw new HBaseOperationException(e);
		} finally {
			if (rs != null)
				rs.close();
			if(null!=table)
				try {
					table.close();
				} catch (IOException e) {
					throw new HBaseOperationException(e);
				}
		}
	}


	public <T extends HBaseBean> T get(String tableName, byte[] rowKey,
			Class<T> type) throws HBaseOperationException {
		if (null != rowKey) {
			HTableInterface table = null;
			try {
				table = BaseConfiguration
						.getTableFromPool(tableName);
				Result rs = table.get(new Get(rowKey));
				return HBaseBean.toObject(rs, type);
			} catch (IOException e) {
				throw new HBaseOperationException(e);
			} catch (InvalidHBaseBeanException e) {
				throw new HBaseOperationException(e);
			} finally {
				if(null!=table)
					try {
						table.close();
					} catch (IOException e) {
						throw new HBaseOperationException(e);
					}
			}
		}
		return null;

	}


	public void incrementCounter(String tableName, Increment inc)
			throws HBaseOperationException {
		HTableInterface table = null;
		try {
			table = BaseConfiguration
					.getTableFromPool(tableName);
			table.increment(inc);
		} catch (IOException e) {
			throw new HBaseOperationException(e);
		} finally {
			if(null!=table)
				try {
					table.close();
				} catch (IOException e) {
					throw new HBaseOperationException(e);
				}
		}
	}


	public void incrementCounter(String tableName, List<Increment> incs)
			throws HBaseOperationException {
		HTableInterface table = null;
		try {
			table = BaseConfiguration
					.getTableFromPool(tableName);
			for(Increment inc : incs )
				table.increment(inc);
		} catch (IOException e) {
			throw new HBaseOperationException(e);
		} finally {
			if(null!=table)
				try {
					table.close();
				} catch (IOException e) {
					throw new HBaseOperationException(e);
				}
		}
	}
	
}
