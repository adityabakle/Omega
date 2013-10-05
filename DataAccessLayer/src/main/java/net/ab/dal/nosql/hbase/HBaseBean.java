/**
 * 
 */
package net.ab.dal.nosql.hbase;



import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.ab.dal.nosql.hbase.annotations.HBaseColumn;
import net.ab.dal.nosql.hbase.annotations.HBaseRow;
import net.ab.dal.nosql.hbase.common.ObjectPrinter;
import net.ab.dal.nosql.hbase.config.HBaseProperties;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Base class for all beans which needs to be add or updated to HBase.<br/> 
 * Also provide implementation for converting object to and from HBase type.  
 * @author abakle
 * @version BBW 4.4.0
 * 
 */
public abstract class HBaseBean {
	public static final String _DF_Qualifir = "_DF_QUALIFIER_";
	public static final String _DF_ColumnFamily = "_DF_COLUMNFAMILY_";
	
	
	private String startRowKey;
	private Integer numRows = 25;
	
	public String getStartRowKey() {
		return startRowKey;
	}
	
	public String toString(){
		return ObjectPrinter.convertToString(this);
	}
	
	/**
	 * set rowKey of the last record in the current page. based on with the API will offset for next page.
	 * @param startRowKey rowKey of the last record in the current page.
	 */
	public void setStartRowKey(String startRowKey) {
		this.startRowKey = startRowKey;
	}

	public Integer getNumRows() {
		return numRows;
	}

	public void setNumRows(Integer numRows) {
		this.numRows = numRows;
	}
	
	public abstract boolean isRowKeyComplete();
	
	
	/**
	 * Generates String format of RowKey formed for object extending
	 * {@link net.rim.hbase.HBaseBean HBaseBean}
	 * 
	 * @return readable format of rowKey
	 * @throws InvalidHBaseBeanException with below codes<br/>
	 *  <b>2003</b> - {@link net.rim.hbase.annotation.HBaseRow HBaseRow} annotation can be used only on attribute of type Long/Integer/Date/String.<br/>
	 *  <b>2004</b> - 'rowKey' cannot be null. Please check values for attributes annotated with {@link net.rim.hbase.annotation.HBaseRow HBaseRow}.<br/>
	 *  <b>9000</b> - All other exception, Check cause for details.
	 */
	public String getRowKeyString() throws InvalidHBaseBeanException {
		return Bytes.toString(getRowKey());
	}

