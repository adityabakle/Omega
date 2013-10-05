package net.ab.dal.nosql.hbase.downloads;

import java.util.List;

import net.ab.dal.nosql.appworld.downloads.HBDigitalLockerDAO;
import net.ab.dal.nosql.appworld.downloads.HBDigitalLockerParam;
import net.ab.dal.nosql.appworld.downloads.HBDownloadImpl;
import net.ab.dal.nosql.hbase.HBaseUtil;
import net.ab.dal.nosql.hbase.InvalidHBaseBeanException;
import net.ab.dal.nosql.hbase.common.AWHBaseException;

public class FetchLockerRecords {

	public static void echo(Object obj){
		System.out.println(obj.toString());
	}
	
	/**
	 * @param args
	 * @throws AWHBaseException 
	 * @throws InvalidHBaseBeanException 
	 */
	public static void main(String[] args) {
		HBDigitalLockerParam param = new HBDigitalLockerParam();
		param.setCustomerId(94948623l);
//		param.setContentId(57650l);
		
//		param.setNumRows(10);
		
		HBDownloadImpl dao = new HBDownloadImpl();
		try {
			echo(param.getRowKeyString());
			echo("Look up for : " + HBaseUtil.getRegexRowkey(param));
		} catch (InvalidHBaseBeanException e1) {
			echo(e1.getErrorCode() +" : "+e1.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			List<HBDigitalLockerDAO> result = dao.getDigitalLocker(param);
		
			if (result == null || result.size() == 0) {
				echo("No records");
			} else { 
				echo(result);
			}
		} catch(AWHBaseException e){
			echo(e.getErrorCode() +" : "+e.getMessage());
		}

	}

}
