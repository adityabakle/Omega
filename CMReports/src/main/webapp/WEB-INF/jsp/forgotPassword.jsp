<%@taglib uri="/struts-tags" prefix="s"%>
<div id="content">
<div class="breadcrumb">
	<s:url action="login" namespace="/" var="LoginURL"/>
	<s:a href="%{LoginURL}" >Login</s:a>
	<span class="separator"></span>
</div>
<h3>Forgot Password</h3>
<s:form name="frmLogin" method="post" action="recoverPassword" namespace="/">
<table>
	 <tr>
	 	<td><s:actionerror cssClass="errorMsg"/> </td>
	 </tr>
	   <tr>
	   		<td align="left"><label >Please enter your email address below to submit request for password recovery. </label></td>
	   </tr>
	   <tr>	
	   		<td>
	   			<s:textfield cssClass="text1" name="userParams.email" size="50"/> &nbsp;
		  		<s:submit value="Send Request" cssClass="bbcomButton"/>
		  	</td>
	  	</tr>
	</table>
</s:form>   	
</div>