package com.cellmania.cmreports.web.action;

import java.io.File;

import org.apache.log4j.Logger;

import com.cellmania.cmreports.db.request.ExecutionLogDTO;
import com.cellmania.cmreports.web.util.CMDBService;
import com.cellmania.cmreports.web.util.ServerSettingsConstants;

@SuppressWarnings("serial")
public class DownloadFileAction extends WebBaseAction {
	public static Logger log = Logger.getLogger(DownloadFileAction.class);
	private CMDBService cmrDB = null;
	private long id; 
	private String fileName;
	private File dlFile;
	
	public void prepare() { 
    	return;
    }
  
	public String execute() {
		if(id==0)
			return ERROR;
		try{
			ExecutionLogDTO exeDto = cmrDB.getExecutionLogDetails(id);
			if(exeDto!=null){
				log.debug("Download file for : "+exeDto);
				String fn = CMDBService.getServerSettingsValue(ServerSettingsConstants._REPORTS_DIR_PATH)+exeDto.getFileName();
				dlFile = new File (fn);
				if(!dlFile.exists()){
					fn = fn.substring(0,fn.lastIndexOf("."))+".zip";
					dlFile = new File (fn);
					if(!dlFile.exists()){
						addActionError("Server couldnot find file ("+dlFile.getName()+").");
						return ERROR;
					} 
				}
				
				log.debug("DL File : "+dlFile.getAbsolutePath());
				fileName = dlFile.getName();
				return SUCCESS;
			} else {
				addActionError("Invalid ID. Please check your URL.");
				return ERROR;
			}
			
			
		} catch (Exception e){
			return ERROR;
		}
	}
	
	
	
	/**
	 * Setter & Getter methods Defined below
	 * */
	public CMDBService getCmrDB() {
		return cmrDB;
	}

	public void setCmrDB(CMDBService cmrDB) {
		this.cmrDB = cmrDB;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public File getDlFile() {
		return dlFile;
	}

	public void setDlFile(File dlFile) {
		this.dlFile = dlFile;
	}
}
