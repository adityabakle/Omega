package net.ab.dal.nosql.hbase.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HBaseRow {
	/**
	 * Specify the type of Row key. Default is set as Long.class
	 * <br> Type can be either Long.class or String.class.
	 */
	Class<?> type() default Long.class;
	/**
	 * Index of the attribute in a compound row key. default value is 0. 
	 * <br>If multiple attribute is set with default the most recent will be overwritten.  
	 * <br>e.g. &lt;key0&gt;-&lt;key1&gt;-&lt;key2&gt;
	 * <br>Make sure the value of the attributes ha no '-'. 
	 * 
	 */
	
	int keyIndex() default 0;
	
	/**
	 * Seperator for compound key. default is "-"
	 * */
	//String seperator() default "-";
}
