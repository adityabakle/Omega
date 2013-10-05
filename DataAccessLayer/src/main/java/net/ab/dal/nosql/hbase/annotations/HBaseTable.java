package net.ab.dal.nosql.hbase.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.ab.dal.nosql.hbase.HBaseBean;

/**
 * @author abakle
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HBaseTable {
	
	/**
	 * Class level Annotation mapping POJO to HBase table.
	 * HBase Table Name is case sensitive .  
	 */
	
	String tableName();
	
	/**
	 * Optional attribute for table to specify column family the pojo belongs. 
	 * Applicable if all attributes in the pojo belong to same Column family. 
	 * @return
	 */
	String columnFamily() default HBaseBean._DF_ColumnFamily;
}
