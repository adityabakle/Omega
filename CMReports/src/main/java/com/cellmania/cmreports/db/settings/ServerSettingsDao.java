package com.cellmania.cmreports.db.settings;

import java.util.Collection;
import java.util.Date;

import com.cellmania.cmreports.db.sqlsession.SessionExecutor;

@SuppressWarnings("unchecked")
public class ServerSettingsDao implements IServerSettings {

	private static IServerSettings dao = null;
	
	public static IServerSettings getInstance(){
		if (dao == null) {
			dao = new ServerSettingsDao();
		}
		return dao;
	}
	
	
	public Date getDBTimeStamp() throws Exception {
		return (Date) SessionExecutor.selectOne("ServerSettings.getDBTimeStamp");
	}

	
	public String getDBTimeZone() throws Exception {
		return (String) SessionExecutor.selectOne("ServerSettings.getDBTimeZone");
	}

	
	public String getServerSettingValue(String key) throws Exception {
		return (String) SessionExecutor.selectOne("ServerSettings.getServerSettingValueByKey", key);
	}

	
	public ServerSettingsDTO getServerSetting(Long id) throws Exception {
		ServerSettingParams sparams = new ServerSettingParams(id);
		return (ServerSettingsDTO) SessionExecutor.selectOne("ServerSettings.getServerSettings", sparams);
	}

	
	public long getServerSettingCount(ServerSettingParams ssparams)
			throws Exception {
		return (Long) SessionExecutor.selectOne("ServerSettings.getServerSettingsCount", ssparams);
	}

	
	
	public Collection<ServerSettingsDTO> getServerSettings(
			ServerSettingParams ssparams) throws Exception {
		return (Collection<ServerSettingsDTO>) SessionExecutor.selectList(
				"ServerSettings.getServerSettings", ssparams,
				ssparams.getStartRow(), ssparams.getNumRows());
	}

	
	public void addServerSetting(ServerSettingsDTO dto) throws Exception {
		SessionExecutor.insert("ServerSettings.addServerSettings", dto);
	}

	
	public void updateServerSetting(ServerSettingsDTO dto) throws Exception {
		SessionExecutor.update("ServerSettings.updateServerSettings", dto);
	}

	
	public void deleteServerSetting(Long id) throws Exception {
		SessionExecutor.delete("ServerSettings.deleteServerSettings",id);
		
	}

	
	public boolean duplicateCheck(ServerSettingParams ssparams)
			throws Exception {
		long count = (Long) SessionExecutor.selectOne("ServerSettings.checkDuplicateKey", ssparams);
		if(count>0) return true;
		else return false;
	}
	
}
