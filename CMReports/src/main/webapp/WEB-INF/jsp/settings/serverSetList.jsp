<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>
<div id="settingTabContentDIV_ID">
<s:actionerror cssClass="errorMsg"/>
<s:actionmessage cssClass="success"/>

<table class="dataGrid-table">
<thead>
	<tr>
		<th>ID</th>
		<th>Key</th>
		<th>Value</th>
		<th>Status</th>
		<th>Action</th>
	</tr>
</thead>
<s:if test="serverSettings!=null">
<tbody>
<s:iterator value="serverSettings" status="stat">
	<s:if test="#stat.odd">
		<s:set var="alternate">alternate</s:set>
	</s:if> <s:else>
		<s:set var="alternate" value="%{''}"/>
	</s:else>
	
	<tr class="<s:property value="#alternate"/> " >
		<td class="id"><s:property value="id"/> </td>
		<td><s:property value="key"/></td>
		<td style="width:40%"><s:property value="value" escapeHtml="true" /></td>
		<td class="status-<s:property value="enabled"/>" align="center">
			<s:if test="enabled"><img src="<s:property value="%{#request.CONTEXT}"/>/images/enable.png" alt="Enabled" height="18" ></s:if>
			<s:else><img src="<s:property value="%{#request.CONTEXT}"/>/images/disable.png" alt="Disabled" height="18" ></s:else>
		</td>
		<td align="center">
			<s:url action="st_serverDetails" namespace="/settings" var="settingDetailsURL">
				<s:param name="settingId" value="id"/>
			</s:url>
			<sj:a href="%{#settingDetailsURL}" targets="settingTabContentDIV_ID"> <img src="<s:property value="%{#request.CONTEXT}"/>/images/edit.png" alt="Edit" height="20" ></sj:a>
			&nbsp;
			<sj:a href="#" cssClass="hyperlink" openDialog="delSSConfirmationDlgID" onclick="$('#selectedSettingId')[0].value=%{id};"> <img src="<s:property value="%{#request.CONTEXT}"/>/images/delete.png" alt="Delete" height="18" ></sj:a>
		</td>
	</tr>	
</s:iterator>
	<tr>
	<td colspan="7">
	
		<s:form id="ajaxSettingFrm" action="st_server" name="/settings">
			<s:hidden name="pageStartIndex"/>
			<s:hidden name="pageTotalRecCount"/>
		</s:form>
		
		<s:include value="/WEB-INF/jsp/pagination.jsp">
			<s:param name="frmId">ajaxSettingFrm</s:param>
			<s:param name="targetId">settingTabContentDIV_ID</s:param>
			<s:param name="totalCount" value="pageTotalRecCount"></s:param>
		</s:include>
		
	</td>
	</tr>
</tbody>
</s:if>
<tfoot>
	<tr>
		<td class="" colspan="5">
			<s:url action="st_serverDetails" namespace="/settings" var="serverDetailsURL"></s:url>
			<span><sj:submit value="Add New" href="%{serverDetailsURL}" targets="settingTabContentDIV_ID" cssClass="bbcomButton"/></span>
		</td>
	</tr>
</tfoot>
</table>
<s:form name="delServerSettingForm" method="post" action="st_deleteServerSetting" namespace="/settings">
	<s:hidden name="settingId" id="selectedSettingId" value="0"/>
	<sj:submit value="Delete Me" listenTopics="deleteServerSettingConfirmed" targets="settingTabContentDIV_ID" cssStyle="visibility: hidden;"/>
</s:form>
</div>
