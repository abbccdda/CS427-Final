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
String day = request.getParameter("userSer");
%>
    <br />
    <div align=center>
	<br />
	<div align=center>
	<table id="ObstetricsTable" align="center" class="fTable">
	<%
//If OBGYN the "Add Obstetrics Info" Button and "Document Obstetrics Visit" will show
if (isOBGYN){
	%>			
	<input type="submit" id="submitAdd" name="submitAdd" value="Add Obstetrics Info" 
	onclick="window.location='addObstetricsInfo.jsp'"> &nbsp&nbsp
	
	<input type="submit" id="submitAdd" name="submitAdd" value="Document Obstetrics Visit" 
	onclick="window.location='addObstetricsVisit.jsp'">
	<%
}
%>
<br />
<div align=center>
<table id="ObstetricsRecordsTable" align="center" class="fTable">
<%
if(!records.isEmpty()){
	if(day == null){
		%>
		<form id="userSearchForm" action="obstetricsInfo.jsp" >
			 
			<td><input type="submit" value="Calculate the EDD:" /></td>
			<td><input name = "userSer" type="text" maxlength="20" size="50" value="enter a valid date: dd/mm/yy" /></td>
		</form>
		<%
	}
	else{
		String[] res;
		try{
			res=ObstetricsInfoAction.calculateEDDAndWeek(day);
		}
		catch(Exception e){
			%>
			<p style="font-size:20px"><i><%= "Invalid Input, Please try again" %></i></p>
			<form id="again" action="obstetricsInfo.jsp" >
				<td><input type="submit" value="Calculate another date" /></td>
			</form>
			<%
			throw e;
		}
			%>
				<p style="font-size:20px"><i> EDD(estimated due date) is <%= res[0] %></i></p>
				<p style="font-size:20px"><i> weeks of pregnant is <%= res[1] %></i></p>
				<p style="font-size:20px"><i> days difference is <%= res[2] %></i></p>
				<form id="again" action="obstetricsInfo.jsp" >
					<td><input type="submit" value="Calculate another date" /></td>
				</form>
			<%
	}
	%>
	<tr>
		<th colspan="4" style="text-align: center;">Obstetric History</th>
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
	if(obstetricsAction.getPatient().getGender()==Gender.Male){
		%>
			<p style="font-size:20px"><i>No Obstetric Information, Male Patient</i></p>
		<%
	}
	else{
		%>
		<p style="font-size:20px"><i>No Obstetric Information</i></p>
		<%
	}
}
%>
		</table>
		</div>
	</table>
	<br />
</div>
<br />
<br />
<br />
<br />

<%@include file="/footer.jsp" %>
