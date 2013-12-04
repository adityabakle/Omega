<%@taglib uri="/struts-tags" prefix="s"%>
<div id="content">
<div class="breadcrumb">
	<s:url action="login" namespace="/" var="LoginURL"/>
	<s:a href="%{LoginURL}" >Login Page</s:a>
	<span class="separator"></span>
</div>
<s:actionerror cssClass="errorMsg"/>
<s:form name="frmResetPassword" method="post" action="doReset" namespace="/">
<table style="width:100%">
	<tr>
		<td class="title" colspan="2" >Password Reset</td>
	</tr>
	<tr>
		<td colspan="2" ><label>Please fill up the following form to reset your password.</label> </td>
	</tr>
	<tr>	
		<td class="label">User Name:</td>
	   	<td class="field">
	   		<s:textfield name="userParams.userName" maxlength="15"/>
	   	</td>
	</tr>
	<tr>	
		<td class="label">Email:</td>
	   	<td class="field">
	   		<s:textfield name="userParams.email" size="50" maxlength="100"/>
	   	</td>
	</tr>
	<tr>	
		<td class="label">New Password:</td>
	   	<td class="field">
	   		<s:password  name="userParams.newPassword" maxlength="20"/>
	   	</td>
	</tr>
	<tr>	
		<td class="label">Confirm Password:</td>
	   	<td class="field">
	   		<s:password  name="userParams.confirmPassword" maxlength="20"/>
	   	</td>
	</tr>
	<tr>
		<td colspan="2" class="buttons">
		<s:hidden name="userParams.passwordRecoveryKey"/>
		<span><s:submit value="Submit" cssClass="bbcomButton"/></span>
		</td>
	</tr>
</table>
</s:form>   	
</div>