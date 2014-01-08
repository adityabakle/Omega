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
		tst.map.put("ONE", new TestDTO2());
		//tst.map.put("TWO", 2);
		tst.names.add(new TestDTO2());
		
		//System.out.println("Raw Object value :"+tst.toString());
		
		System.out.println(tst);

	}
}

class TestDTO{
	String name = "Omega";
	long l = 314;
	double d = 3.14;
	Long[] lst = {1l,2l,3l};
	List<TestDTO2> names = new ArrayList<TestDTO2>(4);
	Map<String, TestDTO2> map = new HashMap<String, TestDTO2>();
	
	public String toString(){
		return ObjectConverter.convertToString(this);
	}
}
class TestDTO2{
    String name2 = "Omega 2";
    String value = "XXX";
    public String toString(){
        return ObjectConverter.convertToString(this);
    }
}
