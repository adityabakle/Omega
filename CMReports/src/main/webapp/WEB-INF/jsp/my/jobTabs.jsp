<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>

<div id="content">
<sj:tabbedpanel id="jobTabPannel" selectedTab="%{selectedTab}" animate="true" cssClass="bodyTxt" cssStyle="width:100%"  >
	<s:url action="my_scheduledJobs" namespace="/my" var="scheduledJobsURL"/>
	<s:url action="my_activeJobs" namespace="/my" var="activeJobsURL"/>
	<s:url action="my_jobs" namespace="/my" var="jobsURL"/>
	<s:url action="my_history" namespace="/my" var="historyURL"/>
	<s:if test="%{loggedInUser.role.name=='Administrator'}">
		<s:url action="my_allJob" namespace="/my" var="allJobURL"/>
		<s:url action="my_failedJob" namespace="/my" var="failedJobURL"/>
	</s:if>
	
	<sj:tab id="schJobTab" href="%{scheduledJobsURL}" label="Scheduled Jobs"></sj:tab>
	<sj:tab id="actJobTab" href="%{activeJobsURL}" label="Active Jobs"></sj:tab>
	<sj:tab id="myJobTab" href="%{jobsURL}" label="My Jobs"></sj:tab>
	<sj:tab id="historyJob" href="%{historyURL}" label="My Old Jobs"></sj:tab>
	
	<s:if test="%{loggedInUser.role.name=='Administrator'}">
		<sj:tab id="allJob" href="%{allJobURL}" label="All Jobs"></sj:tab>
		<sj:tab id="failedJob" href="%{failedJobURL}" label="Failed Jobs"></sj:tab>
	</s:if>
</sj:tabbedpanel>
</div>