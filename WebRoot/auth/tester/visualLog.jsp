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

<%
	pageTitle = "iTrust - Tester Home";
%>

<%@include file="/header.jsp"%>

<%

Date startDate = new Date(Long.parseLong(session.getAttribute("startDate").toString()));
Date endDate = new Date(Long.parseLong(session.getAttribute("endDate").toString()));
Object transactionType = session.getAttribute("transactionType");
Object userRole1 = session.getAttribute("userRole1");
Object userRole2 = session.getAttribute("userRole2"); 

/* Date startDate = new Date();
Date endDate = new Date();
Object transactionType = "sdfas";
Object userRole1 = "sad";
Object userRole2 = "sdfasdf"; */

String [] sug =new String[4];
sug[0] = "111,123,444,33";
sug[1] = "444,123,111,33";
sug[2] = "111,33,444,123";
sug[3] = "33,123,444,111";

for (int graph = 0; graph < 4; graph++) {
	
	HashMap<String, Long> map = new HashMap<String, Long>();
	map = null;
	String title = "TEST";

	if (graph == 0) {
		map = DAOFactory.getProductionInstance()
		.getTransactionDAO()
		.getLogForRole(userRole1.toString().toLowerCase(),
		transactionType.toString(), startDate, endDate, true);

		title = "Role+Versus+Transaction+Count+For+Logged+In+User";
	} else if (graph == 1) {
		map = DAOFactory.getProductionInstance()
		.getTransactionDAO()
		.getLogForRole(userRole2.toString().toLowerCase(),
		transactionType.toString(), startDate, endDate, false);
		title = "Role+Versus+Transaction+Count+For+Secondary+User";

	} else if (graph == 2) {
		map = DAOFactory.getProductionInstance()
		.getTransactionDAO().getLogByTime(userRole1.toString().toLowerCase(), userRole2.toString().toLowerCase(), transactionType.toString(), startDate, endDate);
		
		title = "Month_Year+Versus+Transaction+Count";

		
	} else if (graph == 3) {
		map = DAOFactory.getProductionInstance()
		.getTransactionDAO().getLogByType(userRole1.toString().toLowerCase(), userRole2.toString().toLowerCase(), transactionType.toString(), startDate, endDate);
		title = "Transaction+Type+Versus+Transaction+Count";

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
	
	System.out.print(values);
	values = sug[graph];
	//values = values.substring(0, values.length() - 1);
	
	String chartURL = "https://chart.googleapis.com/chart?chs=800x200&amp;chd=t:";
	chartURL += values;
	chartURL += "&amp;chds=a&amp;cht=bvs&amp;chxt=x,y&amp;chxl=0:";
	
	for (String role : map.keySet()) {
		labels += "|" + role;
	}
	
	chartURL += labels;
	chartURL += "&amp;chts=1FE89A,20,l";
	chartURL += "&amp;chtt=";
	chartURL += title;
	chartURL += "&amp;chbh=a,50,0";
%><img src=<%=chartURL%>></img><%
}

%>









<%@include file="/footer.jsp"%>