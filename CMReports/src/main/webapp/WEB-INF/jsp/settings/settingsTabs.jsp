<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>
<div id="content">
<sj:tabbedpanel id="masterTab" selectedTab="%{selectedTab}" animate="true" cssClass="bodyTxt" cssStyle="width:100%"  >
	<s:url action="st_user" namespace="/settings" var="usrTabUrl"/>
	<s:url action="st_carrier" namespace="/settings" var="carrierTabUrl"/>
	<s:url action="st_report" namespace="/settings" var="reportTabUrl"/>
	<s:url action="st_server" namespace="/settings" var="servetSettingTabUrl"/>
	<s:url action="st_schedulerDetails" namespace="/settings" var="schedulerDetails"/>
	<s:url action="st_staticValues" namespace="/settings" var="staticValues"/>
	<s:url action="dbConCheck" namespace="/settings" var="dbConCheck"/>
	
	<sj:tab id="usrTab" href="%{usrTabUrl}" label="User Master" ></sj:tab>
	<sj:tab id="carrierTab" href="%{carrierTabUrl}" label="Carrier Master"></sj:tab>
	<sj:tab id="reportTab" href="%{reportTabUrl}" label="Report Master"  listenTopics="reloadReportList"></sj:tab>
	<sj:tab id="serverTab" href="%{servetSettingTabUrl}" label="Server Settings"></sj:tab>
	<sj:tab id="schedulerDetails" href="%{schedulerDetails}" label="Scheduler"></sj:tab>
	<sj:tab id="staticValues" href="%{staticValues}" label="Static Values"></sj:tab>
	<sj:tab id="dbConCheck" href="%{dbConCheck}" label="DB Connection Check"></sj:tab>
	
</sj:tabbedpanel>
<sj:dialog
	id="delSSConfirmationDlgID"
	autoOpen="false"
	cssStyle="display:none"
	buttons="{
		'OK':function(){delSSOk();},
		'Cancel':function(){delSSCancel();}
	}"
	
>
	Are You sure you want to delete this setting ?
</sj:dialog>
<script>
	function delSSCancel(){
		$('#delSSConfirmationDlgID').dialog('close');
	}
	function delSSOk(){
		$.publish('deleteServerSettingConfirmed');
		$('#delSSConfirmationDlgID').dialog('close');
		
	}
</script>
</div>