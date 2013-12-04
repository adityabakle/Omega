<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<html>
	<head>
		<!-- meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" / -->
		<sj:head ajaxcache="false" jqueryui="true" compressed="false" debug="true" jquerytheme="cmreport" customBasepath="%{#request.CONTEXT}/includes/themes"/>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link rel="shortcut icon" href="<s:property value="%{#request.CONTEXT}"/>/images/favicon.ico"/>
		<link rel="icon" type="image/gif" href="<s:property value="%{#request.CONTEXT}"/>/images/animated_favicon1.gif"/>
		<tiles:insertAttribute name="scripts" />
		<title><tiles:insertAttribute name="title" ignore="true" /></title>
	</head>
	<body>
		<s:include value="/WEB-INF/template/dialog.jsp"/>
		<table id="wrapper" align="center">
			<tr>
				<td><tiles:insertAttribute name="header" /></td>
			</tr>
			<tr >
				<td valign="top" width="1024px">
					<tiles:insertAttribute name="menu" /> 
				</td>
			</tr>	
			<tr>
				<td width="1024px" valign="top" >
				<div id="ContentFrame" style="height:100%;width:100%;scrolling:auto;marginHeight=5;marginwidth=5;border=0;">
					<tiles:insertAttribute name="body" />
					</div> 
				</td>
			</tr>
			<tr>
				<td width="1024px" valign="bottom"><tiles:insertAttribute name="footer"/></td>
			</tr>
		</table>
	</body>
</html>