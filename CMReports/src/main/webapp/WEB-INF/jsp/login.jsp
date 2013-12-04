<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/struts-jquery-tags" prefix="sj"%>

<div id="content" align="center">
<s:form name="frmLogin" method="post" action="authenticate" namespace="/">
<table>
	<tr>
		<td valign="bottom" align="left">
			<img src="<s:property value="%{#request.CONTEXT}"/>/images/CM_BigLogo.png" border=0 alt="Cellmania Inc."/>
		</td>
		<td width="100px">&nbsp;</td>
		<td valign="bottom" align="right">
			<img src="<s:property value="%{#request.CONTEXT}"/>/images/RIMLogo_Black.jpg" height="80" border=0 alt="Research In Motion Corp."/>
		</td>
	</tr>
	<tr>
		<td colspan="3"><hr></td>
	</tr>
	<tr>
		<td colspan="3">
			<s:actionerror cssClass="errorMsg"/>
			<s:actionmessage cssClass="success"/>
		</td>
	</tr>
	<tr >
		<td align="center" colspan="3" valign="bottom" style="background-image:url('<s:property value="%{#request.CONTEXT}"/>/images/loginForm_bg.gif')" >
		
			 <table id="loginTable">
			   <tr>
			   		<td rowspan="2"><img src="<s:property value="%{#request.CONTEXT}"/>/images/silverlock.png" alt="Login" height="50px"></td>
			   		<td align="left"><label >User Name</label></td>
			   		<td align="left"><label >Password</label></td>
			   		<td></td>
			   </tr>
			   <tr>
			   		<td><s:textfield name="userParams.userName" size="25"/></td>
			  		<td><s:password  name="userParams.password" size="25"/></td>
				  	<td valign="middle" align="right">
				  		<s:submit value="Login" cssClass="bbcomButton"/>
				  		<s:hidden value="login" name="action"/>&nbsp;&nbsp;
						<s:hidden name="usrOffSet" id="usrOffsetID"/>
				  	</td>
			  	</tr>
			  	<tr>
			  		<td colspan="4" align="center">
			  			<s:url var="forgotPasswordURL" action="forgotPassword" namespace="/"/>
			  			<s:a href="%{forgotPasswordURL}" title="Forgot Password">Forgot Password?</s:a>
			  		</td>
			  	</tr>
			</table>
		
		</td>
	</tr>
	<tr>
		<td colspan="3"><hr></td>
	</tr>
	<tr>
		<td colspan="3" align="center"><span class="smallTxt"><i>(Best works with Chrome 14 and above.)</i></span> </td>
	</tr>
</table>
</s:form>
<s:if test="passwordExpired">
<s:url action="paswordExpired" namespace="/" var="changePasswordURL">
	<s:param name="passwordExpired" value="%{passwordExpired}"/>
</s:url>

	<sj:dialog
		id="DLG_ChangePassword_ID"
		autoOpen="true"
		cssStyle="display:none"
		closeOnEscape="true"
		draggable="false"
		resizable="false"
		href="%{#changePasswordURL}"
		modal="true"
		loadingText="Verifying..."
		width="400"
		height="250"
		title="Change Password"
	/>
	
	
</s:if>	
</div>
<script type="text/javascript">
$('#usrOffsetID')[0].value=new Date().getTimezoneOffset();
</script>