<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>FHIR Client Registration</title>
</head>
<body>
	<div align="center">
		<form:form action="register" method="post" commandName="clientForm">
			<table border="0">
				<tr>
					<td colspan="2" align="center"><h2>FHIR Client -
							Registration</h2></td>
				</tr>
				<tr>
					<td><b>Description:</b></td>
					<td><form:input path="additionalInformation" /></td>
				</tr>
				<tr height="20" />

				<tr>
					<td><b>Redirect URI:</b></td>
					<td><form:input path="webServerRedirectUri" /></td>
				</tr>
				<tr height="20" />
				<tr>
					<td><b>Scope:</b></td>
					<td><form:checkboxes delimiter="<br/>" path="scopeList"
							items="${scopeList}" /></td>
				</tr>
				<tr height="20" />
				<tr />
				<tr />
				<tr>
					<td><b>Resources:</b></td>
					<td><form:checkboxes delimiter="<br/>" path="resourceList"
							items="${resourceList}" /></td>
				</tr>
				<tr height="10" />
				<tr>
					<td colspan="2" align="center"><input type="submit"
						value="Register" /></td>
				</tr>
			</table>
		</form:form>
	</div>
</body>
</html>