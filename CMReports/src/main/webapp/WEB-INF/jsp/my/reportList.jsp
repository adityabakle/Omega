<%@taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>

<s:form id="myReportAjaxFrm" action="ajaxMyReports" name="/my">
	<s:hidden name="pageStartIndex"/>
	<s:hidden name="pageTotalRecCount"/>
	<sj:submit cssStyle="display:none" value="Submit" targets="reportListDIVID" listenTopics="SubmitMe"/>	

	<table style="width:100%">
		<tr>
		<td class="title"  colspan="4">Filter Reports by: </td>
		</tr>
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
					onchange="$('#myRepSelRepList')[0].value = 0;"
					headerKey="0"
					headerValue="-- All --"
					/></td>
			<td class="label">Report</td>
			<td class="field">
				<s:url action="json_fetchReportsForCarrier" namespace="/" var="jsonReport4CarriersURL"/>
				<sj:select
					id="myRepSelRepList"
					cssStyle="width:170px"
					formIds="myReportAjaxFrm"
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

<s:include value="/WEB-INF/jsp/pagination.jsp">
 	<s:param name="frmId">myReportAjaxFrm</s:param>
 	<s:param name="targetId">reportListDIVID</s:param>
 	<s:param name="totalCount" value="pageTotalRecCount"></s:param>
 </s:include>