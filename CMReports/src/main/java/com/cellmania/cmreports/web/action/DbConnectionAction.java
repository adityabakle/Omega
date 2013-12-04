package com.cellmania.cmreports.web.action;

import java.util.Date;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

import com.cellmania.carriers.sqlSession.CarrierSessionExecutor;
import com.cellmania.carriers.sqlSession.CarrierSqlSessionConfig;
import com.cellmania.cmreports.common.CMException;
import com.cellmania.cmreports.db.masters.CarrierMasterDTO;
import com.cellmania.cmreports.db.masters.CarrierParams;
import com.cellmania.cmreports.web.util.CMDBService;
 
@SuppressWarnings("serial")
public class DbConnectionAction extends WebBaseAction {
	public static Logger log = Logger.getLogger(DbConnectionAction.class);
	private Long carrierId;
	private CMDBService cmrDB = null;
	private CarrierMasterDTO crDto; 
	public CMDBService getCmrDB() {
		return cmrDB;
	}

	public void setCmrDB(CMDBService cmrDB) {
		this.cmrDB = cmrDB;
	}

	public void prepare(){
		
	}
	
	public String execute() {
		if(carrierId != null){
			String errorMessage = null;
			
			try{
				crDto = cmrDB.getCarrier(new CarrierParams(carrierId));
				if(crDto==null) throw new CMException("Invalid Carrier ID");
				
				SqlSessionFactory session = CarrierSqlSessionConfig.getSqlMapClient(crDto);
				Date dbSysDate = (Date)CarrierSessionExecutor.selectOne(session, "Common.dbTimeStamp");
				errorMessage = "Connection Successful @ : "+dbSysDate;
			} catch (CMException e){
				log.error("Error fetchiong Carrier Details ("+carrierId+"): "+e.getMessage(),e);
				errorMessage = "Unable to fetch Carrier Details. "+e.getMessage();
			} catch (Exception e){
				log.error("Error checking connection status for ("+crDto.getDisplayName()+"): "+e.getMessage(),e);
				errorMessage = "Error Connection to DB for carrier ("+crDto.getDisplayName()+"): "+e.getMessage();
			}
			addFieldError("conCheckStatus", errorMessage);
		}
		
        return SUCCESS;
    }

	
	
	
	public Long getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Long carrierId) {
		this.carrierId = carrierId;
	}

	public CarrierMasterDTO getCrDto() {
		return crDto;
	}

	public void setCrDto(CarrierMasterDTO crDto) {
		this.crDto = crDto;
	}

}
