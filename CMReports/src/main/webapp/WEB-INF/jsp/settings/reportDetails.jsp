<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>

<s:form name="frmReportMaster" id="frmReportMasterID" action="st_saveReport" namespace="/settings">
<s:actionerror cssClass="errorMsg"/>
<s:fielderror fieldName="mandateField" cssClass="errorMsg" id="errorMsgRptId"/>
<table  style="width: 100%">
	<tr>
		<td class="title" >
		Edit Details for : <s:property value="report.displayName"/> <img id="detailRLoadingbar" alt="Loading..." src="<s:property value="%{#request.CONTEXT}"/>/images/loading.gif" style="display:none">
		</td>
	</tr>
	<tr>
		<td valign="top">
			<table style="width:100%;">
				<tr>
					<td class="label">ID:</td>
					<td class="field">
						<s:property value="report.reportId"/>
						<s:hidden name="report.reportId"/>
					</td>
				</tr>
				<tr>
					<td class="label">Name:</td>
					<td class="field">
						<s:textfield name="report.name" size="25"/>
					</td>
				</tr>
				<tr>
					<td class="label">Display Name:</td>
					<td class="field">
						<s:textfield name="report.displayName" size="30"/>
					</td>
				</tr>
				<tr>
					<td class="label">File Name Prefix:</td>
					<td class="field"><s:textfield name="report.fileNamePrefix" size="25"/> </td>
				</tr>
				
				<tr>
					<td class="label">DB API Name:</td>
					<td class="field"><s:textfield name="report.dbServiceApiName" size="30"/> </td>
				</tr>
				
				<tr>
					<td class="label">XLS API Name:</td>
					<td class="field"><s:textfield name="report.xlsApiName" size="30"/> </td>
				</tr>
				
				<tr>
					<td class="label">File Extension:</td>
					<td class="field">
					<s:url action="json_fetchFileExtensions" namespace="/" var="jsonRolesURL"/>
					<sj:select href="%{jsonRolesURL}"
						list="fileExtensions"
						name="report.fileExtension.fileExtensionId"
						listKey="fileExtensionId"
						listValue="name"
						emptyOption="false"
					/>
					</td>
				</tr>
				<tr>
					<td class="label">CSV Header:</td>
					<td class="field"><s:textarea name="report.csvHeader" cols="50" rows="3" /> </td>
				</tr>
				
				<tr>
					<td class="label">Status:</td>
					<td class="field"><s:radio list="#{true:' Enable', false:' Disable'}" name="report.enabled"></s:radio></td>
				</tr>
				<tr>
					<td class="label">Created Date:</td>
					<td class="field">
						<s:if test="report.createdDate!=null">
							<s:text name="format.date"><s:param name="value" value="report.createdDate"></s:param></s:text>
						</s:if>
					</td>
				</tr>
				<tr>
					<td class="label">Last Updated By:</td>
					<td class="field">
						<s:property value="report.updatedByName" default=" - "/>
					</td>
				</tr>
				<tr>
					<td class="label">Modified Date:</td>
					<td class="field">
						<s:if test="report.modifiedDate!=null">
						<s:text name="format.date"><s:param name="value" value="report.modifiedDate"></s:param></s:text>
						</s:if>
					</td>
				</tr>
			</table> 
		</td>
	</tr>
	<tr>
		<td class="buttons">
			<s:if test="report.reportId != null">
				<s:submit type="button" value="New Report" cssClass="bbcomButton" onclick="reloadRpt();"/>
			</s:if>
			<span><sj:submit value="Save" targets="reportDetailsTarget" cssClass="bbcomButton"/></span>
		</td>
	</tr>
</table>
</s:form>
<s:url action="home" namespace="/settings" var="settingsURL">
	<s:param name="selectedTab" value="%{selectedTab}"/>
</s:url>
<s:if test="reloadList">

<script>
	alert("<s:property value="alertMessage"/>");
	reloadRpt();
</script>
</s:if>
<script>
function reloadRpt(){
	$('#masterTab').tabs("load",2);
	//window.location.href='<s:property value="%{#settingsURL}"/>';
}
</script>
