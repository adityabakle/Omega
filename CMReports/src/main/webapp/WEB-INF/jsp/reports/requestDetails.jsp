<%@taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>
<div id="requestDtlsDlg">
<s:actionerror cssClass="errorMsg"/>
<s:actionmessage cssClass="success"/>
<s:form id="frmNewRptID" name="frmNewRpt" action="updateReport" method="post" namespace="/report">
<table style="width:100%;">
	<tr>
		<td class="title" colspan="4">Request Details</td>
		
	</tr>
	<tr>
		<td class="label">Request ID:</td>
		<td class="field">
			<s:property value="reqReport.requestId"/>
			<s:hidden  name="requestId" value="%{reqReport.requestId}"/>
			<s:hidden  name="reqReport.requestId"/>
		</td>
		<td class="label">Requested By:</td>
		<td class="field"><s:property value="reqReport.requestedBy.name"/></td>
	</tr>
	
	<tr>
		<td class="label">Carrier:</td>
		<td class="field"><s:property value="reqReport.carrier.displayName"/></td>
		<td class="label">Report:</td>
    	<td class="field"><s:property value="reqReport.report.displayName"/></td>
	</tr>
	
	<tr>
	<td class="label"> Include CP:</td>
		<td class="field">
			<s:if test="reqReport.includeCP">Yes</s:if><s:else>No</s:else>
		</td>
		<td class="label">Initial Dates:</td>
		<td class="field">
			<s:if test="reqReport.initialStartDate!=null">
				<s:date name="reqReport.initialStartDate" format="dd-MMM-yyyy"/> to <s:date name="reqReport.initialEndDate" format="dd-MMM-yyyy"/>
			</s:if><s:else>N/A</s:else>
		</td>
		
	</tr>
	
	<tr>
	<td class="label"> Include Bundle:</td>
		<td class="field">
			<s:if test="reqReport.includeBundles">Yes</s:if><s:else>No</s:else>
		</td>
		<td class="label">Current Dates:</td>
		<td class="field">
			<s:if test="reqReport.startDate!=null">
				<s:date name="reqReport.startDate" format="dd-MMM-yyyy"/> to <s:date name="reqReport.endDate" format="dd-MMM-yyyy"/>
			</s:if><s:else>N/A</s:else>
		</td>
		
		
	</tr>
	<tr>
		<td class="label"> Conversion Rate/CRT%:</td>
		<td class="field">
			<s:property value="reqReport.currencyConversion" default="N/A"/>
		</td>
		<s:if test="'sqlRpt'==reqReport.report.name">
		<td colspan="2" align="center">
			<s:url action="viewSql" namespace="/report" var="viewSQLURL">
				<s:param name="requestId" value="reqReport.requestId"/>
			</s:url>
			<sj:a href="%{viewSQLURL}" cssClass="hyperlink" targets="requestDtlsDlg">View Query</sj:a>
		</td>
		</s:if><s:else>
			<td class="label">Updated By:</td>
			<td class="field"> <s:property value="reqReport.updatedByName"/></td>
		</s:else>
	</tr>
	
	<tr><td class="title" colspan="4">Schedule Details</td></tr>
	<tr>
		<td class="label"> Start Date:</td>
		<td class="field">
			<s:date name="reqReport.scheduledDate" format="dd-MMM-yyyy HH:mm z"/>
		</td>
		<td class="label">Frequency:</td>
		<td class="field">
			<s:property	value="reqReport.frequency.name"/>
		</td>
		
		
	
	</tr>
	
	<tr>
		<td class="label"> Expire Date:</td>
		<td class="field">
			<s:if test="reqReport.expiryDate!=null">
				<s:date name="reqReport.expiryDate" format="dd-MMM-yyyy HH:mm z"  />
			</s:if><s:else>
				Never
			</s:else>
		</td>
		<td class="label"> Last Executed:</td>
		<td class="field"><s:if test="reqReport.lastExecutedDate==null"> Not Yet</s:if>
			<s:else>
				<s:date name="reqReport.lastExecutedDate" format="dd-MMM-yyyy HH:mm" timezone="%{#session.loginUser_timeZone_Id}" nice="false"/>
			</s:else>
		</td>
	</tr>
	
	
	<tr>
		<td class="title" colspan="4">Mail Details
		<s:hidden name="reqReport.mail.mailId"/>
		</td>
	</tr>
	<tr>
		<td class="label"> Reply To<span class="required">*</span>:</td>
		<td class="field" colspan="3"> 
			<s:textfield name="reqReport.mail.replyToAddress" size="40"/> 
			
		</td>
		
	</tr>
	<tr>
		<td class="label"> To<span class="required">*</span>:</td>
		<td class="field" colspan="3"> <s:textfield name="reqReport.mail.toAddress" size="50" /> </td>
	</tr>
	<tr>
		<td class="label"> CC:</td>
		<td class="field" colspan="3"> <s:textfield name="reqReport.mail.ccAddress" size="50"/> </td>
	</tr>
	
	<tr>
		<td class="label"> Subject<span class="required">*</span>:</td>
		<td class="field" colspan="3"> <s:textfield id="mailSubject" name="reqReport.mail.subject" size="60"/> </td>
	</tr>
	<tr>
		<td class="field" colspan="4"> <s:textarea name="reqReport.mail.body" rows="4"/></td>
	</tr>
	<tr>
		<td class="field" colspan="4" align="right"> 
		<s:checkbox name="reqReport.mail.includeDefaultFooter" fieldValue="true" value="true"/>Default Mail Footer.
		<s:checkbox name="reqReport.mail.attachment" fieldValue="true"/> Attach Report.
		<s:checkbox name="reqReport.mail.includeFileLink" fieldValue="true"/> Download URL.
		</td>
	</tr>
	
	<tr>
		<td class="buttons" colspan="4">
			<s:url action="ajaxExeLog" namespace="/report" var="exeLogURL">
				<s:param name="requestId" value="reqReport.requestId"/>
			</s:url>
			<sj:a href="%{exeLogURL}" cssClass="hyperlink" indicator="loadingbar" targets="requestDtlsDlg" >View Past Executions</sj:a>
			<span>
				<img id="loadingbar" alt="Loading..." src="<s:property value="%{#request.CONTEXT}"/>/images/loading.gif" style="display:none">
				<s:if test="%{(#session.loginUser.role.roleId == 1 || #session.loginUser.userId == reqReport.requestedBy.userId) && reqReport.state!='expired'}">
					<sj:submit value="Update Mail Details" indicator="loadingbar" targets="requestDtlsDlg" cssClass="bbcomButton" onCompleteTopics="reuestCompleted"/>
				</s:if>
			</span>
			
		</td>
	</tr>
	</table>
	
</s:form>
</div>