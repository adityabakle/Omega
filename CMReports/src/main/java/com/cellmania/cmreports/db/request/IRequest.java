package com.cellmania.cmreports.db.request;

import java.util.Collection;

public interface IRequest {
	public Collection<FrequencyDTO> getSchedulingFrequencies() throws Exception;
	public FrequencyDTO getFrequencyDetails(Long frequencyId) throws Exception;
	public Long addRequest(RequestDTO dto) throws Exception;
	public void updateRequest(RequestDTO dto) throws Exception;
	public RequestDTO getRequestDetails(RequestParams rparams) throws Exception;
	public Long addExecutionLog(ExecutionLogDTO dto) throws Exception;
	public void completeJobExecution(ExecutionLogDTO dto) throws Exception ;
	
	public Collection<RequestDTO> getRequestForUser(RequestParams rparams) throws Exception;
	public Collection<ExecutionLogDTO> getExecutionLogForRequest(RequestParams rparams) throws Exception;
	public ExecutionLogDTO getExecutionLogDetails(Long exeLogId) throws Exception;
	public Long getExecutionLogForRequestCount(RequestParams rparams) throws Exception;
	public Long getRequestForUserCount(RequestParams rparams) throws Exception;
	
	public void unscheduleJob(RequestDTO dto)  throws Exception;
	public void updateMailDetails(RequestDTO dto) throws Exception;
	public void updateSql(RequestDTO dto) throws Exception;
	public RequestDTO getSQLAndHeader(Long requestId) throws Exception;
	
	public void forceTermination(RequestDTO rDto, ExecutionLogDTO eDto) throws Exception;
}
