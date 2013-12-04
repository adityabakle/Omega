package com.cellmania.cmreports.db.settings;

import java.util.Collection;
import java.util.Date;

public interface IServerSettings {
	public Date getDBTimeStamp() throws Exception;
	public String getDBTimeZone() throws Exception;
	public String getServerSettingValue(String key) throws Exception;
	public ServerSettingsDTO getServerSetting(Long id) throws Exception;
	public long getServerSettingCount(ServerSettingParams ssparams) throws Exception;
	public Collection<ServerSettingsDTO> getServerSettings(ServerSettingParams ssparams) throws Exception;
	public void addServerSetting(ServerSettingsDTO dto) throws Exception;
	public void updateServerSetting(ServerSettingsDTO dto) throws Exception;
	public void deleteServerSetting(Long id) throws Exception;
	public boolean duplicateCheck(ServerSettingParams ssparams) throws Exception;
}
