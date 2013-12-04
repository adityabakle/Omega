<%@taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>
<div id="content">
<%-- <div class="breadcrumb">
	<s:url action="home" namespace="/" var="homeURL"/>
	<s:a href="%{homeURL}" >MyDashboard</s:a>
	<span class="separator"></span>
</div> --%>

<s:actionmessage cssClass="success"/>

<s:form name="frmSqlRpt" action="sqlSchReport" method="post" namespace="/">
<table style="width:100%;" >
	<tr>
		<td class="title" colspan="4">SQL Executor</td>
	</tr>
	<tr>
		<td class="label">Select Carrier<span class="required">*</span>:</td>
		<td class="field"> 
			<s:url action="json_fetchCarriersForUser" namespace="/" var="jsonCarriersURL"/>
			<sj:select
				cssStyle="width:150px"
				href="%{jsonCarriersURL}"
				id="carrierSel"
				name="reqReport.carrier.carrierId" 
				list="carriers" 
				listKey="carrierId" 
				listValue="displayName"
				emptyOption="false"
				/>
			<s:hidden name="reqReport.report.name" value="sqlRpt"/>
    	</td> 
    	<td class="label">File Name<span class="required">*</span>:</td>
    	<td class="field">
    		<s:textfield name="reqReport.fileNamePrefix" size="25"/> (<b>*.csv</b> only)
    	</td>
	</tr>
	
	<tr>
		<td colspan="4" class="labelHead"><b>Query<span class="required">*</span>:</b></td>
	</tr>
	<tr>
		<td class="field" colspan="4">
			<s:textarea  rows="15" name="reqReport.sql" ></s:textarea>
		</td>
	</tr>
	<tr>
		<td colspan="4" class="labelHead"><b>CSV Header:</b></td>
	</tr>
	<tr>
		<td class="field" colspan="4">
			<s:textarea  rows="3" name="reqReport.csvHeader" ></s:textarea>
		</td>
	</tr>
	<tr>
		<td class="label"> Report Start Date:</td>
		<td>
			<sj:datepicker name="reqReport.startDate" 
				buttonImageOnly="true" 
				changeMonth="true" 
				changeYear="true"
				buttonImage="%{#request.CONTEXT}/images/cal.png" 
				buttonText="Select a date..." 
				displayFormat="dd-M-yy"/>
		</td>
		<td class="label"> Report End Date:</td>
		<td>
			<sj:datepicker name="reqReport.endDate" 
				buttonImageOnly="true" 
				changeMonth="true" 
				changeYear="true"
				buttonImage="%{#request.CONTEXT}/images/cal.png" 
				buttonText="Select a date..." 
				displayFormat="dd-M-yy"/>
		</td>
	</tr>
	<tr>
		<td class="label">Scheduling Option<span class="required">*</span>:</td>
		<td class="field" colspan="3">
			<s:url action="json_fetchSchedulingOptions" namespace="/" var="jsonSchOptionURL"/>
			<sj:radio href="%{jsonSchOptionURL}"
				list="schFrequencies"
				listKey="frequencyId"
				listValue="name"
				name="reqReport.frequency.frequencyId"				
			/>
		</td>
	</tr>
	<tr>
		<td class="label"> Schedule Start Date<span class="required">*</span>:</td>
		<td>
			<sj:datepicker name="reqReport.scheduledDate" 
				buttonImageOnly="true"
				changeMonth="true" 
				changeYear="true" 
				displayFormat="dd-M-yy" 
				timepicker="true" 
				buttonImage="%{#request.CONTEXT}/images/cal.png" 
				buttonText="Select a date..."
				timepickerAmPm="false" 
				timepickerStepMinute="5"/>
		</td>
		<td class="label"> Schedule End Date:</td>
		<td>
			<sj:datepicker name="reqReport.expiryDate" 
				buttonImageOnly="true" 
				changeMonth="true" 
				changeYear="true" 
				displayFormat="dd-M-yy" 
				timepicker="true" 
				buttonImage="%{#request.CONTEXT}/images/cal.png" 
				buttonText="Select a date..."
				timepickerAmPm="false" 
				timepickerStepMinute="5"/>
		</td>
	</tr>
	
	<tr>
		<td class="title" colspan="4">Mail Details</td>
	</tr>
	<tr>
		<td class="label"> Reply To<span class="required">*</span>:</td>
		<td class="field" colspan="3"> 
			<s:textfield name="reqReport.mail.replyToAddress" size="40" value="%{#session.loginUser.email}"/> 
			<s:checkbox name="reqReport.mail.attachment" fieldValue="true"/> Attach Report.
			<s:checkbox name="reqReport.mail.includeFileLink" fieldValue="true"/> Include Download URL.
		</td>
		
	</tr>
	<tr>
		<td class="label"> To<span class="required">*</span>:</td>
		<td class="field" colspan="3"> <s:textfield name="reqReport.mail.toAddress" size="100" value="%{#session.loginUser.email}"/> </td>
	</tr>
	<tr>
		<td class="label"> CC:</td>
		<td class="field" colspan="3"> <s:textfield name="reqReport.mail.ccAddress" size="100"/> </td>
	</tr>
	
	<tr>
		<td class="label"> Subject<span class="required">*</span>:</td>
		<td class="field" colspan="3"> <s:textfield name="reqReport.mail.subject" size="110"/> </td>
	</tr>
	<tr>
		<td class="field" colspan="4"> <s:textarea name="reqReport.mail.body" rows="5"/></td>
	</tr>
	<tr>
		<td class="field" colspan="4" align="right"> <s:checkbox name="reqReport.mail.includeDefaultFooter" fieldValue="true" value="true"/> Include Default Mail Footer.</td>
	</tr>
	
	<tr>
		<td class="buttons" colspan="4">
			<span>
				<img id="loadingbar" alt="Loading..." src="<s:property value="%{#request.CONTEXT}"/>/images/loading.gif" style="display:none">
				<sj:submit value="Send Request" indicator="loadingbar" targets="sqlExeResultDIVID" cssClass="bbcomButton" onCompleteTopics="reuestCompleted"/>
			</span>
		</td>
	</tr>
	</table>
	
</s:form>


<script type="text/javascript">
$.subscribe('reuestCompleted', function(event, data){
	$('#resultConfirmDlg').dialog('open');
});
</script>
<sj:dialog
	id="resultConfirmDlg"
	modal="true"
	autoOpen="false"
	title="SQL Report Request!"
	closeOnEscape="false"
	draggable="false"
	resizable="false"
	width="350"
	cssStyle="display:none"	
	buttons="{
			'Ok':function() { $('#resultConfirmDlg').dialog('close'); }
			}"
>
	<br>
	<div id="sqlExeResultDIVID" style="margin-left: 10px"></div>
</sj:dialog>

<sj:dialog id="helpDlg"
	modal="true"
	closeOnEscape="true" 
    autoOpen="false"
    cssStyle="display:none"
    draggable="false"
    resizable="false"
    title="Scheduling Options."
    buttons="{
              'Close':function() { $('#helpDlg').dialog('close'); } 
              }"
     width="350">
	<div id='helpDiv' 
		style="padding:5 5 5 5;background:#E7E7E7;">
	<p><b>Execute Once:</b> Will run the requested report once for the given date and time.</p>
	<p><b>Daily:</b> Will run the requested report daily at the given time, Starting from the specified date.</p>
	<p><b>Weekly:</b> Will run the requested report every day of the week, based on the selected date at specified time.</p>
	<p><b>Monthly:</b> Will run the report every month on the date and time specified. Make sure the date is not beyond 28th.</p>
	<p><b>MTD:</b> Will run the report daily from the date and time specified. Report is usually executed for a given Billing cycle.</p>
	</div>
</sj:dialog>
</div>