<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ObstetricsVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ObstetricsInfoAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientOfficeVisitHistoryAction"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Obstetrics Office Visit";
%>

<%@include file="/header.jsp" %>

<%
/* Require a Patient ID first */
String pidString = (String) session.getAttribute("pid");
System.out.println(pidString);
/* If the patient id doesn't check out, then kick 'em out to the exception handler in the Action Class*/
ObstetricsInfoAction action = new ObstetricsInfoAction(prodDAO,pidString,loggedInMID.longValue());
List<ObstetricsVisitBean> obstetricsVisits = action.getObstetricsVisits();
loggingAction.logEvent(TransactionType.PATIENT_LIST_VIEW, loggedInMID, 0, "");



%>

<%@include file="/footer.jsp" %>
