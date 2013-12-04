<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>
<table  style="width: 100%">
	<tr>
		<td width="25%" id="listing">
		<s:if test="users!=null">
		<ul>
			<s:iterator value="users">
				<li>
					<s:url var="usrDtlsURL" action="st_userDetails" namespace="/settings" escapeAmp="false">
						<s:param name="userId" value="userId"/>
					</s:url>
					<sj:a href="%{usrDtlsURL}" targets="userDetailsTarget" indicator="detailLoadingbar">
						<s:property value="name"/>
					</sj:a>
				</li>
			</s:iterator>
		</ul>
		</s:if><s:else>
			<label>No Users in System.</label>
		</s:else>
		</td>
		<td valign="top">
			<s:url var="usrDtlsURL" action="st_userDetails" namespace="/settings" escapeAmp="false"/>
			<sj:div id="userDetailsTarget" href="%{usrDtlsURL}">
			</sj:div> 
		</td>
	</tr>
</table>