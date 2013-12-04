package com.cellmania.cmreports.db.request;

import java.util.Collection;
import java.util.LinkedList;

import com.cellmania.cmreports.db.sqlsession.SessionExecutor;
@SuppressWarnings("unchecked")
public class RequestDao implements IRequest {
	
	private static IRequest dao = null;
	
	public static IRequest getInstance(){
		if (dao == null) {
			dao = new RequestDao();
		}
		return dao;
	} 
	
	public Collection<FrequencyDTO> getSchedulingFrequencies() throws Exception {
		return (Collection<FrequencyDTO>) SessionExecutor.selectList("ReportMaster.getSchedulingFrequencies");
	}
	
	
	public FrequencyDTO getFrequencyDetails(Long frequencyId) throws Exception {
		return (FrequencyDTO) SessionExecutor.selectOne("ReportMaster.getFrequency", frequencyId);
	}
	
	
	public Long addRequest(RequestDTO dto) throws Exception{
		Long requestId = (Long) SessionExecutor.selectOne("RequestMaster.generateRequestId");
		dto.setRequestId(requestId);
		dto.getMail().setMailId(requestId);
		
		LinkedList<Object[]> queryAndParams = new LinkedList<Object[]>();
		
		Object[] params = new Object[2];
		params[0] = "RequestMaster.addMailDetails";
		params[1] = dto.getMail();
		queryAndParams.add(params);
		
		params = new Object[2];
		params[0] = "RequestMaster.addRequest";
		params[1] = dto;
		queryAndParams.add(params);
		
		SessionExecutor.insert(queryAndParams);
		return requestId;
	}


	
	public void updateRequest(RequestDTO dto) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	public RequestDTO getRequestDetails(RequestParams rparams) throws Exception {
		return (RequestDTO) SessionExecutor.selectOne("RequestMaster.getRequestDetail", rparams.getRequestId());
		
	}


	
	public Long addExecutionLog(ExecutionLogDTO dto) throws Exception {
		SessionExecutor.update("RequestMaster.createExecutionLog", dto);
		return dto.getId();
	}
	
	
	public void completeJobExecution(ExecutionLogDTO dto) throws Exception {
		SessionExecutor.update("RequestMaster.completeJobExecution", dto);
	}

	public Collection<RequestDTO> getRequestForUser(RequestParams rparams)
			throws Exception {
		return (Collection<RequestDTO>) SessionExecutor.selectList(
				"RequestMaster.getRequestForUser", rparams,
				rparams.getStartRow(), rparams.getNumRows());
	}

	public Collection<ExecutionLogDTO> getExecutionLogForRequest(
			RequestParams rparams) throws Exception {
		return (Collection<ExecutionLogDTO>) SessionExecutor.selectList(
				"RequestMaster.getExecutionLogForRequest",rparams,
				rparams.getStartRow(), rparams.getNumRows());
	}


	
	public ExecutionLogDTO getExecutionLogDetails(Long exeLogId)
			throws Exception {
		RequestParams rp = new RequestParams();
		rp.setExeLogId(exeLogId);
		return (ExecutionLogDTO) SessionExecutor.selectOne("RequestMaster.getExecutionLogForRequest", rp);
	}


	
	public Long getRequestForUserCount(RequestParams rparams) throws Exception {
		return (Long) SessionExecutor.selectOne("RequestMaster.getRequestForUserCount", rparams);
	}


	
	public Long getExecutionLogForRequestCount(RequestParams rparams)
			throws Exception {
		return (Long) SessionExecutor.selectOne("RequestMaster.getExecutionLogForRequestCount", rparams);
	}


	
	public void unscheduleJob(RequestDTO dto) throws Exception {
		SessionExecutor.update("RequestMaster.unscheduleJob", dto);
	}


	
	public void updateMailDetails(RequestDTO dto) throws Exception {
		LinkedList<Object[]> queryAndParams = new LinkedList<Object[]>();
		
		Object[] params = new Object[2];
		params[0] = "RequestMaster.updateMailDetails";
		params[1] = dto.getMail();
		queryAndParams.add(params);
		
		params = new Object[2];
		params[0] = "RequestMaster.updateRequest";
		params[1] = dto;
		queryAndParams.add(params);
		
		SessionExecutor.update(queryAndParams);
	}


	
	public RequestDTO getSQLAndHeader(Long requestId) throws Exception {
		return (RequestDTO) SessionExecutor.selectOne("RequestMaster.getSQLAndHeader", requestId);
	}


	
	public void updateSql(RequestDTO dto) throws Exception {
		SessionExecutor.update("RequestMaster.updateRequest", dto);
	}

	public void forceTermination(RequestDTO rDto, ExecutionLogDTO eDto)
			throws Exception {
		LinkedList<Object[]> queryAndParams = new LinkedList<Object[]>();
		
		Object[] params = new Object[2];
		params[0] = "RequestMaster.updateAllActiveExeLog";
		params[1] = eDto;
		queryAndParams.add(params);
		
		params = new Object[2];
		params[0] = "RequestMaster.updateAllActiveRequest";
		params[1] = rDto;
		queryAndParams.add(params);
		
		SessionExecutor.update(queryAndParams);
		
	}

}
