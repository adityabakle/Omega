<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>
<table  style="width: 100%">
	<tr>
		<td width="25%" id="listing">
		<s:if test="carriers!=null">
		<ul>
			<s:iterator value="carriers">
				<li>
					<s:url var="carrierDtlsURL" action="st_carrierDetails" namespace="/settings" escapeAmp="false">
						<s:param name="carrierId" value="carrierId"/>
					</s:url>
					<sj:a href="%{carrierDtlsURL}" targets="carrierDetailsTarget" indicator="detailCLoadingbar">
						<s:property value="displayName"/>
					</sj:a>
				</li>
			</s:iterator>
		</ul>
		</s:if><s:else>
			<label>No carriers defines in system.</label>
		</s:else>
		</td>
		<td valign="top">
			<s:url var="carrierDtlsURL" action="st_carrierDetails" namespace="/settings" escapeAmp="false"/>
			<sj:div id="carrierDetailsTarget" href="%{carrierDtlsURL}">
			</sj:div> 
		</td>
	</tr>
</table>