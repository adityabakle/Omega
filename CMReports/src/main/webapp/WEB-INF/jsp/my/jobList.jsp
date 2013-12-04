<%@taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>

<div id="schJobTabDiv_<s:property value="selectedTab"/>" style="width:100%">
<s:actionmessage cssClass="success"/>
<s:actionerror cssClass="errorMsg"/>
<label>
	<s:if test="selectedTab==0">
		List of all jobs currently Scheduled in the System.
		<script>
			setTimeout("realodMe();",5000);
			function realodMe(){
				$.publish("reloadMe");
			}
		</script>
	</s:if> <s:elseif test="selectedTab==1">
		List of jobs that are currently executing. 
	</s:elseif> <s:elseif test="selectedTab==2">
		List of jobs requested by you. 
	</s:elseif><s:elseif test="selectedTab==3">
		List of all your past expired jobs.
	</s:elseif><s:elseif test="selectedTab==4">
		All Jobs.
	</s:elseif><s:elseif test="selectedTab==5">
		All Failed Jobs.
	</s:elseif>
</label><br/><br/>
<s:form id="requestAjaxFrm_%{selectedTab}" action="ajaxJobs" name="/my">
	<s:hidden name="pageStartIndex"/>
	<s:hidden name="pageTotalRecCount"/>
	<s:hidden name="selectedTab"/>
	<sj:submit cssStyle="display:none" value="Submit" targets="%{'schJobTabDiv_'+selectedTab}" listenTopics="SubmitMe"></sj:submit>	

	<table style="width:100%">
		<tr>
			<td class="label">Carrier</td>
			<td class="field">
				<s:url action="json_fetchCarriersForUser" namespace="/" var="jsonCarriersURL"/>
				<sj:select
					cssStyle="width:150px"
					href="%{jsonCarriersURL}"
					name="reqReport.carrier.carrierId" 
					list="carriers" 
					listKey="carrierId" 
					listValue="displayName"
					emptyOption="false"
					onCompleteTopics="reloadReportDropdown"
					onChangeTopics="reloadReportDropdown,SubmitMe"
					headerKey="0"
					headerValue="-- All --"
					/></td>
			<td class="label">Report</td>
			<td class="field">
				<s:url action="json_fetchReportsForCarrier" namespace="/" var="jsonReport4CarriersURL"/>
				<sj:select
					
					cssStyle="width:170px"
					formIds="requestAjaxFrm_%{selectedTab}"
					deferredLoading="true"
					href="%{jsonReport4CarriersURL}"
					name="reqReport.report.reportId" 
					list="reports" 
					listKey="reportId" 
					listValue="displayName"
					emptyOption="false"
					reloadTopics="reloadReportDropdown"
					onChangeTopics="SubmitMe"
					
					headerKey="0"
					headerValue="-- All --"
					/>
			</td>
		</tr>
	</table>
