package com.cellmania.cmreports.common;

import java.lang.reflect.Field;
import java.util.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * This class provides method to represent any object in a 
 * readable String format similar to JSON. Also includes methods to convert Object to XML.
 * 
 *  
 * @author Aditya Bakle
 * @since JDK 1.5 
 *
 */
public class ObjectConvertor {
	private static String tab = "";
	/**
	 * This method converts the given object into readable String format Similar to JSON 
	 * @param obj object to be presented as String
	 * @return String with complete Object structure and value.
	 */
	public static String convertToString(Object obj) {
		try {
			StringBuffer sbf = new StringBuffer()
			.append(obj.getClass().getSimpleName())//.getName())
			.append(" {\n");
			Field[] arrFields = obj.getClass().getDeclaredFields();
			tab = tab+"\t";
			for(Field objFld : arrFields)
			{
				objFld.setAccessible(true);
				sbf.append(tab+"[")
				.append(objFld.getName())
				.append(" : ");
					try {
						sbf.append(objFld.get(obj));
					} catch (IllegalArgumentException e) {
						sbf.append(e.getClass().getName());
					} catch (IllegalAccessException e) {
						sbf.append(e.getClass().getName());
					} catch (Exception e){
						sbf.append(e.getMessage());
					}
				 sbf.append("]\n"); 
			}
			sbf.append(tab+"}\n");
			if(tab.length()>0) {
				tab = tab.substring(0,tab.length()-1);
			}
			sbf.append(tab);
			return sbf.toString();
			} catch (Exception E) {
				System.out.println("Error Printing Object : "+obj.getClass().getSimpleName()+" : " + E);
				E.printStackTrace();
				return null;
			}
	}
	
	/**
	 * This method converts the given object into XML String. 
	 * Attribute are used for tag name containing 
	 * value. Each tag has attribute representing data type. 
	 * <![CDATA[]]> is used for elements with data type String or date.
	 * @param obj object to be presented as XML data
	 * @return XML String presenting object in tree structure 
	 */
	public static String convertToXML(Object obj) {
		return convertToXML(obj,false);
	}
	
	/**
	 * This method is called by {@link convertToXML()}
	 * @param obj object to be presented as XML data
	 * @param bAttribute this value is true an attribute in a class is an object with its own attribute. 
	 * and false if the object is a parent object. This attribute is computed by the code so no need to set.
	 * @return XML String presenting object in tree structure
	 */
	private static String convertToXML(Object obj,boolean bAttribute) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("<")
		.append(obj.getClass().getSimpleName());//.getName())
		if(bAttribute){
			sbf.append(" attribute=\"")
			.append(bAttribute)
			.append("\" dataType=\"")
			.append(obj.getClass().getSimpleName())
			.append("\"");
		}
		sbf.append(">\n");
		
		Field[] arrFields = obj.getClass().getDeclaredFields();
		
		for(Field objFld : arrFields)
		{
			
			objFld.setAccessible(true);
			sbf.append("<")
			.append(objFld.getName())
			.append(" dataType=\"")
			.append(objFld.getType().getSimpleName())
			.append("\">");
			
			if("String".equals(objFld.getType().getSimpleName())
					||"Date".equals(objFld.getType().getSimpleName())) {
				sbf.append("<![CDATA[");
			}
			
			try {
				sbf.append(objFld.get(obj));
			} catch (IllegalArgumentException e) {
				sbf.append(e.getClass().getName());
			} catch (IllegalAccessException e) {
				sbf.append(e.getClass().getName());
			} catch (Exception e){
				sbf.append(e.getMessage());
			}
			if("String".equals(objFld.getType().getSimpleName())
					||"Date".equals(objFld.getType().getSimpleName())) {
				sbf.append("]]>");
			}
			 
			 sbf.append("</")
			 .append(objFld.getName())
			 .append(">\n");
		}
		sbf.append("</")
		.append(obj.getClass().getSimpleName())//.getName())
		.append(">");
	
		return sbf.toString();
		
	}
	
	/**
	 * This method returns date in format specified in the format parameter.
	 * @param obj is a java.sql.Date object
	 * @param format is the format in which date object to be converted.
	 * @return formatted String of date.
	 */
	public static String simpleDate(Date obj,String format){
		if(obj==null){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(obj).toString();
	}
	
	/**
	 * This method returns date object of the passed String with the given format.
	 * @param strDate is a string representation of a date
	 * @param format is the format in which string represent the date.
	 * @return  date object.
	 * @throws ParseException 
	 */
	public static Date getDate(String obj,String format) throws ParseException{
		if(obj==null){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(obj);
	}
	
	/**
	 * This method returns date in format specified in the format parameter.
	 * @param obj is a java.sql.Date object
	 * @param format is the format in which date object to be converted.
	 * @return formatted String of date.
	 */
	public static String simpleDate(java.sql.Date obj,String format){
		if(obj==null){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(obj).toString();
	}
	
	/**
	 * This method returns double in format <code>##,##0.00</code>. can be used for displaying Amount.
	 * @param obj is of type double
	 * @return formatted String of double.
	 */
	public static String displayAmount(double amt){
		DecimalFormat df = new DecimalFormat("###,###,##0.00");
		return df.format(amt);
	}
	
	/**
	 * This method returns double in format passed. can be used for displaying Amount.
	 * @param obj is of type double
	 * @param format is of type String representing the format pattern.
	 * @return formatted String of double.
	 */
	public static String displayAmount(double amt, String format){
		DecimalFormat df = new DecimalFormat(format);
		return df.format(amt);
	}
}
