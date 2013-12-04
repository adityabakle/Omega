package reportmaster;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.cellmania.cmreports.common.Encryptor;
import com.cellmania.cmreports.db.masters.UserMasterDTO;
import com.cellmania.cmreports.web.util.CMDBService;
import com.cellmania.cmreports.web.util.ServerSettingsConstants;
import com.cellmania.cmreports.web.util.WebUtility;

public class AddUpdateReport {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		/*UserMasterDTO user = new UserMasterDTO();
		user.setUserName("aditya");
		user.setEmail("abakle@rim.com");
		user.setName("Aditya Bakle");
		user.setRoleId(1l);
		user.setEnabled(true);
		user.setUpdatedBy(2l);
		user.setPassword(Encryptor.encrypt("mm@dm@n", Encryptor.getSalt("aditya")));*/
		
		Date dt = new Date();
		String serverTz = CMDBService.getServerSettingsValue(ServerSettingsConstants._SERVER_TIME_ZONE);
		System.out.println("Before :"+ dt);
		TimeZone uTZ = getUserTimeZone(-330);
		int  i = uTZ.getDSTSavings();
		System.out.println("User Time Zone :"+ uTZ.getDisplayName(i>0?true:false, TimeZone.SHORT));
		
		System.out.println("After :"+getScheduledDateTime(dt,uTZ,TimeZone.getTimeZone(serverTz)));
		
		
	}

	public static Date getScheduledDateTime(Date schDate, String frmTZ, String toTZ) throws ParseException{
		return getScheduledDateTime(schDate,TimeZone.getTimeZone(frmTZ), TimeZone.getTimeZone(toTZ));
	}
	
	public static Date getScheduledDateTime(Date schDate, TimeZone frmTZ,
			TimeZone toTZ) throws ParseException {
		// get Offset of User Time Zone
		int oOff = frmTZ.getOffset(schDate.getTime());
		
		// Change the date to GMT by substracting user timezone offset
		long dateInGMT = schDate.getTime() - oOff;
		Date dtGMT = new Date(dateInGMT);
		
		//Get offset for Server time Zone 
		int noff = toTZ.getOffset(schDate.getTime());
		
		// Add offset to GMT date so that it is converted to Server time 
		long dtInServer = dtGMT.getTime()+noff;
		Date serverDate =new Date(dtInServer) ; 
		
		return serverDate;
	}
	
	
	public static TimeZone getUserTimeZone(Integer userOffset){
		int iOff = -(userOffset)/60;
		int remainder = userOffset%60;
		TimeZone sTz = TimeZone.getTimeZone("GMT"+(iOff>0?"+"+iOff:iOff)+ (remainder==0?"":":30"));
		return sTz; 
	}
}
