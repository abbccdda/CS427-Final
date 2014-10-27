<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.ObstetricsBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ObstetricsInfoAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.enums.Ethnicity"%>
<%@page import="edu.ncsu.csc.itrust.enums.BloodType"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException" %>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Obstetrics Information";
%>

<%@include file="/header.jsp"%>
<itrust:patientNav thisTitle="Obstetrics" />
<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
		out.println("pidstring is null");
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/obstetricsInfo.jsp");
		return;
	}
	
	try {
		/* If the patient id doesn't check out, then kick 'em out to the exception handler */
		ObstetricsInfoAction action = new ObstetricsInfoAction(prodDAO, pidString);
		List<ObstetricsBean> records = action.getAllObstetrics();
		%>
		
		
		<br />
		<div align=center>
			<table id="HealthRecordsTable" align="center" class="fTable">
			<%
				if(records.isEmpty()){
			%>
				<p style="font-size:20px"><i>No obstetrics records available</i></p>
			<%
				}
			%>
			</table>
		</div>
			
	
	<%
	} catch (ITrustException e) {
		%>
		<div
						style="text-align: center; width: 100%; background-color: white; border:1px solid #cc0000">
						<span style="color: #cc0000; font-size: 20px; font-weight: bold;"><%=e.getMessage() %></span>
					</div>
		<%
	}
	
	/* Now take care of updating information */
	
// 	boolean formIsFilled = request.getParameter("formIsFilled") != null
// 			&& request.getParameter("formIsFilled").equals("true");
// 	PatientBean p;
// 	if (formIsFilled) {
// 		p = new BeanBuilder<PatientBean>().build(request
// 				.getParameterMap(), new PatientBean());
// 		try {
// 			action.updateInformation(p);
// 			loggingAction.logEvent(TransactionType.DEMOGRAPHICS_EDIT, loggedInMID.longValue(), p.getMID(), "");
%>

<itrust:patientNav thisTitle="Obstetrics" />

<%@include file="/footer.jsp"%>