	/**
	 * Generates byte[] format of RowKey formed for object extending
	 * {@link net.rim.hbase.HBaseBean HBaseBean} Compound rowKey is formed based
	 * on the keyIndex specified for each attribute annotated by
	 * {@link net.rim.hbase.annotation.HBaseRow HBaseRow}
	 * 
	 * @return byte[] format of rowKey
	 * @throws InvalidHBaseBeanException with below codes<br/>
	 *  <b>2003</b> - {@link net.rim.hbase.annotation.HBaseRow HBaseRow} annotation can be used only on attribute of type Long/Integer/Date/String.<br/>
	 *  <b>2004</b> - 'rowKey' cannot be null. Please check values for attributes annotated with {@link net.rim.hbase.annotation.HBaseRow HBaseRow}.<br/>
	 *  <b>9000</b> - All other exception, Check cause for details.
	 */
	public byte[] getRowKey() throws InvalidHBaseBeanException {
		Map<Integer, Object> rowKeyMap = null;
		byte[] rowKey = null;
		
		try {
			rowKeyMap = HBaseUtil.getRowkeyMap(this);
		} catch (Exception e) {
			throw new InvalidHBaseBeanException("9000",HBaseProperties.getExceptionResource().getProperty("9000"),e);
		}
			

		// build rowKey
		if (null == rowKeyMap || rowKeyMap.size() == 0) { 
			return null;
		} else if (rowKeyMap.size() == 1) { // Single value row key
			Object value = rowKeyMap.get(0);
			if (value != null) {
				if (value instanceof Long) {
					rowKey = Bytes.toBytes((Long) value);
				} else if (value instanceof Integer) {
					rowKey = Bytes.toBytes((Integer) value);
				} else if (value instanceof String) {
					rowKey = Bytes.toBytes((String) value);
				} else if (value instanceof Date) {
					rowKey = Bytes.toBytes(((Date) value).getTime());
				} else {
					//HBaseRow can be annoated only on attribute of type Long/Integer/Date/String.
					throw new InvalidHBaseBeanException("2003",
							HBaseProperties.getExceptionResource().getProperty("2003").toString());
				}
			} else {
				//'rowKey' cannot be null. Please check values for attributes annotated with HBaseRow.
				throw new InvalidHBaseBeanException("2004",
						HBaseProperties.getExceptionResource().getProperty("2004"));
			}

		} else {
			String strRowkey = null;
			for (int i = 0; i < rowKeyMap.size(); i++) {
				Object value = rowKeyMap.get(i);
				if (value != null) {
					if (value instanceof Long || value instanceof Integer
							|| value instanceof String) {
						strRowkey = (strRowkey == null ? String.valueOf(value)
								: strRowkey + "-" + String.valueOf(value));
					} else if (value instanceof Date) {
						strRowkey = (strRowkey == null ? String
								.valueOf(((Date) value).getTime()) : strRowkey
								+ "-" + String.valueOf(((Date) value).getTime()));
					} else {
						//HBaseRow can be annoated only on attribute of type Long/Integer/Date/String.
						throw new InvalidHBaseBeanException("2003",HBaseProperties.getExceptionResource().getProperty("2003")+" Check Field :> index " +i);
					}
				}
			}
			// System.out.println(strRowkey);
			rowKey = Bytes.toBytes(strRowkey);
		}
		return rowKey;
	}

	/**
	 * Converts an object extending {@link net.rim.hbase.HBaseBean HBaseBean} to
	 * a Put Object for inserting data to HBase.
	 * 
	 * @return {@link org.apache.hadoop.hbase.client.Put Put} object
	 *         representation of the HBaseBean object
	 * @throws InvalidHBaseBeanException with bellow codes<br/>
	 *  <b>2001</b> - Class is missing '{@link net.rim.hbase.annotation.HBaseRow HBaseRow}' annotation. At-least one element must be annotated as {@link net.rim.hbase.annotation.HBaseRow HBaseRow}.<br/>
	 *  <b>2002</b> - {@link net.rim.hbase.annotation.HBaseColumn HBaseColumn} annotation cannot have empty 'cf' (Column family).<br/>
	 *  <b>9000</b> - All other exception, Check cause for details.
	 */
	public Put toPut() throws InvalidHBaseBeanException {
		Put p = null;

		if (this.getClass().isAnnotationPresent(HBaseRow.class)) {
			throw new InvalidHBaseBeanException("2001",HBaseProperties.getExceptionResource().getProperty("2001"));
		} else {
			p = new Put(this.getRowKey());
			String pojoColumnFamily = HBaseUtil.getColumnfamilyForPojo(this);
			for (Field f : this.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				HBaseColumn annCol = f.getAnnotation(HBaseColumn.class);
				if (annCol != null) {
					if (annCol.columnFamily() == null  || (_DF_ColumnFamily.equals(annCol.columnFamily()) && _DF_ColumnFamily.equals(pojoColumnFamily))) {
						throw new InvalidHBaseBeanException("2002",HBaseProperties.getExceptionResource().getProperty("2002")+ f.getName());
					}
					try {
						Object value = f.get(this);
						String qualifier = (_DF_Qualifir.equals(annCol
								.qualifier()) ? f.getName() : annCol
								.qualifier());
						String columnFamily = (_DF_ColumnFamily.equals(annCol.columnFamily()) ?pojoColumnFamily : annCol.columnFamily());
						if (value != null) {
							p.add(Bytes.toBytes(columnFamily),
									Bytes.toBytes(qualifier),
									HBaseUtil.getBytes(value));
						}
					} catch (Exception e) {
						throw new InvalidHBaseBeanException("9000",HBaseProperties.getExceptionResource().getProperty("9000"),e);
					}
				}
			}
		}

		return p;
	}

