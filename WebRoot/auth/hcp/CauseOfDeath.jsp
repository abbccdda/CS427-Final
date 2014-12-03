<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>

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
	start = "2013";
	String end = new String();
	end = "2014";
%>	
	
<div align="center">
	<form method="post">	
		<table>
			<tr>
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

<div align="center">
	<h4>Results for male (2013-2014)</h4>
	<table class="fancyTable" id="deathtable" width="80%">
		<tr>
			<th>Cause of Death</th>
			<th>Number of Deaths</th>
		</tr>
		<tr>
			<td>Guns</td>
			<td>500000</td>
		</tr>
		<tr>
			<td>Knives</td>
			<td>300000</td>
		</tr>
	</table>
</div>

<%@include file="/footer.jsp" %>