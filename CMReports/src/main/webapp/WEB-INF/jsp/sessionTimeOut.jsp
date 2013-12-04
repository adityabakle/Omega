<%@taglib uri="/struts-tags" prefix="s"%>
<s:url action="login" namespace="/" var="loginURL"/>
<p class="errorMsg">Sorry!. Your session has timeout.</p>
<label>Click <s:a href="%{loginURL}" style="color:blue"><b>here</b></s:a> to login.</label>