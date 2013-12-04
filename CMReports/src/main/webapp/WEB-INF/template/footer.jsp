<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<s:if test="%{(#session.loginUser!=null && #session.loginUser.role.name=='Administrator') || #session.originalLoginUser!=null && #session.originalLoginUser.role.name=='Administrator'}">
	<br>
		<label>Login as : </label>
		<s:url action="json_fetchUsers" namespace="/" var="jsonUserURL"/>
		<sj:select
			cssStyle="width:150px"
			href="%{jsonUserURL}"
			name="chngLoginUserId" 
			list="users" 
			listKey="userId" 
			listValue="name"
			emptyOption="false"
			onchange="reloadDashboard(this.value);"
			headerKey="0"
			headerValue="-- Original --"
			value="%{#session.loginUser.userId}"
			/>
	<s:form id="frmChngUserID" name="frmChngUser" action="dashboard" namespace="/my">		
		<s:hidden name="chngLoginUserId" id="chngLoginUserID"/>			
	</s:form>
	<script type="text/javascript">
	
	<!--
	function reloadDashboard(val){
		$('#chngLoginUserID')[0].value = val;
		$('#frmChngUserID').submit();	
	}
	//-->
	</script>

</s:if>
					
<div id="footer" class="pgFooter">
	<p style="line-height:20px;">
		Rev-Share Report Portal <b>v3.0&nbsp;</b>
	</p>  
</div>

