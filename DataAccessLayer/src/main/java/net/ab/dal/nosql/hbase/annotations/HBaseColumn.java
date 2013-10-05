package net.ab.dal.nosql.hbase.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.ab.dal.nosql.hbase.HBaseBean;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HBaseColumn {
	/**
	 * Column family Name
	 */
	String columnFamily() default HBaseBean._DF_ColumnFamily;
	/**
	 * Qualifier name within a Column Family if not specified then the field name is used as qualifier. 
	 */
	String qualifier() default HBaseBean._DF_Qualifir;
	
	/**
	 * Set what type of Attribute is this. Default is set to <code>Long.class</code>
	 */
	Class<?> type() default Long.class;
	
	//boolean counter() default false;
	
}
