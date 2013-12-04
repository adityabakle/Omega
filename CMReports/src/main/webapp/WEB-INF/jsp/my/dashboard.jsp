<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/struts-jquery-tags" prefix="sj"%>
<div id="content">

<table style="width:100%;table-layout: fixed" >
	<tr>
		<td class="title">Your Recent Reports for Download</td>
		<td class="title"><b>Your Scheduled jobs</b></td>
	</tr>
	<tr>
		<td valign="top" style="border-right:solid;border-right-width: 1px;">
			<s:if test="exeLogs!=null && exeLogs.size() > 0">
			<table class="dataGrid-table">
			<s:iterator value="exeLogs" status="stat">
				<s:if test="#stat.even">
					<s:set var="alternate">alternate</s:set>
				</s:if><s:else>
					<s:set var="alternate" value="%{''}"/>
				</s:else>
				
					<tr class="<s:property value="%{#alternate}"/>">
						<td><s:property value="request.carrier.displayName" default="N/A"/> </td>
						<td><s:property value="fileName" default="N/A"/>  </td>
						<td><s:if test="endTime==null"> N/A</s:if><s:else><s:date name="endTime" format="dd-MMM-yyyy HH:mm" timezone="%{#session.loginUser_timeZone_Id}" nice="false"/></s:else></td>
						<td>
							<s:if test="success || fileName!=null">
							<s:url action="dlFile" namespace="/" var="dlFileURL">
								<s:param name="id" value="id"/>
							</s:url>
								<s:a href="%{dlFileURL}"><img src="<s:property value="%{#request.CONTEXT}"/>/images/download.png" title="Download Report" alt="Download" height="15"></s:a>
							</s:if>
							<s:else>N/A</s:else>
						</td>
					</tr>
			</s:iterator>
				<s:if test="%{#request.totalReportCount > perPageRecCount}">
					<tr>
						<td colspan="4">
							<s:a href="%{myReportsURL}">More...</s:a>
						</td>
					</tr>	
				</s:if>
			</table>
			</s:if><s:else>
				<label>No reports available for download.</label>
			</s:else>
		</td>
		<td valign="top">
			<s:if test="myReqReports!=null && myReqReports.size() > 0">
			<table class="dataGrid-table">
			<s:iterator value="myReqReports" status="stat">
				<s:if test="#stat.even">
					<s:set var="alternate">alternate</s:set>
				</s:if><s:else>
					<s:set var="alternate" value="%{''}"/>
				</s:else>
				
					<tr class="<s:property value="%{#alternate}"/>">
						<td> 
							<s:property value="report.displayName"/>
							<s:if test="%{report.name=='sqlRpt'}">
								<br/><span class="smallTxt"><s:property value="mail.subject"/></span>
							</s:if> 
						</td>
						<td><s:property value="carrier.displayName"/></td>
						<td>
							<s:if test="state=='active'">
								<span class="required">Executing...</span><img id="loadingbar" alt="Loading..." src="<s:property value="%{#request.CONTEXT}"/>/images/loading.gif">
							</s:if> 
							<s:else>
								<b>Next:</b> <s:if test="nextExecutionDate==null"> N/A</s:if><s:else><b><s:date name="nextExecutionDate" format="dd-MMM-yyyy HH:mm" timezone="%{#session.loginUser_timeZone_Id}"/></b></s:else><br/>
								Last: <s:if test="lastExecutedDate==null"> N/A</s:if><s:else><s:date name="lastExecutedDate" format="dd-MMM-yyyy HH:mm" timezone="%{#session.loginUser_timeZone_Id}" nice="false"/></s:else><br/>
								Start: <s:if test="scheduledDate==null"> N/A</s:if><s:else><s:date name="scheduledDate" format="dd-MMM-yyyy HH:mm" timezone="%{#session.loginUser_timeZone_Id}" nice="false"/></s:else><br/> 
								Expires: <s:if test="expiryDate==null"> Never </s:if><s:else><s:date name="expiryDate" format="dd-MMM-yyyy HH:mm" timezone="%{#session.loginUser_timeZone_Id}"/></s:else>
							</s:else>
						</td>
						<td valign="middle">
							<s:url action="details" namespace="/report" var="reqDetailsURl">
								<s:param name="requestId" value="requestId"/>
							</s:url>
							<sj:a cssClass="hyperlink" href="%{reqDetailsURl}" openDialog="requestDetailsDLG" ><img src="<s:property value="%{#request.CONTEXT}"/>/images/view.png" title="View Request" alt="View" height="15"></sj:a>
						</td>
					</tr>
			</s:iterator>
				<s:if test="%{#request.totalReqCount > perPageRecCount}">
					<tr>
						<td colspan="3">
							<s:url action="jobs" namespace="/my" var="myJobsURL">
								<s:param name="selectedTab">2</s:param>
							</s:url>
							<s:a href="%{myJobsURL}">More...</s:a>
						</td>
					</tr>	
				</s:if>
			</table>
			</s:if><s:else>
				<label>No valid request found.</label>
			</s:else>
		</td>
	</tr>
	<s:if test="adminAnnouncement!=null">
	<tr>
		<td colspan="2"><hr/></td>
	</tr>
		<tr>
			<td colspan="2" class="title">Administrator Announcement!</td>
		</tr>
		<tr>
			<td colspan="2"><s:property value="adminAnnouncement" escapeHtml="false"/></td>
		</tr>
	</s:if>
</table>
</div>