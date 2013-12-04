<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>

<s:form name="frmServerSettMaster" id="frmServerSettMasterID" action="st_schReStart" namespace="/settings">
<s:actionerror cssClass="errorMsg"/>
<s:fielderror fieldName="mandateField" cssClass="errorMsg" id="errorMsgSSetId"/>
<table  style="width: 100%">
	<tr>
		<td class="title" >
			Schedular Details:
		</td>
	</tr>
	<tr>
		<td valign="top">
			<table style="width:100%;">
				<tr>
					<td class="label">ID:</td>
					<td class="field">
						<s:property value="%{schMD.getSchedulerInstanceId()}"/>
						
					</td>
				</tr>
				<tr>
					<td class="label">Name:</td>
					<td class="field">
						<s:property value="%{schMD.getSchedulerName()}"/>
					</td>
				</tr>
				<tr>
					<td class="label">Class:</td>
					<td class="field"><s:property value="%{schMD.getSchedulerClass()}"/></td>
				</tr>
				
				<tr>
					<td class="label">Status:</td>
					<td class="field">
						<s:if test="%{schMD.isInStandbyMode()}">
							Stand-By
						</s:if><s:elseif test="%{schMD.isPaused()}">
							Paused
						</s:elseif><s:elseif test="%{schMD.isStarted()}">
							Started
						</s:elseif><s:elseif test="%{schMD.isShutdown()}">
							Shutdown
						</s:elseif>
					</td>
				</tr>
				<tr>
					<td class="label">Start Date:</td>
					<td class="field">
						<s:date name="schMD.runningSince" format="dd-MMM-yyyy HH:mm z"  />
						
					</td>
				</tr>
				
				<tr>
					<td class="label">Thread Pool Size</td>
					<td class="field">
						<s:property value="%{schMD.getThreadPoolSize()}"/>
					</td>
				</tr>
				<tr>
					<td class="label">Version</td>
					<td class="field">
						<s:property value="%{schMD.getVersion()}"/>
					</td>
				</tr>
				
				<tr>
					<td class="label"># Job Executed </td>
					<td class="field">
						<s:property value="schMD.numberOfJobsExecuted"/>
					</td>
				</tr>
			</table> 
		</td>
	</tr>
	<tr>
		<td class="buttons">
			<s:if test="%{schMD.isInStandbyMode()}">
				<s:submit value="Re-Start" cssClass="bbcomButton"/>
			</s:if><s:else>
				<s:submit action="st_schStandBy" value="Stand-By" cssClass="bbcomButton"/>
			</s:else>
			<s:hidden name="selectedTab"/>
		</td>
	</tr>
</table>
</s:form>
