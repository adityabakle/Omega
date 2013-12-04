<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<sj:dialog
 	id="requestDetailsDLG"
 	title="Report Request Details"
 	resizable="false"
 	autoOpen="false"
 	modal="true"
 	cssStyle="display:none"
 	closeOnEscape="true" 	
 	width="650"
 	draggable="true"
 	height="500"
 />
 
 <sj:dialog
	id="unscheduleConfirmationDlgID"
	autoOpen="false"
	modal="true"
	cssStyle="display:none"
	buttons="{
		'OK':function(){unscheduleOk();},
		'Cancel':function(){unscheduleCancel();}
	}">
	
	<p>Are You sure you want to delete this Request ?</p>
	<p>Note: <i>Doing so you will no longer receive report for this request.</i></p> 
</sj:dialog>

<s:url action="chnagePassword" namespace="/my" var="changeMyPasswordURL"/>
<sj:dialog
		id="DLG_ChangePassword_Profile_ID"
		autoOpen="false"
		cssStyle="display:none"
		closeOnEscape="true"
		draggable="false"
		resizable="false"
		href="%{#changeMyPasswordURL}"
		modal="true"
		loadingText="Verifying..."
		width="400"
		height="250"
		title="Change Password"
	/>
<script>
	function unscheduleCancel(){
		$('#unscheduleConfirmationDlgID').dialog('close');
	}
	function unscheduleOk(){
		$.publish("dlgUnscheduleConfirmed");
		$('#unscheduleConfirmationDlgID').dialog('close');	
	}
</script>

<sj:dialog
	id="resultConfirmDlg"
	modal="true"
	autoOpen="false"
	title="New Report Request"
	closeOnEscape="false"
	draggable="true"
	resizable="false"
	width="400"
	cssStyle="display:none"	
	buttons="{
			'Ok':function() { $('#resultConfirmDlg').dialog('close'); }
			}"
>
	<br>
	<div id="newReportResultDIVID"></div>
</sj:dialog>