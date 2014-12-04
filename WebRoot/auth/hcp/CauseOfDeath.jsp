<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.CauseOfDeathDAO" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="edu.ncsu.csc.itrust.beans.CauseOfDeathBean"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Cause of Death Trends";
%>

<%@include file="/header.jsp" %>

<h2 align="center">Cause of Death Trends</h2>

<style type="text/css">
	.fancyTable {
		font-family:"Trebuchet MS", Arial, Helvetica, sans-serif;
		border-collapse:collapse;
	}
	.fancyTable td, .fancyTable th {
		font-size:1em;
		background-color: #FFFFFF;
		border:1px solid #4F708D;
		padding:3px 7px 2px 7px;
	}
	.fancyTable th {
		font-size:1.1em;
		text-align:left;
		padding-top:5px;
		padding-bottom:4px;
		background-color:#4F708D;
		color:#ffffff;
	}
	.fancyTable tr.alt td {
		color:#000000;
		background-color:#DDDDFF;
	}
		
</style>

<%
	String start = new String();
	String end = new String();
	String type1 = new String();
	String type2 = new String();
	String num1 = new String();
	String num2 = new String();
	
	String gender = new String();
	String patient = new String();
	
	if(request.getParameter("find")!=null){
		start = request.getParameter("startDate");
		end = request.getParameter("endDate");
	}
	
	CauseOfDeathDAO dao = new CauseOfDeathDAO(prodDAO);
	if(request.getParameter("find") != null) {
		if(request.getParameter("chooseby").equals("all")){
			patient = "All Patients";
			if(start.length() == 4 && end.length() == 4 && request.getParameter("sortby").equals("all")) {
				List<CauseOfDeathBean> death = dao.getTop2All(loggedInMID, null, Integer.parseInt(start), Integer.parseInt(end));
				
				if(death.size() > 0) {
					type1 = death.get(0).getDescription();
					num1 = ""+ death.get(0).getCount();
				}
				
				if(death.size() > 1) {
					type2 = death.get(1).getDescription();
					num2 = ""+ death.get(1).getCount();
				}
				
				gender = "All Genders";
			}
			
			else if(start.length() == 4 && end.length() == 4 && request.getParameter("sortby").equals("male")) {
				List<CauseOfDeathBean> death = dao.getTop2All(loggedInMID, "Male", Integer.parseInt(start), Integer.parseInt(end));
				
				if(death.size() > 0) {
					type1 = death.get(0).getDescription();
					num1 = ""+ death.get(0).getCount();
				}
				
				if(death.size() > 1) {
					type2 = death.get(1).getDescription();
					num2 = ""+ death.get(1).getCount();
				}
				
				gender = "Male";
			}
			
			else if(start.length() == 4 && end.length() == 4 && request.getParameter("sortby").equals("female")) {
				List<CauseOfDeathBean> death = dao.getTop2All(loggedInMID, "Female", Integer.parseInt(start), Integer.parseInt(end));
				
				if(death.size() > 0) {
					type1 = death.get(0).getDescription();
					num1 = ""+ death.get(0).getCount();
				}
				
				if(death.size() > 1) {
					type2 = death.get(1).getDescription();
					num2 = ""+ death.get(1).getCount();
				}
				
				gender = "Female";
			}
		}
		else {
			patient = "My Patients";
			if(start.length() == 4 && end.length() == 4 && request.getParameter("sortby").equals("all")) {
				List<CauseOfDeathBean> death = dao.getTop2Specific(loggedInMID, null, Integer.parseInt(start), Integer.parseInt(end));
				
				if(death.size() > 0) {
					type1 = death.get(0).getDescription();
					num1 = ""+ death.get(0).getCount();
				}
				
				if(death.size() > 1) {
					type2 = death.get(1).getDescription();
					num2 = ""+ death.get(1).getCount();
				}
				
				gender = "All Genders";
			}
			
			else if(start.length() == 4 && end.length() == 4 && request.getParameter("sortby").equals("male")) {
				List<CauseOfDeathBean> death = dao.getTop2Specific(loggedInMID, "Male", Integer.parseInt(start), Integer.parseInt(end));
				
				if(death.size() > 0) {
					type1 = death.get(0).getDescription();
					num1 = ""+ death.get(0).getCount();
				}
				
				if(death.size() > 1) {
					type2 = death.get(1).getDescription();
					num2 = ""+ death.get(1).getCount();
				}
				
				gender = "Male";
			}
			
			else if(start.length() == 4 && end.length() == 4 && request.getParameter("sortby").equals("female")) {
				List<CauseOfDeathBean> death = dao.getTop2Specific(loggedInMID, "Female", Integer.parseInt(start), Integer.parseInt(end));
				
				if(death.size() > 0) {
					type1 = death.get(0).getDescription();
					num1 = ""+ death.get(0).getCount();
				}
				
				if(death.size() > 1) {
					type2 = death.get(1).getDescription();
					num2 = ""+ death.get(1).getCount();
				}
				
				gender = "Female";
			}
		}
	}
	
	
%>	
	
<div align="center">
	<form method="post">	
		<table>
			<tr>
				<td>
					<select name="chooseby">
						<option value="all">All Patients</option>
						<option value="my">My Patients</option>
					</select>
				</td>
				<td>
					<select name="sortby">
							<option value="all">All Genders</option>
							<option value="male">Male</option>
							<option value="female">Female</option>
					</select>
				</td>
				<td>
					<label for="startDate">Start Date: </label>
					<input type="text" name="startDate" id="startDate" value="<%= StringEscapeUtils.escapeHtml("" + (start )) %>" />
				</td>
				<td>
					<label for="endDate">End Date: </label>
					<input type="text" name="endDate" id="endDate" value="<%= StringEscapeUtils.escapeHtml("" + (end)) %>" />
				</td>
				<td>
					<input type="submit" name="find" value="Find" />
				</td>
			</tr>
		</table>
	</form>
</div>

<%if(!(start.length() == 4 && end.length() == 4 && type1.length() > 0)) {%>
	<h4 align="center">Please Enter a Valid Query.</h4>
<%} %>

<% if(start.length() == 4 && end.length() == 4 && type1.length() > 0) {%>
<div align="center">
	<h4>Results for <%= StringEscapeUtils.escapeHtml("" + (gender)) %> (<%= StringEscapeUtils.escapeHtml("" + (start)) %>-<%= StringEscapeUtils.escapeHtml("" + (end)) %>)</h4>
	<h5><%= StringEscapeUtils.escapeHtml("" + (patient)) %></h5>
	<table class="fancyTable" id="deathtable" width="80%">
		<tr>
			<th>Cause of Death</th>
			<th>Number of Deaths</th>
		</tr>
		<tr>
			<td><%= StringEscapeUtils.escapeHtml("" + (type1)) %></td>
			<td><%= StringEscapeUtils.escapeHtml("" + (num1)) %></td>
		</tr>
		<tr>
			<td><%= StringEscapeUtils.escapeHtml("" + (type2)) %></td>
			<td><%= StringEscapeUtils.escapeHtml("" + (num2)) %></td>
		</tr>
	</table>
</div>

<% } %>

<%@include file="/footer.jsp" %>