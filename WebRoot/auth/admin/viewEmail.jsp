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
	pageTitle = "iTrust - View Message";
%>

<%@include file="/header.jsp" %>

<% 
Email email = null;
if (request.getParameter("msg") != null) {
	String msgParameter = request.getParameter("msg");
	int msgIndex = 0;
	try{
		msgIndex = Integer.parseInt(msgParameter);
		List<Email> emails = DAOFactory.getProductionInstance().getFakeEmailDAO().getEmailsByPerson("System Reminder");
		if(emails.size() <= msgIndex){
			response.sendRedirect("/iTrust/auth/admin/viewAppointmentReminders.jsp");
		}
		else{
			email = emails.get(msgIndex);
		}
	}
	catch (NumberFormatException e){
		request.setAttribute("msg", null);
		response.sendRedirect("/iTrust/auth/admin/viewAppointmentReminders.jsp");
	}
}
	
else {
	response.sendRedirect("/iTrust/auth/admin/viewAppointmentReminders.jsp");
}
%>

<div align="center">
	<h1>View Email</h1>
</div>
<% if(email != null) { %>
<div align = "left">
	<br>To: <%= StringEscapeUtils.escapeHtml("" + (email.getToListStr() )) %></br>
	<br>Subject: <%= StringEscapeUtils.escapeHtml("" + (email.getSubject() )) %></br>
	<br>Sent: <%= StringEscapeUtils.escapeHtml("" + (email.getTimeAdded().toString())) %></br>
	<br></br>
	<br> <%= StringEscapeUtils.escapeHtml("" + (email.getBody())) %> </br>

</div>
<% } %>

<%@include file="/footer.jsp" %>