package org.ab.util;

/**
 * Class responsible for printing any Java PoJo to readable format.
 * Useful for Logging purpose.
 * @author abakle
 * 
 * @version 1.0
 * */

import java.lang.reflect.Field;
import java.util.Arrays;

public class ObjectConverter {
	private static String tab = "";

	/**
	 * This method converts the given object into readable String format Similar
	 * to JSON
	 * 
	 * @param obj
	 *            object to be presented as String
	 * @return String with complete Object structure and value.
	 */
	public static String convertToString(Object obj) {
		StringBuffer sbf = new StringBuffer().append(
				obj.getClass().getSimpleName()).append(" {\n");
		sbf.append(convert(obj.getClass(), obj)).append("}");
		return sbf.toString();
	}

	private static String convert(Class<?> cls, Object obj) {
		StringBuffer sbf = new StringBuffer();
		if (cls.getSuperclass() != Object.class) {
			sbf.append(convert(cls.getSuperclass(), obj));
		}
		Field[] arrFields = cls.getDeclaredFields();
		tab = tab + "\t";
		for (Field objFld : arrFields) {
			objFld.setAccessible(true);
			sbf.append(tab + "[").append(objFld.getName()).append(" : ");
			try {
				if (objFld.getType().isArray()) {
					Object[] arrObj = (Object[]) objFld.get(obj);
					sbf.append(Arrays.asList(arrObj));
				} else
					sbf.append(objFld.get(obj));

			} catch (IllegalArgumentException e) {
				sbf.append(e.getClass().getName());
			} catch (IllegalAccessException e) {
				sbf.append(e.getClass().getName());
			} catch (Exception e) {
				sbf.append(e.getMessage());
			}
			sbf.append("]\n");
		}
		sbf.append(tab + "\n");
		if (tab.length() > 2) {
			tab = tab.substring(0, tab.length() - 1);
		}
		sbf.append(tab);
		return sbf.toString();
	}
}
