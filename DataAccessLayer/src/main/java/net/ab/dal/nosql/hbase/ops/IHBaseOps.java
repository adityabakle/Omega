package net.ab.dal.nosql.hbase.ops;

import java.util.List;

import net.ab.dal.nosql.hbase.HBaseBean;

import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;

public interface IHBaseOps {
	public void addBean(String tableName, HBaseBean data)
			throws HBaseOperationException;

	public <T extends HBaseBean> void addBeans(String tableName, List<T> dataCol)
			throws HBaseOperationException;
	
	public <T extends HBaseBean> T get(String tableName, byte[] rowKey, Class<T> type)
			throws HBaseOperationException;

	public <T extends HBaseBean> List<T> scan(String tableName,
			Class<T> returnType, List<Filter> filters, boolean passAllFilters)
			throws HBaseOperationException;

	public <T extends HBaseBean> List<T> scan(String tableName,
			Class<T> returnType, List<Filter> filters, boolean passAllFilters,
			byte[] startRow, byte[] stopRow) throws HBaseOperationException;

	public <T extends HBaseBean> List<T> scan(String tableName,
			Class<T> returnType, Scan scan) throws HBaseOperationException;
	
	public void incrementCounter(String tableName, Increment inc) 
			throws HBaseOperationException;
	
	public void incrementCounter(String tableName, List<Increment> incs) 
			throws HBaseOperationException;
}
