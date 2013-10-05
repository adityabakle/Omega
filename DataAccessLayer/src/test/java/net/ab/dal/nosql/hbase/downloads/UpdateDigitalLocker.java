package net.ab.dal.nosql.hbase.downloads;

import java.util.List;

import net.ab.dal.nosql.appworld.downloads.HBDigitalLockerDAO;
import net.ab.dal.nosql.appworld.downloads.HBDigitalLockerParam;
import net.ab.dal.nosql.appworld.downloads.HBDownloadImpl;
import net.ab.dal.nosql.hbase.InvalidHBaseBeanException;
import net.ab.dal.nosql.hbase.common.AWHBaseException;

public class UpdateDigitalLocker {

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
		param.setContentId(57670l);
		param.setReleaseId(129212l);
		
//		param.setNumRows(10);
		
		HBDownloadImpl dao = new HBDownloadImpl();

		try {
			List<HBDigitalLockerDAO> result = dao.getDigitalLocker(param);
		
			if (result == null || result.size() == 0) {
				echo("No records");
			} else { 
				HBDigitalLockerDAO lockerRec = result.get(0);
				echo(lockerRec);
				lockerRec.setStatus(2);
				dao.updateDigitalLockerRecord(lockerRec);

			}
		} catch(AWHBaseException e){
			echo(e.getErrorCode() +" : "+e.getMessage());
		}

	}

}