</s:form>
<s:if test="selectedTab!=5">
<table class="dataGrid-table">
	<thead>
	<tr >
		<th>ID</th>
		<th>Report</th>
		<th>Carrier</th>
		<th>Report Date Range</th>
		<th>Frequency</th>
		<th>Schedule Execution</th>
		<s:if test="%{#session.loginUser.role.roleId == 1}">
		<th>Requested By</th>
		</s:if>
		<th>Action</th>
	</tr>
	</thead>
	<tbody>
	<s:if test="myReqReports!=null && myReqReports.size() > 0">
		<s:iterator value="myReqReports" status="stat">
		<s:if test="#stat.even">
			<s:set var="alternate">alternate</s:set>
		</s:if><s:else>
			<s:set var="alternate" value="%{''}"/>
		</s:else>
		<tr class="<s:property value="%{#alternate}"/>">
			<td class="id"> <s:property value="requestId"/> </td>
			<td> 
				<s:property value="report.displayName"/>
				<s:if test="%{report.name=='sqlRpt'}">
					<br/><span class="smallTxt"><s:property value="mail.subject"/></span>
				</s:if>  
			</td>
			<td> <s:property value="carrier.displayName"/></td>
			<td> <s:if test="startDate==null"> N/A</s:if><s:else><s:date name="startDate" format="dd-MMM-yyyy"/> to  <s:date name="endDate" format="dd-MMM-yyyy"/></s:else> </td>
			<td> <s:property value="frequency.name"/></td>
			<td>
				<s:if test="state=='active'">
					<span class="required">Executing...</span><img id="loadingbar" alt="Loading..." src="<s:property value="%{#request.CONTEXT}"/>/images/loading.gif">
				</s:if> 
					<s:elseif test="state=='expired'">
					Last: <s:if test="lastExecutedDate==null"> N/A</s:if><s:else><s:date name="lastExecutedDate" format="dd-MMM-yyyy HH:mm" timezone="%{#session.loginUser_timeZone_Id}" nice="false"/></s:else><br/>
					<s:if test="selectedTab==4">
						Start: <s:if test="scheduledDate==null"> N/A</s:if><s:else><s:date name="scheduledDate" format="dd-MMM-yyyy HH:mm" timezone="%{#session.loginUser_timeZone_Id}" nice="false"/></s:else><br/>
					</s:if>
					Expires: <s:if test="expiryDate==null"> N/A </s:if><s:else><s:date name="expiryDate" format="dd-MMM-yyyy HH:mm" timezone="%{#session.loginUser_timeZone_Id}"/></s:else>
				</s:elseif>
					<s:else>
					<b>Next:</b> <s:if test="nextExecutionDate==null"> N/A</s:if><s:else><b><s:date name="nextExecutionDate" format="dd-MMM-yyyy HH:mm" timezone="%{#session.loginUser_timeZone_Id}"/></b></s:else><br/>
					Last: <s:if test="lastExecutedDate==null"> N/A</s:if><s:else><s:date name="lastExecutedDate" format="dd-MMM-yyyy HH:mm" timezone="%{#session.loginUser_timeZone_Id}" nice="false"/></s:else><br/>
					Start: <s:if test="scheduledDate==null"> N/A</s:if><s:else><s:date name="scheduledDate" format="dd-MMM-yyyy HH:mm" timezone="%{#session.loginUser_timeZone_Id}" nice="false"/></s:else><br/> 
					Expires: <s:if test="expiryDate==null"> Never </s:if><s:else><s:date name="expiryDate" format="dd-MMM-yyyy HH:mm" timezone="%{#session.loginUser_timeZone_Id}"/></s:else>
				</s:else>
			</td>
			<s:if test="%{#session.loginUser.role.roleId == 1}">
				<td> <s:property value="requestedBy.name"/></td>
			</s:if>
			<td valign="middle">
				<s:url action="details" namespace="/report" var="reqDetailsURl">
					<s:param name="requestId" value="requestId"/>
					<s:param name="selectedTab" value="%{#selectedTab}"/>
				</s:url>
				<sj:a cssClass="hyperlink" href="%{reqDetailsURl}" openDialog="requestDetailsDLG"><img src="<s:property value="%{#request.CONTEXT}"/>/images/view.png" title="View Request" alt="View" height="15"></sj:a>
				<s:if test="%{(#session.loginUser.role.roleId == 1 || #session.loginUser.userId == requestedBy.userId) && state!='expired'}">
					<sj:a openDialog="unscheduleConfirmationDlgID"><img src="<s:property value="%{#request.CONTEXT}"/>/images/delete.png" title="Delete Request" alt="Delete" height="15" onclick="$('#requestIdHiddenID').val(<s:property value="requestId"/>);" ></sj:a>
				</s:if>
			</td>
		</tr>
		</s:iterator>
	</s:if><s:else>
		<tr><td align="center" colspan="7">
			<s:if test="selectedTab==0">
				There are no jobs scheduled in the system.
			</s:if> <s:elseif test="selectedTab==1">
				There are currently no active jobs executing in the system.
			</s:elseif> <s:elseif test="selectedTab==2">
				You have not yet submitted any jobs to the system.
			</s:elseif><s:elseif test="selectedTab==3">
				No records found.
			</s:elseif>
		</td></tr>
	</s:else>	
	</tbody>
</table>
</s:if>
<s:else> <!--  Shows failed Jobs List Below -->
<table class="dataGrid-table">
	<thead>
	<tr >
		<th>ID</th>
		<th>Carrier</th>
		<th>File Name</th>
		<th>Report Date Range</th>
		<th>Generated On</th>
		<th>Status</th>
		<th>Mailing List</th>
		<th>Action</th>
	</tr>
	</thead>
	<tbody>
	<s:if test="exeLogs!=null && exeLogs.size() > 0">
		<s:iterator value="exeLogs" status="stat">
		<s:if test="#stat.even">
			<s:set var="alternate">alternate</s:set>
		</s:if><s:else>
			<s:set var="alternate" value="%{''}"/>
		</s:else>
		<tr class="<s:property value="%{#alternate}"/>">
			<td class="id"> <s:property value="id"/> </td>
			<td> <s:property value="request.carrier.displayName" default="N/A"/> </td>
			<td> <s:property value="fileName" default="N/A"/> </td>
			<td> <s:if test="reportStartDate==null">N/A</s:if><s:else><s:date name="reportStartDate" format="dd-MMM-yyyy"/> to  <s:date name="reportEndDate" format="dd-MMM-yyyy"/></s:else> </td>
			<td> <s:if test="endTime==null"> N/A</s:if><s:else><s:date name="endTime" format="dd-MMM-yyyy HH:mm" timezone="%{#session.loginUser_timeZone_Id}" nice="false"/></s:else></td>
			<td class="status-<s:property value="success"/>">
				<s:if test="success">
					Success
				</s:if><s:else>
					Failed! <a href="javascript:void 0;" class="hperlink" onclick="$('#myRpt_err_td_<s:property value="id"/>').toggle()"><img alt="View/Hide Error" src="<s:property value="%{#request.CONTEXT}"/>/images/viewIcon.png" title="View Error Details." height="16"/></a>
				</s:else>
			</td>
			<td>
				To: <s:property value="mailedTo"/><br/>
				CC: <s:property value="mailedCc" default="N/A"/> 
			</td>
			<td>
				<s:url action="details" namespace="/report" var="reqDetailsURl">
					<s:param name="requestId" value="request.requestId"/>
				</s:url>
				<s:url action="dlFile" namespace="/" var="dlFileURL">
					<s:param name="id" value="id"/>
				</s:url>
				<s:if test="success || fileName!=null">
					<s:a href="%{dlFileURL}"><img src="<s:property value="%{#request.CONTEXT}"/>/images/download.png" title="Download Report" alt="Download" height="15"></s:a>
				</s:if>&nbsp;<sj:a cssClass="hyperlink" href="%{reqDetailsURl}" openDialog="requestDetailsDLG" ><img src="<s:property value="%{#request.CONTEXT}"/>/images/view.png" title="View Request" alt="View" height="15"></sj:a>
			</td>
		</tr>
		<tr id="myRpt_err_td_<s:property value="id"/>" class="<s:property value="%{#alternate}"/>" style="display:none;">
			<td colspan="7" class="errorMsg"> <s:property value="errorReason"/> </td>
		</tr>
		</s:iterator>
	</s:if><s:else>
		<tr><td align="center" colspan="7">
				No records found.
		</td>
	</s:else>	
	</tbody>
</table>
</s:else>
<s:form name="unscheduleJobForm" action="unschedule" namespace="/my">
	<s:hidden name="pageStartIndex"/>
	<s:hidden name="pageTotalRecCount"/>
	<s:hidden name="selectedTab"/>
	<s:hidden id="requestIdHiddenID" name="requestId"/>
	<sj:submit value="Delete Me" listenTopics="dlgUnscheduleConfirmed" targets="schJobTabDiv_%{selectedTab}" cssStyle="visibility: hidden;"/>
</s:form>



<s:include value="/WEB-INF/jsp/pagination.jsp">
 	<s:param name="frmId" value="%{'requestAjaxFrm_'+selectedTab}"/>
 	<s:param name="targetId" value="%{'schJobTabDiv_'+selectedTab}"/>
 	<s:param name="totalCount" value="pageTotalRecCount"></s:param>
 </s:include>
 
</div>
