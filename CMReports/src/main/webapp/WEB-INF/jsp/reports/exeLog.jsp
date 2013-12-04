<%@taglib uri="/struts-tags" prefix="s"%>
<s:actionmessage cssClass="success"/>
<s:actionerror cssClass="errorMsg"/>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>

<table class="dataGrid-table">
	<thead>
	<tr >
		<th>ID</th>
		<th>File Name</th>
		<th>Report Date Range</th>
		<th>Generated On</th>
		<th>Status</th>
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
			<td> <s:property value="fileName" default="N/A"/> </td>
			<td> <s:if test="reportStartDate==null">N/A</s:if><s:else><s:date name="reportStartDate" format="dd-MMM-yyyy"/> to  <s:date name="reportEndDate" format="dd-MMM-yyyy"/></s:else> </td>
			<td> <s:if test="endTime==null"> N/A</s:if><s:else><s:date name="endTime" format="dd-MMM-yyyy HH:mm" timezone="%{#session.loginUser_timeZone_Id}" nice="false"/></s:else></td>
			<td class="status-<s:property value="success"/>">
				<s:if test="success==null">
					Executing...<img id="loadingbar" alt="Loading..." src="<s:property value="%{#request.CONTEXT}"/>/images/loading.gif">
				</s:if>
				<s:elseif test="success">
					Success
				</s:elseif><s:else>
					Failed! <a href="javascript:void 0;" class="hperlink" onclick="$('#err_td_<s:property value="id"/>').toggle()"><img alt="View/Hide Error" src="<s:property value="%{#request.CONTEXT}"/>/images/viewIcon.png" title="View Error Details." height="16"/></a>
				</s:else>
			</td>
			<td>
				<s:if test="success || fileName!=null">
				<s:url action="dlFile" namespace="/" var="dlFileURL">
					<s:param name="id" value="id"/>
				</s:url>
					<s:a cssClass="hyperlink" href="%{dlFileURL}"><img src="<s:property value="%{#request.CONTEXT}"/>/images/download.png" title="Download Report" alt="Download" height="15"></s:a>
				</s:if>
				<s:else>N/A</s:else>
			</td>
		</tr>
		<tr id="err_td_<s:property value="id"/>" class="<s:property value="%{#alternate}"/>" style="display:none;">
			<td colspan="7" class="errorMsg"> <s:property value="errorReason"/> </td>
		</tr>
		</s:iterator>
	</s:if><s:else>
		<tr><td align="center" colspan="7">
				No records found.
		</td>
		</tr>
	</s:else>
	<tr>
	<td colspan="7">
		<s:form id="ajaxExeLogFrm" action="ajaxExeLog" name="/report">
			<s:hidden name="pageStartIndex"/>
			<s:hidden name="pageTotalRecCount"/>
			<s:hidden name="requestId"/>
		</s:form>
		
		<s:include value="/WEB-INF/jsp/pagination.jsp">
			<s:param name="frmId">ajaxExeLogFrm</s:param>
			<s:param name="targetId">requestDtlsDlg</s:param>
			<s:param name="totalCount" value="pageTotalRecCount"></s:param>
		</s:include>
	</td>
	</tr>
	<tr>
		<td class="buttons" colspan="7">
			<s:url action="details" namespace="/report" var="reqDetailsURl">
				<s:param name="requestId" value="requestId"/>
			</s:url>
				<span><sj:submit value="Back" href="%{reqDetailsURl}" targets="requestDtlsDlg" cssClass="bbcomButton" onCompleteTopics="reuestCompleted"/></span>
		</td>
	</tr>	
	</tbody>
</table>


 
		