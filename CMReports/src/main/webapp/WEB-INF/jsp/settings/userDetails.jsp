<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>
<s:form name="frmUserMaster" action="st_saveUser" namespace="/settings">
<s:actionmessage cssClass="success"/>
<s:fielderror cssClass="errorMsg"/>
<s:actionerror cssClass="errorMsg"/>
<table  style="width: 100%">
	<tr>
		<td class="title" >
		Edit Details for : <s:property value="user.name"/> <img id="detailLoadingbar" alt="Loading..." src="<s:property value="%{#request.CONTEXT}"/>/images/loading.gif" style="display:none">
		</td>
	</tr>
	<tr>
		<td valign="top">
			<table style="width:100%;">
				<tr>
					<td class="label">ID:</td>
					<td class="field">
						<s:property value="user.userId"/>
						<s:hidden name="user.userId"/>
					</td>
					<td rowspan="14" valign="top">
					<s:if test="carriers!=null">
						 <table class="checkboxTable">
						 	<s:checkboxlist tabindex="1" cssStyle="vertical" name="user.userCarrierXref.carrierId" value="user.userCarrierXref.{carrierId}"  list="carriers" theme="cmreport" listKey="carrierId" listValue="displayName"/>
						</table>
					</s:if><s:else>
						<label>No carriers Defined.</label>
					</s:else>
					</td>
				</tr>
				<tr>
					<td class="label">User Name:</td>
					<td class="field">
						<s:if test="user.userId!=null">
							<s:hidden name="user.userName"/>
							<s:property value="user.userName"/>
						</s:if><s:else>
							<s:textfield name="user.userName" size="20"/>
						</s:else>
					</td>
				</tr>
				<tr>
					<td class="label">Password:</td>
					<td class="field"><s:password name="user.password" size="20"/> </td>
				</tr>
				<s:if test="user==null || user.userId==null">
					<tr>
						<td class="label">Confirm Password:</td>
						<td class="field"><s:password name="user.confirmPassword" size="20"/> </td>
					</tr>
				</s:if>
				<tr>
					<td class="label">Name:</td>
					<td class="field">
						<s:textfield name="user.name" size="35"/>
					</td>
				</tr>
				<tr>
					<td class="label">Email:</td>
					<td class="field"><s:textfield name="user.email" size="35"/> </td>
				</tr>
				<tr>
					<td class="label">Role:</td>
					<td class="field">
					<s:url action="json_fetchRoles" namespace="/" var="jsonRolesURL"/>
					<sj:select href="%{jsonRolesURL}"
						list="roles"
						name="user.roleId"
						listKey="roleId"
						listValue="name"
						emptyOption="false"
					/>
					</td>
				</tr>
				<tr>
					<td class="label">Status:</td>
					<td class="field"><s:radio list="#{true:' Enable', false:' Disable'}" name="user.enabled"></s:radio></td>
				</tr>
				<tr>
					<td class="label">Account:</td>
					<td class="field"><s:radio list="#{true:' Locked', false:' Unlocked'}" name="user.accountLocked"></s:radio></td>
				</tr>
				<tr>
					<td class="label">Login Attempts:</td>
					<td class="field">
						<s:property value="user.loginAttempts"/>
					</td>
				</tr>
				<tr>
					<td class="label">Last Login:</td>
					<td class="field">
						<s:if test="user.lastLoginDate!=null">
							<s:text name="format.date"><s:param name="value" value="user.lastLoginDate"></s:param></s:text>
						</s:if> <s:else> N/A </s:else>
					</td>
				</tr>
				<tr>
					<td class="label">Created Date:</td>
					<td class="field">
						<s:if test="user.createdDate!=null">
						<s:text name="format.date"><s:param name="value" value="user.createdDate"></s:param></s:text>
						</s:if>
					</td>
				</tr>
				<tr>
					<td class="label">Last Updated By:</td>
					<td class="field">
						<s:property value="user.updatedByName" default=" - "/>
					</td>
				</tr>
				<tr>
					<td class="label">Modified Date:</td>
					<td class="field">
						<s:if test="user.modifiedDate!=null">
						<s:text name="format.date"><s:param name="value" value="user.modifiedDate"></s:param></s:text>
						</s:if>
					</td>
				</tr>
			</table> 
		</td>
	</tr>
	<tr>
		<td class="buttons">
			<s:submit type="button" value="New User" cssClass="bbcomButton" onclick="reloadUsr();"/>
			<span>
				<img id="loadingbar" alt="Loading..." src="<s:property value="%{#request.CONTEXT}"/>/images/loading.gif" style="display:none">
				<s:if test="user.userId!=null && user.lastLoginDate==null">
					<s:url action="st_sendInvite" namespace="/settings" var="inviteUserURL">
						<s:param name="userId" value="user.userId"/>
					</s:url>
					<sj:submit value="Invite" href="%{inviteUserURL}" indicator="loadingbar" targets="userDetailsTarget" cssClass="bbcomButton"/>
				</s:if>
				<sj:submit value="Save" indicator="loadingbar" targets="userDetailsTarget" cssClass="bbcomButton"/>
			</span>
		</td>
	</tr>
</table>
</s:form>
<s:url action="home" namespace="/settings" var="settingsURL">
	<s:param name="selectedTab" value="%{selectedTab}"/>
</s:url>

<script>
function reloadUsr(){
	$('#masterTab').tabs("load",0);
	//window.location.href='<s:property value="%{#settingsURL}"/>';
}
</script>
<s:if test="reloadList">
<script>
	alert("<s:property value="alertMessage"/>");
	reloadUsr();
</script>
</s:if>