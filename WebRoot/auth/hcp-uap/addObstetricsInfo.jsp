<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewHealthRecordsHistoryAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricsRecordsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.HealthRecord"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.beans.forms.HealthRecordForm"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ObstetricsBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.enums.DeliveryMethod"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Add Obstetric Record";
%>

<%@include file="/header.jsp" %>
<itrust:patientNav thisTitle="Obstetric Record" />
<%
//Require a Patient ID first
String pidString = (String)session.getAttribute("pid");
if (pidString == null || 1 > pidString.length()) {
	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/addObstetricsInfo.jsp");
	return;
}

ViewObstetricsRecordsAction obstetricsAction = new ViewObstetricsRecordsAction(prodDAO,pidString,loggedInMID.longValue());
String patientName = obstetricsAction.getPatientName();

boolean formIsFilled = request.getParameter("formIsFilled") != null
	&& request.getParameter("formIsFilled").equals("true");
	ObstetricsBean b;
	
		if (formIsFilled) {
			b = new ObstetricsBean();
			b.setMID(obstetricsAction.getPatientMID());
			b.setYearOfConception(Integer.parseInt(request.getParameter("yearOfConception")));
			b.setWeeksPregnant(request.getParameter("weeksPregnant"));
			b.setHoursLabor(Double.parseDouble(request.getParameter("hoursLabor")));
			b.setDeliveryMethod(request.getParameter("deliveryMethod"));
			obstetricsAction.addObstetricsInfo(b);
			loggingAction.logEvent(TransactionType.ADD_OBSTETRICS, loggedInMID.longValue(), b.getMID(), "");
			response.sendRedirect("/iTrust/auth/hcp-uap/obstetricsInfo.jsp");
				obstetricsAction.addObstetricsInfo(b);
				loggingAction.logEvent(TransactionType.ADD_OBSTETRICS, loggedInMID.longValue(), b.getMID(), "");
			response.sendRedirect("/iTrust/auth/hcp-uap/obstetricsInfo.jsp");

%>
<br />
	<div align=center>
		<span class="iTrustMessage">Information Successfully Updated</span>
	</div>
<br />
<%
		} else {
			b = new ObstetricsBean();
			loggingAction.logEvent(TransactionType.ADD_OBSTETRICS, loggedInMID.longValue(), b.getMID(), "");
		}
%>
<form id="editForm" action="addObstetricsInfo.jsp" method="post"><input type="hidden"
	name="formIsFilled" value="true">
<br />
<div align=center>
	<table id="AddObstetricsTable" align="center" class="fTable">
		<tr>
			<th colspan="4" style="text-align: center;">Obstetric History for <%= patientName %></th>
		</tr>
		<tr>
			<td class="subHeaderVertical">Year Of Conception:</td>
			<td><input type=text name="yearOfConception" maxlength="10"
				size="10" value="<%= StringEscapeUtils.escapeHtml("" + (b.getYearOfConception())) %>"> <input
				type=button value="Select Date"
				onclick="displayDatePicker('yearOfConception');"></td>
		</tr>
		<tr>
			<td class="subHeaderVertical">Weeks Pregnant-Days:</td>
			<td><input name="weeksPregnant" value="<%= StringEscapeUtils.escapeHtml("" + (b.getWeeksPregnant())) %>" type="text"></td>
		</tr>
		<tr>
			<td class="subHeaderVertical">Hours Labor:</td>
			<td><input name="hoursLabor" value="<%= StringEscapeUtils.escapeHtml("" + (b.getHoursLabor())) %>" type="text"></td>
		</tr>
		<tr>
			<td class="subHeaderVertical">Delivery Method:</td>
				<td><select name="deliveryMethod">
					<%
					String selected = "";
						for (DeliveryMethod dm : DeliveryMethod.values()) {
							selected = (dm.equals(b.getDeliveryMethod())) ? "selected=selected"
									: "";
					%>
					<option value="<%=dm.getName()%>" <%= StringEscapeUtils.escapeHtml("" + (selected)) %>><%= StringEscapeUtils.escapeHtml("" + (dm.getName())) %></option>
					<%
						}
					%>
				</select></td>
		</tr>
	</table>
</div>
<div align=center>
	<input type="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="Add Obstetrics Info">
</div>
</form>

<%@include file="/footer.jsp" %>