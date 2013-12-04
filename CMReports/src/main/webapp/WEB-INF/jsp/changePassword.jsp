<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/struts-jquery-tags" prefix="sj"%>
<div id="contentChangePassword">
<s:if test="%{#request.cpDone}">
	<script>
		$('#DLG_ChangePassword_ID').dialog('close');
		alert("Password chnaged succesfully. Please login with your new credentials.");
	</script>
</s:if>
<s:else>
	<s:if test="%{#parameters.passwordExpired}">
		<label style="color: red">Your password has expired!!</label><br/>
	</s:if>
	<label>Please fill up the following form to change your password. </label>
	<br/>
	<br/>
	<s:actionerror cssClass="errorMsg"/>
	<s:form id="frmChangePassword" name="frmChangePassword" action="changePassword" namespace="/">
	<table style="width: 100%">
		<tr>	
			<td class="label">
		   		User Name:
		   	</td>
		   	<td>
		   		<s:textfield cssClass="text1" name="userParams.userName" maxlength="15" size="15"/> &nbsp;
		   	</td>
		</tr>
		<tr>	
			<td class="label">
		   		Old Password: 
		   	</td>
		   	<td>
		   		<s:password cssClass="text1" name="userParams.password" maxlength="20"/> &nbsp;
		   	</td>
		</tr>
		<tr>	
			<td class="label">
		   		New Password: 
		   	</td>
		   	<td>
		   		<s:password cssClass="text1" name="userParams.newPassword" maxlength="20"/> &nbsp;
		   	</td>
		</tr>
		<tr>	
			<td  class="label">
		   		Confirm Password: 
		   	</td>
		   	<td>
		   		<s:password cssClass="text1" name="userParams.confirmPassword" maxlength="20"/> &nbsp;
		   	</td>
		</tr>
		<tr>
			<td colspan="2" align="right">
			<s:hidden name="passwordExpired" value="%{#parameters.passwordExpired}"/>
			<span><sj:submit cssClass="bbcomButton" value="Submit" formIds="frmChangePassword" targets="DLG_ChangePassword_ID"/></span>
			</td>
		</tr>
	</table>
	</s:form>
 </s:else>  	
</div>