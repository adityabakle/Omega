package net.ab.dal.nosql.hbase.common;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

public class ObjectPrinter {
private static String tab = "";
    
    /**
     * This method takes a collection of String and converts them
     * into a format that the stored procedure expects
     *
     * @param Collection Collection of String
     * @return		String
     *
     */
    public static String stringCollectionToString(Collection<?> c)
    {
        if ( c != null ) {
            StringBuffer sb = new StringBuffer();
            Iterator<?> it = c.iterator();
            int i = 0;
            while (it.hasNext()) {
                String myObject = (String)it.next();
                if ( i++ > 0 ) sb.append("|"); // No "|" after last append
                sb.append(myObject);
            }
            return sb.toString();
        } else return null;
    }

    /**
     * This method takes a collection of Integer and converts them
     * into a format that the stored procedure expects
     *
     * @param Collection Collection of Integer
     * @return          String
     *
     */
    public static String integerCollectionToString(Collection<?> c)
    {
        if ( c != null ) {
            StringBuffer sb = new StringBuffer();
            Iterator<?> it = c.iterator();
            int i = 0;
            while (it.hasNext()) {
                Integer myObject = (Integer)it.next();
                if ( i++ > 0 ) sb.append("|"); // No "|" after last append
                sb.append(myObject);
            }
            return sb.toString();
        } else return null;
    }

    /**
    * This method converts the given object into readable String format Similar to JSON 
    * @param obj object to be presented as String
    * @return String with complete Object structure and value.
    */
   public static String convertToString(Object obj)
   {
      StringBuffer sbf = new StringBuffer()
         .append(obj.getClass().getSimpleName()).append(" {\n");
      sbf.append(convert(obj.getClass(), obj)).append("}");
      return sbf.toString();
   }
   
   private static String convert(Class<?> cls, Object obj)
   {
      StringBuffer sbf = new StringBuffer();
      if(cls.getSuperclass() != Object.class)
      {
         sbf.append(convert(cls.getSuperclass(), obj));
      }
      Field[] arrFields = cls.getDeclaredFields();
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
      sbf.append(tab+"\n");
      if(tab.length()>0) {
         tab = tab.substring(0,tab.length()-1);
      }
      sbf.append(tab);
      return sbf.toString();
   }
}
