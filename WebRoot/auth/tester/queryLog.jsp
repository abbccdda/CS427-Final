<%@page import="edu.ncsu.csc.itrust.enums.TransactionType"%>
<%@page import="edu.ncsu.csc.itrust.beans.OperationalProfile"%>
<%@page import="java.text.NumberFormat"%>


<%@page import="java.text.ParseException"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptTypeBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.ApptTypeDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.HCPVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewVisitedHCPsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="edu.ncsu.csc.itrust.action.AddApptRequestAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptRequestBean"%>
<%@page import="java.util.List"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Tester Home";
%>

<%@include file="/header.jsp"%>
<!-- Cite from T817 for query log implementation -->

<%
	String userRole1 = "";
	String userRole2 = "";
	String startDate = "";
	String endDate = "";
	String msg = "";
	String transactionType = "";
	if (request.getParameter("view") != null || request.getParameter("summarize") != null) {

		userRole1 = request.getParameter("userRole1");
		userRole2 = request.getParameter("userRole2");
		startDate = request.getParameter("startDate");
		endDate = request.getParameter("endDate");
		transactionType = request.getParameter("transactionType");
		
		SimpleDateFormat frmt = new SimpleDateFormat(
				"MM/dd/yyyy");
		try {
			Date startD = frmt.parse(startDate);
			Date endD = frmt.parse(endDate);
			
			if (startD.after(endD)) {
				msg = "ERROR: End Date must be after Start Date";
			}
			else
			{
				
				session.setAttribute("userRole1", userRole1);
				session.setAttribute("userRole2", userRole2);
			    session.setAttribute("startDate", startD.getTime()); 
			    session.setAttribute("endDate", endD.getTime()); 
			    session.setAttribute("transactionType", transactionType);
				if (request.getParameter("view") != null) {

				    response.sendRedirect("logDisplay.jsp");
				}
				
				if(request.getParameter("summarize") != null){
					
					response.sendRedirect("visualLog.jsp");
					
				}
			}
			

		} catch (ParseException e) {
			msg = "ERROR: Date must by in the format: MM/dd/yyyy";
		}
	}
%>


<%
	if (msg.contains("ERROR")) {
%>
<span class="iTrustError"><%=msg%></span>
<%
	} else {
%>
<span class="iTrustMessage"><%=msg%></span>
<%
	}
%>


<form action="queryLog.jsp" autocomplete="on" method="post">

<table>
		<tr>
<td>
		<input name="startDate"
			value="<%=StringEscapeUtils.escapeHtml("" + (startDate))%>" size="10">
		<input type=button value="Start Date"
			onclick="displayDatePicker('startDate');">
</td>

<td>
		<input name="endDate"
			value="<%=StringEscapeUtils.escapeHtml("" + (endDate))%>" size="10">
		<input type=button value="End Date"
			onclick="displayDatePicker('endDate');">
</td>

	</tr>
	
	<tr>
		<td class="subHeaderVertical">User Role 1:</td>
		<td><select name="userRole1">
				<option value="">Select:</option>
				<option value="Patient">Patient</option>
				<option value="ER">ER</option>
				<option value="HCP">HCP</option>
				<option value="UAP">UAP</option>
				<option value="LT">LT</option>
				<option value="Admin">Admin</option>
				<option value="PHA">PHA</option>
				<option value="Tester">Tester</option>
				<option value="%">All Roles</option>
		</select></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">User Role 2:</td>
		<td><select name="userRole2">
				<option value="">Select:</option>
				<option value="Patient">Patient</option>
				<option value="ER">ER</option>
				<option value="HCP">HCP</option>
				<option value="UAP">UAP</option>
				<option value="LT">LT</option>
				<option value="Admin">Admin</option>
				<option value="PHA">PHA</option>
				<option value="Tester">Tester</option>
				<option value="%">All Roles</option>
		</select></td>
	</tr>

	<!--
	date
	-->


	
	<td class="subHeaderVertical">transactionType:</td>
		<td><select name="transactionType">
				<option value="">Select:</option>
				
				<%    
				TransactionType[] transactionTypes = TransactionType.values();
				
				for(TransactionType type : transactionTypes) {
				
					out.print("<option value= " + type.getCode() + ">" + type.toString() + "</option>");
					
				}
				
				
				%>
		</select></td>
	
	<tr>
	
	
	
	</tr>
	

</table>


  
  <input type="submit" name="view" value="View">
  <input type="submit" name="summarize" value="Summarize">
</form> 

<%@include file="/footer.jsp"%>
