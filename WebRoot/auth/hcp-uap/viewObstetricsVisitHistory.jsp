<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientOfficeVisitHistoryAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricsVisitHistoryAction"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Obstetrics Office Visit";
%>

<%@include file="/header.jsp" %>

<%
ViewObstetricsVisitHistoryAction action = new ViewObstetricsVisitHistoryAction(prodDAO,loggedinMID.longValue());
//List<PatientVisitBean> patientVisits = action.getPatients();
loggingAction.logEvent(TransactionType.PATIENT_LIST_VIEW, loggedInMID, 0, "");

%>

<%@include file="/footer.jsp" %>
