<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/struts-jquery-tags" prefix="sj"%>
<div id="content" >
<table style="width: 100%;" >
	<tr>
		<td colspan="3" class="title">User Information</td>
	</tr>
	<tr>
		<td class="label">UserName:</td>
		<td class="field"><s:property value="userDto.userName"/> </td>
		<td class="field"><b>Allowed Carriers</b> </td>
	</tr>
	<tr>
		<td class="label">Full Name:</td>
		<td class="field"><s:property value="userDto.name"/></td>
		<td rowspan="10" valign="top">
			<table class="checkboxTable">
				<s:iterator value="carriers">
				<tr><td class="field"><s:property value="displayName"/> </td></tr>
				</s:iterator>
			</table>
			
		</td>
	</tr>
	<tr>
		<td class="label">Email:</td>
		<td class="field"><s:property value="userDto.email"/></td>
	</tr>
	<tr>
		<td class="label">Role:</td>
		<td class="field"><s:property value="userDto.role.description"/></td>
	</tr>
	<tr>
		<td class="label">Created:</td>
		<td class="field"><s:property value="userDto.name"/></td>
	</tr>
	<tr>
		<td class="label">Last Login:</td>
		<td class="field">
			<s:if test="userDto.lastLoginDate!=null">
				<s:text name="format.date"><s:param name="value" value="userDto.lastLoginDate"></s:param></s:text>
			</s:if> <s:else> N/A </s:else>
		</td>
	</tr>
	<tr>
		<td class="label">Created Date:</td>
		<td class="field">
			<s:if test="userDto.createdDate!=null">
			<s:text name="format.date"><s:param name="value" value="userDto.createdDate"></s:param></s:text>
			</s:if>
		</td>
	</tr>
	<tr>
		<td class="label">Last Updated By:</td>
		<td class="field">
			<s:property value="userDto.updatedByName" default=" - "/>
		</td>
	</tr>
	<tr>
		<td class="label">Modified Date:</td>
		<td class="field">
			<s:if test="userDto.modifiedDate!=null">
			<s:text name="format.date"><s:param name="value" value="userDto.modifiedDate"></s:param></s:text>
			</s:if>
		</td>
	</tr>
	<tr>
		<td class="label"># of Scheduled Request:</td>
		<td class="field">
			<s:property value="userDto.scheduledRequest" default=" - "/>
			<s:url action="jobs" namespace="/my" var="schJobURL">
				<s:param name="selectedTab" value="2"></s:param>
			</s:url>
			<s:a href="%{schJobURL}">View</s:a>
		</td>
	</tr>
	<tr>
		<td class="label"># of Expired Request:</td>
		<td class="field" valign="top">
			<s:property value="userDto.expiredRequest" default=" - "/>
			<s:url action="jobs" namespace="/my" var="schJobURL">
				<s:param name="selectedTab" value="3"></s:param>
			</s:url>
			<s:a href="%{schJobURL}">View</s:a>
		</td>
	</tr>
	
	<tr>
		<td colspan="3" class="buttons" align="right">
				<span><sj:submit value="Change Password" openDialog="DLG_ChangePassword_Profile_ID" cssClass="bbcomButton"/></span>
		</td>
	</tr>
</table>


</div>