	/**
	 * Converts an list of objects extending {@link net.rim.hbase.HBaseBean
	 * HBaseBean} to a List of Put Object for inserting data to HBase.
	 * 
	 * @param beans
	 *            - List of beans extending {@link net.rim.hbase.HBaseBean
	 *            HBaseBean} to be converted.
	 * @return List of {@link org.apache.hadoop.hbase.client.Put Put} object
	 * @throws InvalidHBaseBeanException with bellow codes<br/>
	 *  <b>2001</b> - Class is missing '{@link net.rim.hbase.annotation.HBaseRow HBaseRow}' annotation. At-least one element must be annotated as {@link net.rim.hbase.annotation.HBaseRow HBaseRow}.<br/>
	 *  <b>2002</b> - {@link net.rim.hbase.annotation.HBaseColumn HBaseColumn} annotation cannot have empty 'cf' (Column family).<br/>
	 *  <b>9000</b> - All other exception, Check cause for details.
	 */
	public static <T extends HBaseBean> List<Put> getPutList(List<T> beans)
			throws InvalidHBaseBeanException {
		List<Put> pl = null;
		if (beans != null && beans.size() > 0) {
			pl = new ArrayList<Put>();
			for (Object obj : beans) {
				pl.add(((HBaseBean) obj).toPut());
			}
		}
		return pl;
	}

	/**
	 * Converts an HBase result to object of type T
	 * 
	 * @param r
	 *            of type {@link org.apache.hadoop.hbase.client.Result Result}
	 * @param type
	 *            Class type T which extends {@link net.rim.hbase.HBaseBean
	 *            HBaseBean} in which the result will be converted.
	 * @return object of type T extending {@link net.rim.hbase.HBaseBean
	 *         HBaseBean}
	 * @throws InvalidHBaseBeanException with below codes<br/>
	 *  <b>2000</b> - Error creating instance of a class of given type. Class should be of type extend HBaseBeans.<br/>
	 *  <b>2001</b> - Class is missing 'HBaseRow' annotation. At-least one element must be annotated as HBaseRow.<br/>
	 *  <b>2002</b> - HBaseColumn annotation cannot have empty 'cf' (Column family).<br/>
	 *  <b>9000</b> - All other exception, Check cause for details.<br/>
	 */
	public static <T extends HBaseBean> T toObject(Result r, Class<T> type)
			throws InvalidHBaseBeanException {
		T obj = null;
		
		if(null==r || r.isEmpty()) return null;
		
		Map<Integer, Field> rowKeyMap = new HashMap<Integer, Field>();
		try {
			obj = type.newInstance();
		} catch (InstantiationException e) {
			throw new InvalidHBaseBeanException("2000", HBaseProperties.getExceptionResource().getProperty("2000"), e);
		} catch (IllegalAccessException e) {
			throw new InvalidHBaseBeanException("2000", HBaseProperties.getExceptionResource().getProperty("2000"), e);
		}

		if (obj.getClass().isAnnotationPresent(HBaseRow.class)) {
			throw new InvalidHBaseBeanException("2001", HBaseProperties.getExceptionResource().getProperty("2001"));
		} else {
			// Finding the Row elements;
			for (Field f : obj.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				HBaseRow annRow = f.getAnnotation(HBaseRow.class);
				if (annRow != null) {
					rowKeyMap.put(annRow.keyIndex(), f);
				}
			}

			try {
				if (rowKeyMap.size() == 1) {
					Field f = (Field) rowKeyMap.values().toArray()[0];
					f.setAccessible(true);
					HBaseRow annRow = f.getAnnotation(HBaseRow.class);

					if (annRow.type() == Long.class) {
						f.set(obj, Bytes.toLong(r.getRow()));
					} else if (annRow.type() == Date.class) {
						f.set(obj, new Date(Bytes.toLong(r.getRow())));
					} else if (annRow.type() == Integer.class) {
						f.set(obj, Bytes.toInt(r.getRow()));
					} else if (annRow.type() == String.class) {
						f.set(obj, Bytes.toString(r.getRow()));
					}

				} else if (rowKeyMap.size() > 1) {
					String rowKey = Bytes.toString(r.getRow());
					String[] keys = rowKey.split("-");
					for (int i = 0; i < keys.length; i++) {
						Field f = rowKeyMap.get(i);
						f.setAccessible(true);
						Class<?> fieldType = f.getType();

						if (fieldType == Long.class) {
							f.set(obj, Long.parseLong(keys[i]));
						} else if (fieldType == Date.class) {
							f.set(obj, new Date(Long.parseLong(keys[i])));
						} else if (fieldType == Integer.class) {
							f.set(obj, Integer.parseInt(keys[i]));
						} else if (fieldType == String.class) {
							f.set(obj, keys[i]);
						}
					}
				}
			}  catch (IllegalAccessException e) {
				throw new InvalidHBaseBeanException("9000",HBaseProperties.getExceptionResource().getProperty("9000"), e);
			}

			byte[] value = null;
			String pojoColumnFamily = HBaseUtil.getColumnfamilyForPojo(obj);
			for (Field f : obj.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				HBaseColumn annCol = f.getAnnotation(HBaseColumn.class);
				if (annCol != null) {
					if (annCol.columnFamily() == null || (_DF_ColumnFamily.equals(annCol.columnFamily()) && _DF_ColumnFamily.equals(pojoColumnFamily))) {
						throw new InvalidHBaseBeanException("2002",HBaseProperties.getExceptionResource().getProperty("2002") + f.getName());
					}
					try {
						String qualifier = (_DF_Qualifir.equals(annCol
								.qualifier()) ? f.getName() : annCol
								.qualifier());
						String columnFamily = (_DF_ColumnFamily.equals(annCol.columnFamily()) ? pojoColumnFamily : annCol.columnFamily());
						Class<?> fieldType = f.getType();
						value = r.getValue(Bytes.toBytes(columnFamily),
								Bytes.toBytes(qualifier));
						if (value == null)
							continue;
						f.set(obj, HBaseUtil.getValue(value, fieldType));

					} catch (Exception e) {
						throw new InvalidHBaseBeanException("9000",HBaseProperties.getExceptionResource().getProperty("9000"),e);
					}
				}
			}
		}
		return obj;
	}

