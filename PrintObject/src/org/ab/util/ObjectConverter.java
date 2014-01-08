package org.ab.util;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Class responsible for printing any Java PoJo to readable format. Useful for Logging purpose. To implement this just
 * overload the <code>toString()</code> method in any PoJo as below... <br/>
 * <br/>
 * <code>
 * <b>public String</b> toString(){<br/>
 *  &nbsp;&nbsp; <b>return</b> ObjectConverter.convertToString(<b>this</b>);<br/>
 * }
 * </code><br/>
 * 
 * @author abakle
 * */
public class ObjectConverter {
    public static final String NEW_LINE = "\r\n";
    public static final String LINE_INDENTION = "    ";
    public static final String FIELD_SEPERATOR = ",";
    public static final String CLASS_START = "{";
    public static final String CLASS_END = "}";
    public static final String NAME_VALUE_SEPERATOR = ":";

    private static String tab = "";

    /**
     * This method converts the given object into readable String format Similar to JSON
     * 
     * @param obj
     *            object to be presented as String
     * @return String with complete Object structure and value.
     */
    public static String convertToString(Object obj) {
        StringBuffer sbf = new StringBuffer().append(obj.getClass().getSimpleName()).append(NAME_VALUE_SEPERATOR)
                .append(CLASS_START).append(NEW_LINE);
        sbf.append(convert(obj.getClass(), obj)).append(CLASS_END);
        return sbf.toString();
    }

    private static String convert(Class<?> cls, Object obj) {
        StringBuffer sbf = new StringBuffer();
        if (cls.getSuperclass() != Object.class) {
            sbf.append(convert(cls.getSuperclass(), obj));
        }
        Field[] arrFields = cls.getDeclaredFields();
        tab = tab + LINE_INDENTION;
        for (Field objFld : arrFields) {
            objFld.setAccessible(true);
            sbf.append(tab).append(objFld.getName()).append(NAME_VALUE_SEPERATOR);
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
            sbf.append(FIELD_SEPERATOR).append(NEW_LINE);
        }
        if (tab.length() >= LINE_INDENTION.length()) {
            tab = tab.substring(0, tab.length() - LINE_INDENTION.length());
        }
        sbf.append(tab);
        return sbf.toString();
    }
}
