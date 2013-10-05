package net.ab.dal.nosql.hbase;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.ab.dal.nosql.hbase.annotations.HBaseColumn;
import net.ab.dal.nosql.hbase.annotations.HBaseRow;
import net.ab.dal.nosql.hbase.annotations.HBaseTable;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author abakle
 * @version BBW 4.4.0
 *
 */
public class HBaseUtil {
	
	/**
	 * Extracts table name from a java hbase bean.
	 * @param bean any java object that extends {@link net.rim.hbase.HBaseBean HBaseBean}
	 * @return hbase table name annotated to the class with {@link net.rim.hbase.annotations.HBaseTable HBaseTable}
	 */
	public static String getTableName(HBaseBean bean){
		String tableName = null;
		HBaseTable hbTab =  bean.getClass().getAnnotation(HBaseTable.class);
		if(null != hbTab)
			tableName = hbTab.tableName();
		return tableName;
	}
	
	public static <T extends HBaseBean> RowFilter getRowFilter(T bean) throws Exception {
		String regexRowKey = getRegexRowkey(bean);
		RowFilter rf = new RowFilter(CompareOp.EQUAL,new RegexStringComparator(regexRowKey));
		return rf;
	}
	
	public static <T extends HBaseBean> PrefixFilter getPrefixFilter(T bean) throws Exception {
		System.out.println("Row Key for Prefix : "+bean.getRowKeyString());
		PrefixFilter pf = new PrefixFilter(bean.getRowKey());
		return pf;
	}
	
	public static <T extends HBaseBean> FilterList getColumnFilter(T bean) throws Exception {
		Map<String, KeyValue> colMap = getColumnMap(bean);
		FilterList fl = null;
		if(colMap!=null && colMap.size()>0){
			fl = new FilterList();
			for(Entry<String, KeyValue> e :colMap.entrySet()){
				SingleColumnValueFilter scvf = new SingleColumnValueFilter(e.getValue().getFamily(),e.getValue().getQualifier(),CompareOp.EQUAL,e.getValue().getValue());
				scvf.setFilterIfMissing(true);
				scvf.setLatestVersionOnly(true);
				//ValueFilter vF = new ValueFilter(CompareOp.EQUAL,new BinaryComparator(HBaseUtil.getBytes(e.getValue())));
				fl.addFilter(scvf);
			}
		}
		return fl;
	}

	public static <T extends HBaseBean> String getRegexRowkey(T bean) throws Exception {
		StringBuffer sb = new StringBuffer();
		boolean bStartsWith = false;
		Map<Integer, Object> rowKey = getRowkeyMap(bean);
		if(rowKey!=null && rowKey.size()>0){
		 for (int i=0;i<rowKey.size();i++){
			 Object val = rowKey.get(i);
			 if(val!=null){
				 if(i==0) 
					 bStartsWith = true;
				 else 
					 sb.append("-");
				 
				 sb.append(val.toString());
			 } else {
				 if(i>0) sb.append("-");
				 sb.append(".*.");
			 }
		 }
		}
		
		if(bStartsWith)
			sb.insert(0, '^');
		
		return sb.toString();
	}
	
	public static <T extends HBaseBean> Map<Integer, Object> getRowkeyMap(T bean) throws Exception{
		Map<Integer, Object> rowKey = new HashMap<Integer, Object>(6);
		
		for (Field f : bean.getClass().getDeclaredFields()){
			if(f.isAnnotationPresent(HBaseRow.class)){
				f.setAccessible(true);
				rowKey.put(f.getAnnotation(HBaseRow.class).keyIndex(), f.get(bean));
			}
		}
		
		return rowKey;
	}
	
