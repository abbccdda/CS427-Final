<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.HealthRecord"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.beans.forms.HealthRecordForm"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ObstetricsInfoAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ObstetricsBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.enums.Gender"%>
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

ObstetricsInfoAction obstetricsAction = new ObstetricsInfoAction(prodDAO,pidString,loggedInMID.longValue());
String patientName = obstetricsAction.getPatientName();
boolean isMale = obstetricsAction.getPatient().getGender()==Gender.Male;

boolean formIsFilled = request.getParameter("formIsFilled") != null
	&& request.getParameter("formIsFilled").equals("true");
	ObstetricsBean b;
	
if (formIsFilled && !isMale) {
    b = new ObstetricsBean();
    b.setMID(obstetricsAction.getPatientMID());
    //Validate Year 
    boolean yearIsValid = false;
    int year=0;
    try{
    	year = Integer.parseInt(request.getParameter("yearOfConception"));
    	yearIsValid = (year >= 1900) && (year < 2050);
    }catch(Exception e){
    	yearIsValid = false;
    }
    String weeks = request.getParameter("weeksPregnant");
    boolean weeksIsValid = (weeks.charAt(2) == '-') && (weeks.length() == 4);
    double hours = Double.parseDouble(request.getParameter("hoursLabor"));
    boolean hoursIsValid = (hours >= 0.0) && (hours < 250.0);
    boolean allAreValid = yearIsValid && weeksIsValid && hoursIsValid;
    
    if (allAreValid){
        b.setYearOfConception(year);
        b.setWeeksPregnant(weeks);
        b.setHoursLabor(hours);
        b.setDeliveryMethod(request.getParameter("deliveryMethod"));
        obstetricsAction.addObstetricsInfo(b);
        loggingAction.logEvent(TransactionType.ADD_OBSTETRICS, loggedInMID.longValue(), b.getMID(), "");
        response.sendRedirect("/iTrust/auth/hcp-uap/obstetricsInfo.jsp");
    } else {
        if (!yearIsValid){
            loggingAction.logEvent(TransactionType.ADD_OBSTETRICS, loggedInMID.longValue(), b.getMID(), "");
            %>
            <div align=center>
                <span class="iTrustMessage" style="color:red">Error: Invalid year of conception</span>
            </div>
            <%
        } if (!weeksIsValid){
            loggingAction.logEvent(TransactionType.ADD_OBSTETRICS, loggedInMID.longValue(), b.getMID(), "");
            %>
            <div align=center>
                <span class="iTrustMessage" style="color:red">Error: Invalid weeks pregnant</span>
            </div>
            <%
        } if (!hoursIsValid){
            loggingAction.logEvent(TransactionType.ADD_OBSTETRICS, loggedInMID.longValue(), b.getMID(), "");
            %>
            <div align=center>
                <span class="iTrustMessage" style="color:red">Error: Invalid hours labor</span>
            </div>
            <%
        }
    }
} else if(!isMale){
    b = new ObstetricsBean();
    loggingAction.logEvent(TransactionType.ADD_OBSTETRICS, loggedInMID.longValue(), b.getMID(), "");
}
else{
	b= new ObstetricsBean();
}
if(isMale){
	%>
	<p style="font-size:20px" align="center"><i>No Obstetric Information, Male Patient</i></p>
	<%	
}
else{
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
					size="10" value="<%= StringEscapeUtils.escapeHtml("" + (b.getYearOfConception())) %>"> </td>
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
	<%
	
}

%>

<%@include file="/footer.jsp" %>
