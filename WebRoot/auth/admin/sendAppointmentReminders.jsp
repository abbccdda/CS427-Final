<%@page import="java.net.URLEncoder" %>
<%@page import="java.net.URLDecoder" %>
<%@page import="java.util.List"%>



<%@include file="/global.jsp" %>

<%
	pageTitle = "iTrust - Send Appointment Reminders";
%>

<%@include file="/header.jsp" %>
<div align="center">
	<h1>Send Reminders</h1>
	<h3>Enter the number of days in advanced to send the appointments: </h3>
	
	<form method="post">
		<input type="text" name="Number of Days">
	</form>
</div>
<%@include file="/footer.jsp" %>