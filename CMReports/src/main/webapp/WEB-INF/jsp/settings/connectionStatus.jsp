<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>

<div id="connectionStatus_DIV">
<s:form name="frmConnectionStatus" id="frmConnectionStatus" action="dbConCheck" namespace="/settings">
<s:actionerror cssClass="errorMsg"/>
<s:actionmessage cssClass="success"/>

<s:fielderror fieldName="mandateField" cssClass="errorMsg" id="errorMsgSSetId"/>
<table  style="width: 100%">
	<tr>
		<td class="title" colspan="2">
			Carrier DB Connection Status 
		</td>
	</tr>
	<tr>
		<td class="label">Carrier:<br/></td>
		<td class="field">
			<s:url action="json_fetchCarriersForUser" namespace="/" var="jsonCarriersURL"/>
			<sj:select
				cssStyle="width:150px"
				href="%{jsonCarriersURL}"
				id="carrierSel"
				name="carrierId" 
				list="carriers" 
				listKey="carrierId" 
				listValue="displayName"
				emptyOption="false"
				onCompleteTopics="reloadReportDropdown"
				onChangeTopics="reloadReportDropdown"
				/>
		</td>
	</tr>
	<s:if test="%{crDto != null}">
		<tr>
			<td class="label">Connection Type : </td>
			<td class="field">
				<s:if test="%{crDto.tnsLookup}"> TNS Name Lookup</s:if>
				<s:else> JDBC Url Connection</s:else>
			</td>
		</tr>
	</s:if>
	<tr>
		<td class="label">Status:</td>
		<td class="field"><s:fielderror fieldName="conCheckStatus" /> </td>
	</tr>
	<tr>
		<td class="buttons" colspan="2">
			<span>
			<img id="stLoadingbar" alt="Loading..." src="<s:property value="%{#request.CONTEXT}"/>/images/loading.gif" style="display:none">
			<sj:submit targets="connectionStatus_DIV" value="Check" indicator="stLoadingbar" cssClass="bbcomButton"/>
			</span>
		</td>
	</tr>
</table>
</s:form>
</div>
