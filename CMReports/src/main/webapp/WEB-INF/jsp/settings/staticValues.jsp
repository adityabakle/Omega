<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>
<div id="staticValues_DIV">
<s:form name="frmStaticValues" id="frmStaticValuesID" action="st_reloadStaticValues" namespace="/settings">
<s:actionerror cssClass="errorMsg"/>
<s:actionmessage cssClass="success"/>

<s:fielderror fieldName="mandateField" cssClass="errorMsg" id="errorMsgSSetId"/>
<table  style="width: 100%">
	<tr>
		<td class="title" >
			Reload Static Values
		</td>
	</tr>
	<tr>
		<td valign="top">
		<label>Changes made Server Settings are not applied immediately. If you need, then reload the application static values.</label><br/>
		<label>Do you want to reload all static values?</label>
		</td>
	</tr>
	<tr>
		<td class="buttons">
			<span>
			<img id="stLoadingbar" alt="Loading..." src="<s:property value="%{#request.CONTEXT}"/>/images/loading.gif" style="display:none">
			<sj:submit targets="staticValues_DIV" value="Yes" indicator="stLoadingbar" cssClass="bbcomButton"/>
			</span>
			<s:hidden name="selectedTab"/>
		</td>
	</tr>
</table>
</s:form>
</div>
