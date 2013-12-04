<%@taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>
<div id="requestDtlsDlg">
<s:form id="frmUpdSqlRpt" name="frmUpdSqlRpt" action="updateSql" method="post" namespace="/report">
<table style="width:100%;">
	<tr>
		<td class="title" colspan="2">SQL for Request #: <s:property value="reqReport.requestId"/></td>
		
	</tr>
	<tr>
		<td class="field" colspan="2"> <s:textarea name="reqReport.sql" rows="18"/></td>
	</tr>
	<tr>
		<td class="title" colspan="2">CSV Header: </td>
	</tr>
	<tr>
		<td class="field" colspan="2"> <s:textarea name="reqReport.csvHeader" rows="3"/></td>
	</tr>
	
	<tr>
		<td class="buttons" colspan="4">
			<s:hidden  name="requestId" value="%{reqReport.requestId}"/>
			<s:hidden  name="reqReport.requestId"/>
			
			<s:url action="details" namespace="/report" var="requestDetailsUrl">
				<s:param name="requestId" value="reqReport.requestId"/>
			</s:url>
			<sj:submit href="%{requestDetailsUrl}" value="Back" targets="requestDtlsDlg" cssClass="bbcomButton" onCompleteTopics="reuestCompleted"/>
			
			<s:if test="%{(#session.loginUser.role.roleId == 1 || #session.loginUser.userId == reqReport.requestedBy.userId) && reqReport.state!='expired'}">
				<span><sj:submit value="Save" targets="requestDtlsDlg" cssClass="bbcomButton" onCompleteTopics="reuestCompleted"/></span>
			</s:if>
		</td>
	</tr>
	</table>
	
</s:form>
</div>