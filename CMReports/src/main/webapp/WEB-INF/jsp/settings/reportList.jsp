<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>
<div id="reporttabContentDIV_ID">
<s:actionerror cssClass="errorMsg"/>
<s:actionmessage cssClass="success"/>
<table  style="width: 100%">
	<tr>
		<td width="25%" id="listing">
		<s:if test="reports!=null">
		<ul>
			<s:iterator value="reports">
				<li>
					<s:url var="reportDtlsURL" action="st_reportDetails" namespace="/settings" escapeAmp="false">
						<s:param name="reportId" value="reportId"/>
					</s:url>
					<sj:a href="%{reportDtlsURL}" targets="reportDetailsTarget" indicator="detailRLoadingbar">
						<s:property value="displayName"/>
					</sj:a>
				</li>
			</s:iterator>
		</ul>
		</s:if><s:else>
			<label>No reports defines in system.</label>
		</s:else>
		</td>
		<td valign="top">
			<s:url var="reportDtlsURL" action="st_reportDetails" namespace="/settings" escapeAmp="false"/>
			<sj:div id="reportDetailsTarget" href="%{reportDtlsURL}" reloadTopics="reloadReportList">
				
			</sj:div> 
		</td>
	</tr>
</table>
</div>