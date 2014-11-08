<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.ParseException"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.HealthRecord"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.beans.forms.HealthRecordForm"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ObstetricsInfoAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ObstetricsVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.enums.DeliveryMethod"%>
<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Add Obstetric Visit Record";
%>

<%@include file="/header.jsp" %>
<itrust:patientNav thisTitle="Obstetric Record" />
<%
//Require a Patient ID first
String pidString = (String)session.getAttribute("pid");
if (pidString == null || 1 > pidString.length()) {
	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/addObstetricsVisit.jsp");
	return;
}
ObstetricsInfoAction obstetricsAction = new ObstetricsInfoAction(prodDAO,pidString,loggedInMID.longValue());
String patientName = obstetricsAction.getPatientName();

boolean formIsFilled = request.getParameter("formIsFilled") != null
	&& request.getParameter("formIsFilled").equals("true");
	ObstetricsVisitBean b;
	
if (formIsFilled) {
    b = new ObstetricsVisitBean();
    b.setMID(obstetricsAction.getPatientMID());
    
    //Validate the Date
 	boolean validVisitDate = true;
	String visitDate = request.getParameter("visitDate");
	//This is to fix the fact the date comes in MM-DD-YYYY
	// if the user selects the date from the calandar and i can't fix it in javascript DatePicker.js
	if(visitDate.length()==10){
		visitDate = visitDate.substring(0,6) + visitDate.substring(8);	
	}
	try{
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yy");
		Date date = formatter.parse(visitDate);
	}
	catch(ParseException e){
		validVisitDate =false;
	}
	//Validate Weeks Pregnant
	String weeks = request.getParameter("weeksPregnant");
    boolean validWeeks = (weeks.charAt(2) == '-')&& (weeks.length() == 4);
    int days = Integer.parseInt(weeks.charAt(3)+"");
    validWeeks = (validWeeks && days<7 && days>=0);
    //Validate Blood Pressure with regex
    String bloodPressureString = request.getParameter("bloodPressure");
    String BLOOD_PRESSURE_PATTERN = "[0-9]{2,3}?[/\\\\][0-9]{2,3}?";
    Pattern bpPattern = Pattern.compile(BLOOD_PRESSURE_PATTERN);
    Matcher bpMatcher = bpPattern.matcher(bloodPressureString);
    boolean validBloodPressure = bpMatcher.matches();
    System.out.println("bp: " +bloodPressureString);
    //Validate Heart Rate
    int fetalHeartRate=0;
    boolean validFetalHeartRate;
    try{
    	fetalHeartRate = Integer.parseInt(request.getParameter("fetalHeartRate"));
    	validFetalHeartRate = (fetalHeartRate>30 && fetalHeartRate<200 );
    }
    catch(Exception e){
    	validFetalHeartRate = false;
    }
  	//Validate fundalHeightUterus
    double fundalHeightUterus=0;
    boolean validFundalHeightUterus;
    try{
    	fundalHeightUterus = Double.parseDouble(request.getParameter("fundalHeightUterus"));
    	validFundalHeightUterus = fundalHeightUterus>0;
    }
    catch(Exception e){
    	validFundalHeightUterus = false;
    }
	
    boolean allAreValid = validWeeks && validBloodPressure && validFetalHeartRate && validFundalHeightUterus;
  
    if (allAreValid){
        b.setWeeksPregnant(weeks);
        b.setVisitDate(visitDate);
        b.setBloodPressure(bloodPressureString);
        b.setFetalHeartRate(fetalHeartRate);
        b.setFundalHeightUterus(fundalHeightUterus);
        obstetricsAction.addObstetricsVisitInfo(b);
        loggingAction.logEvent(TransactionType.ADD_OBSTETRICS_VISIT, loggedInMID.longValue(), b.getMID(), "");
        response.sendRedirect("/iTrust/auth/hcp-uap/viewObstetricsVisitHistory.jsp");
    } else {
        if (!validWeeks){
            loggingAction.logEvent(TransactionType.ADD_OBSTETRICS_VISIT, loggedInMID.longValue(), b.getMID(), "");
            %>
            <div align=center>
                <span class="iTrustMessage" style="color:red">Error: Invalid weeks pregnant</span>
            </div>
            <%
        } if (!validBloodPressure){
            loggingAction.logEvent(TransactionType.ADD_OBSTETRICS_VISIT, loggedInMID.longValue(), b.getMID(), "");
            %>
            <div align=center>
                <span class="iTrustMessage" style="color:red">Error: Invalid Blood Pressure</span>
            </div>
            <%
        } if (!validFetalHeartRate){
            loggingAction.logEvent(TransactionType.ADD_OBSTETRICS_VISIT, loggedInMID.longValue(), b.getMID(), "");
            %>
            <div align=center>
                <span class="iTrustMessage" style="color:red">Error: Invalid Fetal Heart Rate</span>
            </div>
            <%
        } if (!validFundalHeightUterus){
        	loggingAction.logEvent(TransactionType.ADD_OBSTETRICS_VISIT, loggedInMID.longValue(), b.getMID(), "");
            %>
            <div align=center>
                <span class="iTrustMessage" style="color:red">Error: Invalid Fundal Height Uterus</span>
            </div>
            <%
        }
    }
} else {
    b = new ObstetricsVisitBean();
    System.out.println(b.getMID());
    loggingAction.logEvent(TransactionType.ADD_OBSTETRICS_VISIT, loggedInMID.longValue(), b.getMID(), "");
}
//Default values for making the form look pretty
if(b.getWeeksPregnant() == null)b.setWeeksPregnant("00-0");
if(b.getVisitDate() == null){
	String visitDate = "";
	Date today = new Date();
	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	visitDate = formatter.format(today);
	if(visitDate.length()==10){
		visitDate = visitDate.substring(0,6) + visitDate.substring(8);	
	}
	b.setVisitDate(visitDate);
}
if(b.getBloodPressure() == null){
	b.setBloodPressure("100/50");
}
%>
<form id="editVisitForm" action="addObstetricsVisit.jsp" method="post"><input type="hidden" name="formIsFilled" value="true">
<br />
<div align=center>
	<table id="AddObstetricsVisitTable" align="center" class="fTable">
		<tr>
			<th colspan="4" style="text-align: center;">Add Obstetrics Visit Info For <%= patientName %></th>
		</tr>
		<tr>
			<td class="subHeaderVertical">Visit Date</td>
			<td><input type=text name="visitDate" maxlength="8"
				size="8" value="<%= StringEscapeUtils.escapeHtml("" + (b.getVisitDate())) %>"> <input
				type=button value="Select Date"
				onclick="displayDatePicker('visitDate',false,'mm-dd-yy','/');"></td>
		</tr>
		<tr>
			<td class="subHeaderVertical">Weeks Pregnant-Days:</td>
			<td><input name="weeksPregnant" value="<%= StringEscapeUtils.escapeHtml("" + (b.getWeeksPregnant())) %>" type="text"></td>
		</tr>
		<tr>
			<td class="subHeaderVertical">BloodPressure (mg/HL):</td>
			<td><input name="bloodPressure" value="<%= StringEscapeUtils.escapeHtml("" + (b.getBloodPressure())) %>" type="text"></td>
		</tr>
		<tr>
			<td class="subHeaderVertical">Fetal Heart Rate:</td>
			<td><input name="fetalHeartRate" value="<%= StringEscapeUtils.escapeHtml("" + (b.getFetalHeartRate())) %>" type="number"></td>
		</tr>
		<tr>
			<td class="subHeaderVertical">Fundal Height Uterus:</td>
			<td><input name="fundalHeightUterus" value="<%= StringEscapeUtils.escapeHtml("" + (b.getFundalHeightUterus())) %>" type="text"></td>
		</tr>
	</table>
</div>
<div align=center>
	<input type="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="Add Obstetrics Visit Info">
</div>
</form>

<%@include file="/footer.jsp" %>
