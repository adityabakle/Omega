package net.ab.dal.nosql.hbase.downloads;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.ab.dal.nosql.appworld.downloads.HBDownloadDAO;
import net.ab.dal.nosql.appworld.downloads.HBDownloadImpl;
import net.ab.dal.nosql.hbase.InvalidHBaseBeanException;
import net.ab.dal.nosql.hbase.common.AWHBaseException;

@SuppressWarnings("unused")
public class AddDownload {

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
		insertFromDB();
		//addDownload();
		System.exit(0);
	}

	
	
	private static void addDownload() throws AWHBaseException, InvalidHBaseBeanException, InterruptedException {
		HBDownloadDAO download = new HBDownloadDAO();
		
		download.setCustomerId(94948623l);
		download.setProductTypeId(2l);
		download.setContentTypeId(1l);
		download.setContentId(57670l);
		download.setFileBundleId(231580l);
		download.setReleaseId(129212l);
		download.setBlackberryModel("liverpool");
		download.setDevicePin(544581428l);
		download.setDeviceOsVersion(2814749767106560l);
		download.setCountryId(36l);
		download.setLocale("en_CA");
		download.setCarrierId(107l);
		download.setDeviceVendorId(1l);
		download.setCurrentMCCId(37l);
		download.setAwVersion(1125899906842624l);
		download.setListId(1l);
		download.setListGroupId(1l);
		download.setStorefrontId(1l);
		download.setPlatformDeviceTypeMapId(1); //1 BBOS  2 BB10  3 PB
		
		HBDownloadImpl dao = new HBDownloadImpl();
		dao.addDownload(download);
		echo("Done adding " + download.getRowKeyString());
	}
	
	
	private static void insertFromDB() throws AWHBaseException{
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection ("jdbc:oracle:thin:@awdbmv15.mvlab.testnet.rim.net:1521:appworld","rim","rim");

			PreparedStatement pstmt = con
					.prepareStatement("select r.contentid, fb.releaseid, c.contenttypeid, c.producttypeid, pdtm.id PDTMID, d.* "
							+ "from download d, release r, filebundle fb, content c, Blackberrydevice bd, platformdevicetypemap pdtm "
							+ "WHERE d.customerid IN (93448144, 35277580, 94920293, 63100049, 94948623, 55819378, 38157192, 62520355, 91044920)"
							+ " AND d.filebundleid = fb.id "
							+ "and fb.releaseid = r.id  "
							+ "and r.contentid = c.id "
							+ "AND d.blackberrymodel = bd.modelname "
							+ "AND bd.platformid = pdtm.platformid "
							+ "AND bd.devicetypeid = pdtm.devicetypeid");
			
			//pstmt.setLong(1, 91044920l); // 55819378, 38157192, 62520355, 91044920
			ResultSet rs = pstmt.executeQuery();
			
			if (rs != null) {
				HBDownloadImpl dao = new HBDownloadImpl();
				int cnt = 0;
				while (rs.next()) {
					HBDownloadDAO download = new HBDownloadDAO();
					
					download.setCustomerId(rs.getLong("customerid"));
					download.setContentId(rs.getLong("contentid"));
					download.setProductTypeId(rs.getLong("producttypeid"));
					download.setContentTypeId(rs.getLong("contenttypeid"));
					download.setPlatformDeviceTypeMapId(rs.getInt("PDTMID"));
					download.setFileBundleId(rs.getLong("filebundleid"));
					download.setReleaseId(rs.getLong("releaseid"));
					download.setDevicePin(rs.getLong("devicepin"));
					download.setDeviceOsVersion(rs.getLong("deviceosversion"));
					download.setBlackberryModel(rs.getString("blackberrymodel"));
					download.setCountryId(rs.getLong("countryid"));
					download.setLocale(rs.getString("locale"));
					download.setCarrierId(rs.getLong("carrierid"));
					download.setCurrentMCCId(rs.getLong("currentmccid"));
					download.setDeviceVendorId(rs.getLong("devicevendorid"));
					download.setListId(rs.getLong("listid"));
					download.setListGroupId(rs.getLong("listgroupid"));
					download.setStorefrontId(rs.getLong("storefrontid"));
					download.setAwVersion(rs.getLong("appworldversion"));
					
					/*System.out.println("************************************");
					System.out.println("Download = " + rs.getLong("id"));
					System.out.println("CustomerId = " + download.getCustomerId());
					System.out.println("ContentId = " + download.getContentId());
					System.out.println("FileBundleId = " + rs.getLong("filebundleid"));
					System.out.println();*/
					
					// Add to HBASE
					dao.addDownload(download);
					
					System.out.println("Added download " + rs.getLong("id") + " to HBase.");
					cnt++;
				}
				echo("Recorded " + cnt + " downloads.");
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
