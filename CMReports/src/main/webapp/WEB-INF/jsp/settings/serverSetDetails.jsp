<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>

<s:form name="frmServerSettMaster" id="frmServerSettMasterID" action="st_saveServerSetting" namespace="/settings">
<s:actionerror cssClass="errorMsg"/>
<s:fielderror fieldName="mandateField" cssClass="errorMsg" id="errorMsgSSetId"/>
<table  style="width: 100%">
	<tr>
		<td class="title" >
		<s:if test="serverSetting.id != null">
			Edit Details for : <s:property value="serverSetting.id"/>
		</s:if><s:else>
			New Server Setting
		</s:else>
		</td>
	</tr>
	<tr>
		<td valign="top">
			<table style="width:100%;">
				<tr>
					<td class="label">ID:</td>
					<td class="field">
						<s:property value="serverSetting.id"/>
						<s:hidden name="serverSetting.id"/>
					</td>
				</tr>
				<tr>
					<td class="label">Key:</td>
					<td class="field">
						<s:textfield name="serverSetting.key" size="25"/>
					</td>
				</tr>
				<tr>
					<td class="label">Value:</td>
					<td class="field"><s:textarea name="serverSetting.value" cols="50" rows="3" /> </td>
				</tr>
				
				<tr>
					<td class="label">Status:</td>
					<td class="field"><s:radio list="#{true:' Enable', false:' Disable'}" name="serverSetting.enabled"></s:radio></td>
				</tr>
				<tr>
					<td class="label">Created Date:</td>
					<td class="field">
						<s:if test="serverSetting.createdDate!=null">
							<s:text name="format.date"><s:param name="value" value="serverSetting.createdDate"></s:param></s:text>
						</s:if>
					</td>
				</tr>
				<tr>
					<td class="label">Last Updated By:</td>
					<td class="field">
						<s:property value="serverSetting.updatedByName" default=" - "/>
					</td>
				</tr>
				<tr>
					<td class="label">Modified Date:</td>
					<td class="field">
						<s:if test="serverSetting.modifiedDate!=null">
						<s:text name="format.date"><s:param name="value" value="serverSetting.modifiedDate"></s:param></s:text>
						</s:if>
					</td>
				</tr>
			</table> 
		</td>
	</tr>
	<tr>
		<td class="buttons">
			<s:submit type="button" value="Back" cssClass="bbcomButton" onclick="reloadRpt();"/>
			<span><sj:submit value="Save" targets="settingTabContentDIV_ID" cssClass="bbcomButton"/></span>
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
	$('#masterTab').tabs("load",3);
	//window.location.href='<s:property value="%{#settingsURL}"/>';
}
</script>
