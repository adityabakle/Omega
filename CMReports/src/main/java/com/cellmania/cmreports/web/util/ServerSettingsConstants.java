package com.cellmania.cmreports.web.util;

import java.util.Collection;
import java.util.HashMap;

import com.cellmania.cmreports.common.CMException;
import com.cellmania.cmreports.db.settings.ServerSettingParams;
import com.cellmania.cmreports.db.settings.ServerSettingsDTO;

public class ServerSettingsConstants {
	public static final String _SERVER_URL = "server_url";
	public static final String _REPORTS_DIR_PATH = "reports_dir_path";
	public static final String _MAIL_SMTP_HOST = "mail_smtp_host";
	public static final String _MAIL_DEFAULT_FOOTER = "mail_default_footer";
	public static final String _MAIL_DEFAULT_HEADER = "mail_default_header";
	public static final String _MAIL_FROM_ADDRESS = "mail_from_address";
	public static final String _MAIL_FROM_NAME = "mail_from_name";
	public static final String _MAX_LOGIN_ATTEMPTS = "max_login_attempts";
	public static final String _PASSWORD_EXPIRY_DAYS = "password_expiry_days";
	public static final String _CARRIER_SQLCONFIG_PATH = "carrier_sqlConfig_path";
	public static final String _CARRIER_XLSCLASS_PACKAGE = "carrier_xls_class_package";
	public static final String _CARRIER_DB_SERVICE_CLASS = "carrier_db_service_class";
	public static final String _SERVER_TIME_ZONE = "server_time_zone";
	public static final String _USER_ADMIN_ROLE_ID = "user_admin_role_id";
	public static final String _ADMINISTRATOR_ANNOUNCEMENT = "administrator_announcement";
	public static final String _MAIL_FILEDOWNLOAD_MSG = "mail_file_download_msg";
	public static final String _CONTENT_URL = "content_url";
	
	
	private static boolean reload = true;
	private static HashMap<String,String> serverSettings;
	
	static {
		loadServerSettings();
	}
	
	public static String getServerSettingValues(String key){
		if(reload)
			loadServerSettings();
		return serverSettings.get(key);
		
	}

	private static void loadServerSettings() {
		if(serverSettings==null || reload)
			serverSettings = new HashMap<String, String>();
		
		CMDBService cm = new CMDBService();
		ServerSettingParams sp =  new ServerSettingParams();
		sp.setEnabled(true);
		try {
			Collection<ServerSettingsDTO> col = cm.getServerSettings(sp);
			if(col!=null && col.size()>0){
				for(ServerSettingsDTO s: col){
					serverSettings.put(s.getKey(), s.getValue());
				}
			}
			reload = false;
		} catch (CMException e) {
			System.out.println("Error fetching ServerSettings "+e.getMessage());
			e.printStackTrace();
		}
		
		
	}
}
