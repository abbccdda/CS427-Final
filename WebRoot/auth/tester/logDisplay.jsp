<%@page import="edu.ncsu.csc.itrust.enums.TransactionType"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.TransactionBean"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.util.Date"%>



<%-- <%@page import="java.text.ParseException"%>
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
<%@page import="java.util.List"%> --%>

<%@include file="/global.jsp"%>
<!-- Cite from whao2 for display log implementation -->
<%
	pageTitle = "iTrust - Tester Home";
%>

<%@include file="/header.jsp"%>

<% 

Object transactionType = session.getAttribute("transactionType");
Object userRole1 = session.getAttribute("userRole1");
Object userRole2 = session.getAttribute("userRole2");
Date startDate = new Date(Long.parseLong(session.getAttribute("startDate").toString()));
Date endDate = new Date(Long.parseLong(session.getAttribute("endDate").toString()));

/* out.print(startDate + "<br>");
out.print(endDate + "<br>");
out.print(transactionType.toString() + "<br>");
out.print(userRole1.toString() + "<br>");
out.print(userRole2.toString() + "<br>"); */
%>

<table border=1>
<tr>
	<th>LogID</th>
	<th>Logging time</th>
	<th>TranType</th>
	<th>Code</th>
	<th>Description</th>
	<th>firstRole MID</th>
	<th>Secondary MID</th>
	<th>Extra Info</th>
</tr>
<%
	//System.out.printIn(startDate.toString());
	//System.out.printIn(endDate.toString());
	//System.out.printIn(transactionType.toString());

	List<TransactionBean> list = DAOFactory.getProductionInstance().getTransactionDAO().getLog(userRole1.toString().toLowerCase(), userRole2.toString().toLowerCase(), transactionType.toString(), startDate, endDate);
	//test List<TransactionBean> list = DAOFactory.getProductionInstance().getTransactionDAO().getAllTransactions();

	
	for (TransactionBean t : list) {
%>
<tr>
	<td><%= StringEscapeUtils.escapeHtml("" + (t.getTransactionID())) %></td>
	<td><%= StringEscapeUtils.escapeHtml("" + (t.getTimeLogged())) %></td>
	<td><%= StringEscapeUtils.escapeHtml("" + (t.getTransactionType().name())) %></td>
	<td><%= StringEscapeUtils.escapeHtml("" + (t.getTransactionType().getCode())) %></td>
	<td><%= StringEscapeUtils.escapeHtml("" + (t.getTransactionType().getDescription())) %></td>
	<td><%= StringEscapeUtils.escapeHtml("" + (t.getLoggedInMID())) %></td>
	<td><%= StringEscapeUtils.escapeHtml("" + (t.getSecondaryMID())) %></td>
	<td><%= StringEscapeUtils.escapeHtml("" + (t.getAddedInfo())) %></td>
</tr>
<%
}
%>
</table>


<%@include file="/footer.jsp"%>