	public static <T extends HBaseBean> Map<String, KeyValue> getColumnMap(T bean) throws Exception{
		Map<String, KeyValue> colMap = new HashMap<String, KeyValue>(6);
		
		for (Field f : bean.getClass().getDeclaredFields()){
			if(f.isAnnotationPresent(HBaseColumn.class)){
				f.setAccessible(true);
				Object value = f.get(bean);
				if(null!=value){
					String qualifier = (f.getAnnotation(HBaseColumn.class)).qualifier().equals(HBaseBean._DF_Qualifir)?f.getName():(f.getAnnotation(HBaseColumn.class)).qualifier();
					String colFamily = (HBaseBean._DF_ColumnFamily.equals(f.getAnnotation(HBaseColumn.class).columnFamily()))?getColumnfamilyForPojo(bean):f.getAnnotation(HBaseColumn.class).columnFamily();
					KeyValue kv = new KeyValue(bean.getRowKey(),Bytes.toBytes(colFamily),Bytes.toBytes(qualifier),getBytes(value));
					colMap.put(qualifier, kv);
				}
			}
		}
		
		return colMap;
	}
	
	public static <T extends HBaseBean> String getColumnfamilyForPojo(T bean) {
		String columnFamily = null;
		HBaseTable hbTab =  bean.getClass().getAnnotation(HBaseTable.class);
		if(null != hbTab)
			columnFamily = hbTab.columnFamily();
		return columnFamily;
	}
	
	public static <T extends HBaseBean> byte[] getColumnfamily(T bean) {
		byte [] columnFamily = null;
		HBaseTable hbTab =  bean.getClass().getAnnotation(HBaseTable.class);
		if(null != hbTab)
			columnFamily = getBytes(hbTab.columnFamily());
		return columnFamily;
	}

	/**
	 * @param value
	 * @return
	 */
	public static byte[] getBytes(Object value){
		byte[] bval = null;
		
		if (value instanceof Integer) {
			bval = Bytes.toBytes((Integer) value);
		} else if (value instanceof Long) {
			bval = Bytes.toBytes((Long) value);
		} else if (value instanceof String) {
			bval = Bytes.toBytes((String) value);
		} else if (value instanceof Double) {
			bval = Bytes.toBytes((Double) value);
		} else if (value instanceof BigDecimal) {
			bval = Bytes.toBytes((BigDecimal) value);
		} else if (value instanceof Date) {
			value = ((Date) value).getTime();
			bval = Bytes.toBytes((Long) value);
		} else if (value instanceof Boolean) {
			bval = Bytes.toBytes((Boolean) value);
		} else if (value instanceof Float) {
			bval = Bytes.toBytes((Float) value);
		} else if (value instanceof Short) {
			bval = Bytes.toBytes((Short) value);
		} else if (value instanceof ByteBuffer) {
			bval = Bytes.toBytes((ByteBuffer) value);
		} else {
			bval = Bytes.toBytes(value.toString());
		}
		
		return bval;
	}
	
	public static Object getValue(byte[] value, Class<?> fieldType){
		Object rvalue = null;
		if (fieldType == Integer.class) {
			rvalue = Bytes.toInt(value);
		} else if (fieldType == (Long.class)) {
			rvalue = Bytes.toLong(value);
		} else if (fieldType == String.class) {
			rvalue = Bytes.toString(value);
		} else if (fieldType == Double.class) {
			rvalue = Bytes.toDouble(value);
		} else if (fieldType == BigDecimal.class) {
			rvalue = Bytes.toBigDecimal(value);
		} else if (fieldType == Date.class) {
			Date d = new Date(Bytes.toLong(value));
			rvalue = d;
		} else if (fieldType == Boolean.class) {
			rvalue = Bytes.toBoolean(value);
		} else if (fieldType == Float.class) {
			rvalue = Bytes.toFloat(value);
		} else if (fieldType == Short.class) {
			rvalue = Bytes.toShort(value);
		} else if (fieldType == ByteBuffer.class) {
			rvalue = ByteBuffer.wrap(value);
		} else {
			rvalue = fieldType.cast(value);
		}
		return rvalue;
	}
}
