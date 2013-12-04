<%@taglib uri="/struts-tags" prefix="s"%>
<div id="content">
	<s:url action="login" namespace="/" var="loginURL"/>
	<s:url action="reports" namespace="/my" var="myReports"/>
	<p class="errorMsg">Sorry! Some error occurred while downloading the file.</p>
	<s:actionerror cssClass="errorMsg"/>
	<label>
	<s:if test="%{(#session.loginUser!= null}">
		Click <s:a href="%{myReports}" cssClass="hyperlink"><b>here</b></s:a> to return to 'My Reports'.
	</s:if>
	<s:else>
		You can try downloading the file from 'My Reports' section after login.<br/>
		Click <s:a href="%{loginURL}" cssClass="hyperlink"><b>here</b></s:a> to login.
	</s:else>
	</label>
</div>