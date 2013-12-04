<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>CM Reports</title>
</head>
<body>
	DB date and Time :  <s:date name="%{#request.DB_SYSTIMESTAMP}" format="dd-MM-yyyy hh:mm:ss z" timezone="%{#request.DB_TIMEZONE}"/>
	<br/><s:property value="#request.DB_TIMEZONE"/>
</body>
</html>