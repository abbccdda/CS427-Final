<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.action.ObstetricsInfoAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.HealthRecord"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.beans.forms.HealthRecordForm"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ObstetricsBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ObstetricsVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException" %>
<%@include file="/global.jsp"%>
<%
pageTitle = "iTrust - View Obstetric Record";
%>
<%@include file="/header.jsp" %>
<itrust:patientNav thisTitle="Obstetrics" />
<%
// Require a Patient ID first
String pidString = (String)session.getAttribute("pid");
if (pidString == null || 1 > pidString.length()) {
	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/obstetricsInfo.jsp");
   	return;
}

ObstetricsInfoAction obstetricsAction = new ObstetricsInfoAction(prodDAO,pidString,loggedInMID.longValue());
String patientName = obstetricsAction.getPatientName();
//Get all of the patient's health records
List<ObstetricsBean> records = obstetricsAction.getAllObstetricsRecords();
//Save the list of health records in the session
session.setAttribute("obstetricsRecords", records);
boolean isOBGYN = obstetricsAction.getPersonnelSpecialty().equalsIgnoreCase("OB/GYN");
boolean isMale = obstetricsAction.getPatient().getGender()==Gender.Male;
%>
    <br />
    <div align=center>
	<br />
	<div align=center>
	<table id="ObstetricsTable" align="center" class="fTable">
	<%
//If OBGYN the "Add Obstetrics Info" Button and "Document Obstetrics Visit" will show
if (isOBGYN && !isMale){
	%>			
	<input type="submit" id="submitAdd" name="submitAdd" value="Add Obstetrics Info" 
	onclick="window.location='addObstetricsInfo.jsp'"> &nbsp&nbsp
	
	<input type="submit" id="submitAdd" name="submitAdd" value="Document Obstetrics Visit" 
	onclick="window.location='addObstetricsVisit.jsp'">
	<%
}
String day = request.getParameter("userSer");
if(day==null && !isMale){ // The user has not entered in an EDD
	%>
	<br><br>
	<div align=center>
			<form id="userSearchForm" action="obstetricsInfo.jsp" >
				<td><input type="submit" value="Calculate the EDD:" /></td>
				<td><input name = "userSer" type="text" maxlength="20" size="50" value="Last menstrual period: dd/mm/yyyy" /></td>
			</form>
	</div>
	<%
}
else if (!isMale){
	//The user has already clicked the Calculate the EDD: button
	String[] res=null;
	boolean invalidEDDInput =false;
	try{
		System.out.println("day: " + day);
		res=ObstetricsInfoAction.calculateEDDAndWeek(day);
	}
	catch(Exception e){
		if(day.length()==8){
			%>
			<p style="font-size:20px"><i><%= "Invalid Input, Please enter mm/dd/yyyy " %></i></p>
			<form id="again" action="obstetricsInfo.jsp" >
				<td><input type="submit" value="Calculate another date" /></td>
			</form>
			<%	
		}
		else{
			%>
			<p style="font-size:20px"><i><%= "Invalid Input, Please try again" %></i></p>
			<form id="again" action="obstetricsInfo.jsp" >
				<td><input type="submit" value="Calculate another date" /></td>
			</form>
			<%
		}
		
		invalidEDDInput =true;	
	}
	if(!invalidEDDInput){
		%>
		<p style="font-size:20px"><i> EDD(estimated due date): <%= res[0] %></i></p>
		<p style="font-size:20px"><i> <%= res[1]%> weeks <%= " and " +res[1]  %> days pregnant</i></p>
		<form id="again" action="obstetricsInfo.jsp" >
			<td><input type="submit" value="Calculate another date" /></td>
		</form>
		<%	
	}
}
if(!records.isEmpty()){
	%>
	<div align=center>
	<table id="ObstetricsRecordsTable" align="center" class="fTable">
	<tr>
		<th colspan="4" style="text-align: center;">Obstetrics Information</th>
	</tr>
	<tr class = "subHeader">
		<td>Delivery Method</td>
		<td>Weeks Pregnant - Days</td>
		<td>Year of Conception</td>
		<td>Hours of Labor</td>
	</tr>
	<%
	for(ObstetricsBean bean : records){
		%>
		<!-- 
		Get the year of contraception, the number of weeks pregnant, the hours in labor, and the delivery method in a table
		-->
		<tr>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (bean.getDeliveryMethod())) %></td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (bean.getWeeksPregnant())) %></td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (bean.getYearOfConception())) %></td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (bean.getHoursLabor())) %></td>
		</tr>
		<%
	}
}
else{
	//Records was empty -- We can put our exception cases here (like being a male)
	//If male display a seperate message
	if(isMale){
		%>
			<p style="font-size:20px"><i>No Obstetric Information, Male Patient</i></p>
		<%
	}
	else{
		%>
		<p style="font-size:20px"><i>No Obstetric Information</i></p>
		<%
	}
	%>
	</table></div>
	<%
}

//Obstetrics Visit History Table
List<ObstetricsVisitBean> visitHistory = obstetricsAction.getObstetricsVisits();
if(!isMale && visitHistory.size()!=0){
	%> 
	<br>
	<div align=center>
	<table id="ObstetricsVisitHistoryRecords" align="center" class="fTable">
	<tr>
		<th colspan="5" style="text-align: center;">Obstetrics Visit History</th>
	</tr>
	<tr class = "subHeader">
		<td>Visit Date</td>
		<td>Weeks Pregnant - Days</td>
		<td>Blood Pressure</td>
		<td>Fetal Heart Rate</td>
		<td>Fundal Uterus Height</td>
	</tr>
	<%
	for(ObstetricsVisitBean bean : visitHistory){
		//For each record in the visit history populate a row in the table
		%>
		
		<tr>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (bean.getVisitDate())) %></td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (bean.getWeeksPregnant())) %></td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (bean.getBloodPressure())) %></td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (bean.getFetalHeartRate())) %></td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (bean.getFundalHeightUterus())) %></td>
		</tr>
		<%
	}
	%>
	</table></div>
	<%
}
%>
		
	</table>
	<br />
</div>
<br />
<br />
<br />
<br />

<%@include file="/footer.jsp" %>
