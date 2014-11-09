<%@page import="java.net.URLEncoder" %>
<%@page import="java.net.URLDecoder" %>
<%@page import="java.util.List"%>
<%@page import="java.sql.*"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.FakeEmailDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.Email"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>



<%@include file="/global.jsp" %>

<%
	pageTitle = "iTrust - View Appointment Reminders";
%>

<%@include file="/header.jsp" %>
<div align="center">
	<h1>View Reminders</h1>
	<h3>Here all the reminders sent</h3>
<%
List<Email> emails = DAOFactory.getProductionInstance().getFakeEmailDAO().getEmailsByPerson("system Reminder");
%>
<br />
<table class="results">
	<tr>
		<th>To List</th>
		<th>Subject</th>
		<th>Time Stamp</th>
	</tr>
	<%
	int index = 0;
	for (Email email : emails) {
	%>
	<tr>
		<td><%= StringEscapeUtils.escapeHtml("" + (email.getToListStr() )) %></td>
		<td><%= StringEscapeUtils.escapeHtml("" + (email.getSubject() )) %></td>
		<td><%= StringEscapeUtils.escapeHtml("" + (email.getTimeAdded().toString())) %></td>
		<td><a href="/iTrust/auth/admin/viewEmail.jsp?msg=<%= String.valueOf(index) %>">Read</a></td>
	</tr>
	<%
	index++;
	}
	%>
</table>
<br />
<br />
</div>
<%@include file="/footer.jsp" %>