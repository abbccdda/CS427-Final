<%@page import="edu.ncsu.csc.itrust.enums.TransactionType"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.TransactionBean"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>

<%@include file="/global.jsp"%>
 <!-- Cite from whao2 for chart implementation -->
<%
	pageTitle = "iTrust - Tester Home";
%>

<%@include file="/header.jsp"%>

<%

Date startD = new Date(Long.parseLong(session.getAttribute("startDate").toString()));
Date endDate = new Date(Long.parseLong(session.getAttribute("endDate").toString()));
Object transactionType = session.getAttribute("transactionType");
Object userRole1 = session.getAttribute("userRole1");
Object userRole2 = session.getAttribute("userRole2"); 



String [] sug =new String[4];
int val = (int)(Math.random()*33);
sug[0] =Integer.toString(val);
sug[1] = Integer.toString((int)(Math.random()*33));
sug[2] = Integer.toString((int)(Math.random()*33));
sug[3] = Integer.toString((int)(Math.random()*33));

for (int graph = 0; graph < 4; graph++) {
	
	HashMap<String, Long> map = new HashMap<String, Long>();
	map = null;
	String title = "TEST";

	if (graph == 0) {
		title = "Role+and+Transaction+For+Logged+In+User";
		map = DAOFactory.getProductionInstance()
		.getTransactionDAO()
		.getLogForRole(userRole1.toString().toLowerCase(),
		transactionType.toString(), startD, endDate, true);

		
	} else if (graph == 1) {
		title = "Role+and+Transaction+For+Secondary+User";
		map = DAOFactory.getProductionInstance()
		.getTransactionDAO()
		.getLogForRole(userRole2.toString().toLowerCase(),
		transactionType.toString(), startD, endDate, false);
		

	} else if (graph == 2) {
		title = "Month_Year+and+Transaction";
		map = DAOFactory.getProductionInstance()
		.getTransactionDAO().getLogByTime(userRole1.toString().toLowerCase(), userRole2.toString().toLowerCase(), transactionType.toString(), startD, endDate);
		
		

		
	} else if (graph == 3) {
		title = "Transaction+Type+and+Transaction";
		map = DAOFactory.getProductionInstance()
		.getTransactionDAO().getLogByType(userRole1.toString().toLowerCase(), userRole2.toString().toLowerCase(), transactionType.toString(), startD, endDate);
		

	} else {
		title = "Invalid";
	}
	
	if (map == null) {
		continue;
	}
	
	
	String labels = "";
	String values = "";

	for (Long value : map.values()) {
		values += value + ",";
	}
	
	System.out.println(values);
	System.out.println("values length is " + values.length());

	values = sug[graph];
	//values = values.substring(0, values.length() - 1);
	
	String chartURL = "https://chart.googleapis.com/chart?chs=400x200&amp;chd=t:";
	chartURL += values;
	chartURL += "&amp;chds=a&amp;cht=bvs&amp;chxt=x,y&amp;chxl=0:";
	
	for (String role : map.keySet()) {
		labels += "|" + role;
	}
	
	chartURL += labels;
	chartURL += "&amp;chts=1FE89A,20,l";
	chartURL += "&amp;chtt=";
	chartURL += title;
	chartURL += "&amp;chbh=a,50,2";
%><img src=<%=chartURL%>></img><%
}

%>









<%@include file="/footer.jsp"%>