package net.ab.dal.nosql.hbase.downloads;

import net.ab.dal.nosql.appworld.downloads.HBDownloadImpl;
import net.ab.dal.nosql.appworld.downloads.HBDownloadStatsDAO;

public class FetchDownloadStats {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		HBDownloadStatsDAO d = new HBDownloadStatsDAO();
		d.setContentId(680l);
		//d.setPlatformDeviceTypeMapId(1l);
		d.getRowKeyString();
		d = (new HBDownloadImpl()).getDownloadStats(d);
		System.out.println(d);

	}

}
