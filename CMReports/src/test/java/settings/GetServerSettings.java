package settings;

import com.cellmania.cmreports.db.CMReportFactory;
import com.cellmania.cmreports.db.ICMReports;
import com.cellmania.cmreports.db.masters.CarrierParams;
import com.cellmania.cmreports.web.util.ServerSettingsConstants;

public class GetServerSettings {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ICMReports dao = CMReportFactory.getICMReports();
		//System.out.println("Server Setting : "+ServerSettingsConstants._MAIL_FROM_ADDRESS+" : "+dao.getServerSettingValue(ServerSettingsConstants._MAIL_FROM_ADDRESS));
		System.out.println(dao.getCarrier(new CarrierParams(38L)));
	}

}
