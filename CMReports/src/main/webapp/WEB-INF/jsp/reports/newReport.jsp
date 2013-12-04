<%@taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>
<div id="content">

<s:form id="frmNewRptID" name="frmNewRpt" action="newReport" method="post" namespace="/report">
<table style="width:100%;" >
	<tr>
		<td class="title" colspan="4">New Report</td>
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
				onCompleteTopics="reloadReportDropdown"
				onChangeTopics="reloadReportDropdown"
				/>
    	</td> 
    	<td class="label">Select Report<span class="required">*</span>:</td>
    	<td class="field">
    		<s:url action="json_fetchReportsForCarrier" namespace="/" var="jsonReport4CarriersURL"/>
			<sj:select
				cssStyle="width:170px"
				formIds="frmNewRptID"
				deferredLoading="true"
				href="%{jsonReport4CarriersURL}"
				id="reportSel"
				name="reqReport.report.reportId" 
				list="reports" 
				listKey="reportId" 
				listValue="displayName"
				emptyOption="false"
				reloadTopics="reloadReportDropdown"
				onChangeTopics="refreshSubjectLine,refreshOptions"
				onCompleteTopics="refreshSubjectLine,refreshOptions"
				/>
    		
    	</td>
	</tr>
	
	<tr id="reportDate_row">
		<td class="label"> Report Start Date<span class="required">*</span>:</td>
		<td class="field">
			<sj:datepicker 
				name="reqReport.startDate" 
				buttonImageOnly="true" 
				numberOfMonths="2" 
				changeMonth="true" 
				changeYear="true"
				buttonImage="%{#request.CONTEXT}/images/cal.png" 
				buttonText="Select a date..."
				displayFormat="dd-M-yy"/>
		</td>
		<td class="label"> Report End Date<span class="required">*</span>:</td>
		<td class="field">
			<sj:datepicker 
				name="reqReport.endDate" 
				buttonImageOnly="true" 
				numberOfMonths="2" 
				changeMonth="true" 
				buttonText="Select a date..."
				changeYear="true"
				buttonImage="%{#request.CONTEXT}/images/cal.png"  
				displayFormat="dd-M-yy"/>
		</td>
	</tr>
	
	<tr id="tax_row">
		<td class="label"> Conversion Rate / CRT % :</td>
		<td class="field">
			<s:textfield name="reqReport.currencyConversion" value="1"/>
		</td>
	</tr>
	
	
	<tr id="includeOpt_row">
		<td class="label"> Include CP Share<span class="required">*</span>:</td>
		<td class="field">
			<s:radio list="#{true:' Yes',false:' No' }" name="reqReport.includeCP"/>
		</td>
		<td class="label"> Include Bundle Items:</td>
		<td class="field">
			<s:radio list="#{true:' Yes',false:' No' }" name="reqReport.includeBundles"/> <i>(Virgin Mobile Jumptap Report.)</i>
		</td>
	</tr>
	
	
	
	<tr>
		<td class="title" colspan="4">Schedule Details</td>
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
				minDate="+0d" 
				changeMonth="true" 
				changeYear="true" 
				displayFormat="dd-M-yy" 
				timepicker="true" 
				timepickerAmPm="false"
				buttonImage="%{#request.CONTEXT}/images/cal.png" 
				buttonText="Select a date..."
				timepickerStepMinute="5"
				
				/>
		</td>
		<td class="label"> Schedule End Date:</td>
		<td>
			<sj:datepicker name="reqReport.expiryDate" 
				buttonImageOnly="true" 
				minDate="+1d" 
				changeMonth="true" 
				changeYear="true" 
				displayFormat="dd-M-yy" 
				timepicker="true" 
				timepickerAmPm="false" 
				buttonImage="%{#request.CONTEXT}/images/cal.png" 
				buttonText="Select a date..."
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
			<s:checkbox cssClass="checkbox"  name="reqReport.mail.attachment" fieldValue="true"/> Attach Report.
			<s:checkbox cssClass="checkbox"  name="reqReport.mail.includeFileLink" fieldValue="true"/> Include Download URL.
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
		<td class="field" colspan="3"> <s:textfield id="mailSubject" name="reqReport.mail.subject" size="110"/> </td>
	</tr>
	<tr>
		<td class="field" colspan="4"> <s:textarea name="reqReport.mail.body" rows="5"/></td>
	</tr>
	<tr>
		<td class="field" colspan="4" align="right"> <s:checkbox cssClass="checkbox"  name="reqReport.mail.includeDefaultFooter" fieldValue="true" value="true"/> Include Default Mail Footer.</td>
	</tr>
	
	<tr>
		<td class="buttons" colspan="4">
			<span>
				<img id="loadingbar" alt="Loading..." src="<s:property value="%{#request.CONTEXT}"/>/images/loading.gif" style="display:none">
				<sj:submit value="Send Request" indicator="loadingbar" targets="newReportResultDIVID" cssClass="bbcomButton" onCompleteTopics="reuestCompleted"/>
			</span>
		</td>
	</tr>
	</table>
	
</s:form>


<script type="text/javascript">
$.subscribe('reuestCompleted', function(event, data){
	$('#resultConfirmDlg').dialog('open');
});

$.subscribe('refreshSubjectLine', function(event,data) {
	var selCrObj = $('#carrierSel')[0];
	if(selCrObj.value!="") 
		$('#mailSubject')[0].value = selCrObj.options[selCrObj.selectedIndex].text;
	
	selRpObj = $('#reportSel')[0];
	if(selRpObj.value!="") {
		$('#mailSubject')[0].value += " - " + selRpObj.options[selRpObj.selectedIndex].text;
	}
	
});

$.subscribe('refreshOptions', function(event, data){
	var selRpObj = $('#reportSel')[0];
	if(selRpObj.value==3 || selRpObj.value==4) {  // live Items Report
		$('#reportDate_row').hide();
		$('#tax_row').hide();
		$('#includeOpt_row').hide();
	} else {
		$('#reportDate_row').show();
		$('#tax_row').show();
		$('#includeOpt_row').show();
	}
});

</script>

</div>