<%@page import="java.net.URLEncoder" %>
<%@page import="java.net.URLDecoder" %>
<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.enums.Role"%>
<%@page import="edu.ncsu.csc.itrust.EmailUtil" %>
<%@page import="edu.ncsu.csc.itrust.beans.Email"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.FakeEmailDAO"%>


<%@include file="/global.jsp" %>

<%
	pageTitle = "iTrust - Send Appointment Reminders";
%>

<%@include file="/header.jsp" %>

<%
	boolean formIsFilled = request.getParameter("formIsFilled") != null
	&& request.getParameter("formIsFilled").equals("true");

	if (formIsFilled) {
		String form = request.getParameter("days");
		try{
			int days = Integer.parseInt(form);
			if(days<=0){
				%><h1 align="center">The number of days must be greater than 0</h1><%
			}
			else{
				DAOFactory.getProductionInstance().getFakeEmailDAO().sendReminderEmails(days);
				%><h1 align="center">Appointments Sent Successfully</h1><%
			}
		}
		catch (NumberFormatException e){
			%> <h1 align="center">the input was not a number</h1> <%
		}
	}%>

<div align="center">
	<h1>Send Reminders</h1>
	Enter the number of days in advanced to send the appointments:
	<form action="sendAppointmentReminders.jsp" method="post">
		<input type="hidden" name="formIsFilled" value="true"><br />
		<input type="text" name="days">
		<input type="submit" value="Send Appointment Reminders">
	</form>
</div>
<%@include file="/footer.jsp" %>