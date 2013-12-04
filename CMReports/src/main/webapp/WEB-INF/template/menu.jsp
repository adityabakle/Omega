<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="welcome">
	<s:url action="profile" var="myProfileURL" namespace="/my"/>
	<p>Welcome, <span><s:a href="%{myProfileURL}"><s:property value="loggedInUser.name"/> </s:a></span>! &nbsp;<span class="email">[<s:property value="loggedInUser.email"/>]</span></p>
	
</div>
<br/>
<table class="steps" style="width:100%;height:20px;table-layout: fixed;">
	<tr>
	<!--  Dashboard Menu  -->
	<s:if test="%{selectedMenuIndex==1}">
		<td class="selected" valign="middle">Dashboard</td>
	</s:if><s:else>
		<td class="active_step" valign="middle">
			<s:url action="dashboard" namespace="/my" var="dashboardURL"/>
			<s:a href="%{dashboardURL}">Dashboard</s:a>	
		</td>
	</s:else>	
	
	<!--  Report Menu  -->
	<s:if test="%{selectedMenuIndex==2}">
		<td class="selected">New Report</td>
	</s:if><s:else>
		<td class="active_step ">
			<s:url action="home" namespace="/report" var="reportHomeURL"/>
			<s:a href="%{reportHomeURL}">New Report</s:a>	
		</td>
	</s:else>
	<s:if test="%{loggedInUser.role.name=='Administrator' || loggedInUser.role.name=='Developer'}">
		<s:if test="%{selectedMenuIndex==7}">
			<td class="selected">SQL Executor</td>
		</s:if><s:else>
			<s:url action="sqlhome" namespace="/" var="sqlExesURL"/>
			<td class="active_step">
				<s:a href="%{sqlExesURL}">SQL Executor	</s:a>
			</td>
		</s:else>
	</s:if>	
	
	<s:if test="%{selectedMenuIndex==3}">
		<td class="selected">Jobs</td>
	</s:if><s:else>
		<td class="active_step ">
			<s:url action="jobs" namespace="/my" var="myJobsURL"/>
			<s:a href="%{myJobsURL}">Jobs</s:a>	
		</td>
	</s:else>
	
	<s:if test="%{selectedMenuIndex==4}">
		<td class="selected">My Reports</td>
	</s:if><s:else>
		<td class="active_step ">
			<s:url action="reports" namespace="/my" var="myReportsURL"/>
			<s:a href="%{myReportsURL}">My Reports</s:a>	
		</td>
	</s:else>
	
	<s:if test="%{selectedMenuIndex==5}">
		<td class="selected">My Profile</td>
	</s:if><s:else>
		<td class="active_step">
			<s:a href="%{myProfileURL}">My Profile</s:a>	
		</td>
	</s:else>
	
	<s:if test="%{loggedInUser.roleId==1}">
		<s:if test="%{selectedMenuIndex==6}">
			<td class="selected">Settings</td>
		</s:if><s:else>
			<s:url action="home" namespace="/settings" var="settingsURL"/>
			<td class="active_step">
				<s:a href="%{settingsURL}">Settings	</s:a>
			</td>
		</s:else>
	</s:if>	
	
	<td class="active_step">
			<s:url action="logout" namespace="/" var="logoutURL"/>
			<s:a href="%{logoutURL}"> Logout</s:a>	
	</td>
	</tr>
	
</table>
