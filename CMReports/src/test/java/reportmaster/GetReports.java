package reportmaster;

import com.cellmania.cmreports.db.CMReportFactory;
import com.cellmania.cmreports.db.ICMReports;
import com.cellmania.cmreports.db.masters.CarrierParams;
import com.cellmania.cmreports.db.masters.ReportParams;

public class GetReports {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ReportParams rparams = new ReportParams();
		rparams.setEnabled(true);
		ICMReports dao = CMReportFactory.getICMReports();
				
		System.out.println(dao.getReports(rparams));
		System.out.println(dao.getCarriers(new CarrierParams()));
		
		
		
		
	}

}
