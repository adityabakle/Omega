package net.ab.dal.nosql.hbase.downloads;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import net.ab.dal.nosql.appworld.downloads.HBDigitalLockerDAO;
import net.ab.dal.nosql.appworld.downloads.HBDownloadImpl;
import net.ab.dal.nosql.hbase.InvalidHBaseBeanException;
import net.ab.dal.nosql.hbase.common.AWHBaseException;

public class AddDigitalLockerRecord {

	public static void echo(Object obj){
		System.out.println(obj.toString());
	}
	
	/**
	 * @param args
	 * @throws AWHBaseException 
	 * @throws InvalidHBaseBeanException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws AWHBaseException, InvalidHBaseBeanException, InterruptedException {
		insertAppsGamesFromDB();
		System.exit(0);
	}

	
	private static void addDigitalLockerRecord() throws AWHBaseException, InvalidHBaseBeanException, InterruptedException {
		HBDigitalLockerDAO lockerRec = new HBDigitalLockerDAO();
		
		lockerRec.setCustomerId(94948623l);
		lockerRec.setProductTypeId(2l);
		lockerRec.setContentTypeId(1l);
		lockerRec.setContentId(57670l);
		lockerRec.setReleaseId(129212l);
		
		lockerRec.setParentContentId(231580l);
		lockerRec.setShowContentId(10l);
		lockerRec.setTrackContentId(5552248l);
		lockerRec.setReleaseContentId(5552245l);
		lockerRec.setShowContentId(10l);

		lockerRec.setDeleteFlag("N");
		lockerRec.setStatus(1);
		lockerRec.setFromRelease("Y");
		lockerRec.setFromTrack("N");
		lockerRec.setLicenseTypeId(1l);
		lockerRec.setLastDownloadDate(new Date().getTime());
		
		HBDownloadImpl dao = new HBDownloadImpl();
		dao.addDigitalLockerRecord(lockerRec);
		echo("Done adding " + lockerRec.getRowKeyString());
	}
	
	
	private static void insertAppsGamesFromDB() throws AWHBaseException{
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection ("jdbc:oracle:thin:@awdbmv15.mvlab.testnet.rim.net:1521:appworld","rim","rim");

			PreparedStatement pstmt = con.prepareStatement("select r.contentid, r.id releaseid, c.contenttypeid,  cl.licensetypeid, c.producttypeid, cd.* " +
														   "from customerdownload cd, release r, content c, Contentlicensetype cl " +
														   "where cd.customerid IN (36643954)" +
														   //"93448144, 35277580, 94920293, 63100049, 94948623, 55819378, 38157192, 62520355, 91044920) " +
														   "	  and cd.releaseid = r.id " +
														   "	  and r.contentid = c.id " +
														   "AND r.contentid = cl.contentid");
			
			//pstmt.setLong(1, 55819378l);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs != null) {
				HBDownloadImpl dao = new HBDownloadImpl();
				int cnt = 0;
				while (rs.next()) {
					HBDigitalLockerDAO locker = new HBDigitalLockerDAO();
					
					locker.setCustomerId(rs.getLong("customerid"));
					locker.setContentId(rs.getLong("contentid"));
					locker.setProductTypeId(rs.getLong("producttypeid"));
					locker.setContentTypeId(rs.getLong("contenttypeid"));
					locker.setReleaseId(rs.getLong("releaseid"));
					locker.setLicenseTypeId(rs.getLong("licensetypeid"));
					
					locker.setDeleteFlag(rs.getString("deleteflag"));
					Timestamp lastDownloadDate = rs.getTimestamp("lastdownloaddate");
					if (lastDownloadDate != null) {
						locker.setLastDownloadDate(lastDownloadDate.getTime());
					}
					
					/*System.out.println("************************************");
					System.out.println("CustomerId = " + locker.getCustomerId());
					System.out.println("ContentId = " + locker.getContentId());
					System.out.println("ReleaseId = " + locker.getReleaseId());
					System.out.println();*/
					
					// Add to HBASE
					dao.addDigitalLockerRecord(locker);
					
					System.out.println("Added download record to the Digital Lockker in HBase.");
					cnt++;
				}
				echo("Recorded " + cnt + " apps / games records to the locker.");
			} else { 
				echo("No redords in DB");
			}
			con.close();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
