package net.ab.dal.nosql.hbase.downloads;

import net.ab.dal.nosql.appworld.downloads.HBDigitalLockerStatsDAO;
import net.ab.dal.nosql.appworld.downloads.HBDownloadImpl;

public class FetchLockerStats {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		HBDigitalLockerStatsDAO d = new HBDigitalLockerStatsDAO();
		d.setCustomerId(94920293l);
		System.out.println(d.getRowKeyString());
		d = (new HBDownloadImpl()).getCustomerLockerStats(d);
		System.out.println(d);

	}

}
