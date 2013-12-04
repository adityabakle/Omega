<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/struts-jquery-tags" prefix="sj"%>
<div id="contentChangePassword">
<s:if test="%{#request.cpDone}">
	<script>
		$('#DLG_ChangePassword_Profile_ID').dialog('close');
		alert("Password chnaged succesfully. Please use your new credentials when you login next time.");
	</script>
</s:if>
<s:else>
	<label>Please fill up the following form to change your password. </label>
	<br/>
	<s:actionerror cssClass="errorMsg"/>
	<s:form id="frmChangeMyPassword" name="frmChangeMyPassword" action="changeMyPassword" namespace="/my">
	<table style="width: 100%">
		<tr>	
			<td class="label" style="width:35% !important;">
		   		User Name:
		   	</td>
		   	<td class="field">
		   		<s:property value="loggedInUser.userName"/> &nbsp;
		   		<s:hidden name="userParams.userId" value="%{loggedInUser.userId}"/>
		   		<s:hidden name="userParams.userName" value="%{loggedInUser.userName}"/>
		   	</td>
		</tr>
		<tr>	
			<td class="label">
		   		Old Password: 
		   	</td>
		   	<td class="field">
		   		<s:password cssClass="text1" name="userParams.password" maxlength="20"/> &nbsp;
		   	</td>
		</tr>
		<tr>	
			<td class="label">
		   		New Password: 
		   	</td>
		   	<td class="field">
		   		<s:password cssClass="text1" name="userParams.newPassword" maxlength="20"/> &nbsp;
		   	</td>
		</tr>
		<tr>	
			<td  class="label">Confirm Password:</td>
		   	<td  class="field">
		   		<s:password cssClass="text1" name="userParams.confirmPassword" maxlength="20"/> &nbsp;
		   	</td>
		</tr>
		<tr>
			<td colspan="2" align="right">
			<s:hidden name="passwordExpired" value="%{#parameters.passwordExpired}"/>
			<span><sj:submit cssClass="bbcomButton" value="Submit" formIds="frmChangeMyPassword" targets="contentChangePassword"/></span>
			</td>
		</tr>
	</table>
	</s:form>
 </s:else>  	
</div>