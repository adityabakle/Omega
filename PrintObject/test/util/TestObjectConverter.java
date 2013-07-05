package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ab.util.ObjectConverter;

public class TestObjectConverter {

	/**
	 * Test for a ObjectConverter Class
	 * @param args
	 */
	public static void main(String[] args) {
		TestDTO tst = new TestDTO();
		tst.map.put("ONE", 1);
		tst.map.put("TWO", 2);
		
		System.out.println("Raw Object value :"+tst.toString());
		
		System.out.println("Readable Object value :"+tst.toString(true));

	}
}

class TestDTO{
	String name = "Omega";
	long l = 314;
	double d = 3.14;
	Long[] lst = {1l,2l,3l};
	List<String> names = new ArrayList<String>(4);
	Map<String, Integer> map = new HashMap<String, Integer>();
	
	public String toString(boolean readable){
		return ObjectConverter.convertToString(this);
	}
}
