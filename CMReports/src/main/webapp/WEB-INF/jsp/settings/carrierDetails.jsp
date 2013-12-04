<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>

<s:form name="frmCarrierMaster" id="frmCarrierMasterID" action="st_saveCarrier" namespace="/settings">
<s:actionerror cssClass="errorMsg"/>
<s:fielderror fieldName="mandateField" cssClass="errorMsg" id="errorMsgCarId"/>
<table  style="width: 100%">
	<tr>
		<td class="title" >
		Edit Details for : <s:property value="carrier.displayName"/> <img id="detailCLoadingbar" alt="Loading..." src="<s:property value="%{#request.CONTEXT}"/>/images/loading.gif" style="display:none">
		</td>
	</tr>
	<tr>
		<td valign="top">
			<table style="width:100%;">
				<tr>
					<td class="label">ID:</td>
					<td class="field">
						<s:property value="carrier.carrierId"/>
						<s:hidden name="carrier.carrierId"/>
					</td>
					<td class="field">
						<b>Select Reports:</b>
					</td>
				</tr>
				
				<tr>
					<td class="label">Name:</td>
					<td class="field">
						<s:textfield name="carrier.name" size="15"/>
					</td>
					<td rowspan="15" valign="top">
						<s:if test="%{reports!=null}">
						<table class="checkboxTable">
							<s:checkboxlist tabindex="1" cssStyle="vertical" name="carrier.carrierReportXref.reportId" value="carrier.carrierReportXref.{reportId}"  list="reports" theme="cmreport" listKey="reportId" listValue="displayName"/>
						</table>
						</s:if>
					</td>
				</tr>
				<tr>
					<td class="label">Display Name:</td>
					<td class="field">
						<s:textfield name="carrier.displayName" size="35"/>
					</td>
				</tr>
				<tr>
					<td class="label">Status:</td>
					<td class="field"><s:radio list="#{true:' Enable', false:' Disable'}" name="carrier.enabled"></s:radio></td>
				</tr>
				<tr>
					<td class="label">Tax Rate (%):</td>
					<td class="field"><s:textfield name="carrier.taxRate" size="5"/> </td>
				</tr>
				<tr>
					<td class="label">Currency Code:</td>
					<td class="field"><s:textfield name="carrier.currencyCode" size="5" /> </td>
				</tr>
				<tr>
					<td class="label">Time Zone:</td>
					<td class="field"><s:textfield name="carrier.carrierTimeZone" size="35" /> </td>
				</tr>
				<tr>
					<td class="label">Config File:</td>
					<td class="field"><s:textfield name="carrier.sqlMapFile" size="35" /> </td>
				</tr>
				<tr>
					<td class="label">Mapper Namespace:</td>
					<td class="field"><s:textfield name="carrier.sqlMapperNamespace" size="35" /> </td>
				</tr>
				<tr>
					<td class="label">Xls Class Name:</td>
					<td class="field"><s:textfield name="carrier.xlsClassName" size="35" /> </td>
				</tr>
				<tr>
					<td class="label">TNS File:</td>
					<td class="field"><s:textfield name="carrier.tnsFile" size="35"/> </td>
				</tr>
				<tr>
					<td class="label">DB Connection:</td>
					<td class="field"><s:radio list="#{true:' TNS', false:' JDBC Url'}" name="carrier.tnsLookup"></s:radio></td>
				</tr>
				<tr>
					<td class="label">TNS Name:</td>
					<td class="field"><s:textfield name="carrier.dbTnsName" size="30"/> </td>
				</tr>
				<tr>
					<td class="label">DB Server Name:</td>
					<td class="field"><s:textfield name="carrier.dbServerName" size="30"/> </td>
				</tr>
				<tr>
					<td class="label">DB ServiceId</td>
					<td class="field"><s:textfield name="carrier.dbServiceId" size="30"/> </td>
				</tr>
				<tr>
					<td class="label">DB Port</td>
					<td class="field"><s:textfield name="carrier.dbPort" size="10"/> </td>
				</tr>
				<tr>
					<td class="label">DB User</td>
					<td class="field"><s:textfield name="carrier.dbUserName" size="30"/> </td>
				</tr>
				<tr>
					<td class="label">DB Password</td>
					<td class="field"><s:textfield name="carrier.dbPassword" size="30"/> </td>
				</tr>
				<tr>
					<td class="label">Created Date:</td>
					<td class="field">
						<s:if test="carrier.createdDate!=null">
							<s:text name="format.date"><s:param name="value" value="carrier.createdDate"></s:param></s:text>
						</s:if>
					</td>
				</tr>
				<tr>
					<td class="label">Last Updated By:</td>
					<td class="field">
						<s:property value="carrier.updatedByName" default=" - "/>
					</td>
				</tr>
				<tr>
					<td class="label">Modified Date:</td>
					<td class="field">
						<s:if test="carrier.modifiedDate!=null">
						<s:text name="format.date"><s:param name="value" value="carrier.modifiedDate"></s:param></s:text>
						</s:if>
					</td>
				</tr>
			</table> 
		</td>
	</tr>
	<tr>
		<td class="buttons">
			<s:if test="carrier.carrierId != null">
				<s:submit type="button" value="New Carrier" cssClass="bbcomButton" onclick="reloadCarrier();"/>
			</s:if>
			<span>
				<img id="loadingbar" alt="Loading..." src="<s:property value="%{#request.CONTEXT}"/>/images/loading.gif" style="display:none">
				<sj:submit value="Save" targets="carrierDetailsTarget" indicator="loadingbar" cssClass="bbcomButton"/>
			</span>
		</td>
	</tr>
</table>
</s:form>
<s:url action="home" namespace="/settings" var="settingsURL">
	<s:param name="selectedTab" value="%{selectedTab}"/>
</s:url>

<script>
function reloadCarrier(){
	$('#masterTab').tabs("load",1);
	//window.location.href='<s:property value="%{#settingsURL}"/>';
}
</script>
<s:if test="reloadList">
<script>
	alert("<s:property value="alertMessage"/>");
	reloadCarrier();
</script>
</s:if>