	/**
	 * Converts an HBase resultset to list of objects of type T
	 * 
	 * @param rs
	 *            of type {@link org.apache.hadoop.hbase.client.ResultScanner
	 *            ResultScanner}
	 * @param type
	 *            Class type T which extends {@link net.rim.hbase.HBaseBean
	 *            HBaseBean} in which the result will be converted.
	 * @return list of objects of type T extending
	 *         {@link net.rim.hbase.HBaseBean HBaseBean}
	 * @throws InvalidHBaseBeanException with below codes<br/>
	 *  <b>2000</b> - Error creating instance of a class of given type. Class should be of type extend HBaseBeans.<br/>
	 *  <b>2001</b> - Class is missing 'HBaseRow' annotation. At-least one element must be annotated as HBaseRow.<br/>
	 *  <b>2002</b> - HBaseColumn annotation cannot have empty 'cf' (Column family).<br/>
	 *  <b>9000</b> - All other exception, Check cause for details.
	 */
	public static <T extends HBaseBean> List<T> toObject(ResultScanner rs,
			Class<T> type) throws InvalidHBaseBeanException {
		List<T> rtList = null;
		try {
			if (rs != null) {
				rtList = new ArrayList<T>();
				Iterator<Result> it = rs.iterator();
				while (it.hasNext()) {
					Result r = it.next();
					rtList.add(HBaseBean.toObject(r, type));
				}
			}
		} catch (InvalidHBaseBeanException ie) {
			throw ie;
		} finally {
			if (rs != null)
				rs.close();
			rs = null;
		}
		return rtList;
	}
}
