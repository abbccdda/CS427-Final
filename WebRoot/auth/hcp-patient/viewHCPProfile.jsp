<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="edu.ncsu.csc.itrust.action.DeclareHCPAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewHCPProfileAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ReviewsBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.HospitalBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.action.ReviewsAction"%>
<%@include file="/global.jsp" %>
<%@include file="/header.jsp" %>
<%
pageTitle = "iTrust - View HCP Profile";
boolean isHCP = userRole.equals("hcp");
//loggingAction.logEvent(TransactionType.OUTBOX_VIEW, loggedInMID.longValue(), 0L, "");


String mid = request.getParameter("expertID");

if(mid == null){
	// If there's no expertID, there's no Profile to view, so redirect to 
	// allow the user to research for an expert to view the profile of. 
	response.sendRedirect("/iTrust/auth/patient/expertNameSearch.jsp");
	return;
}
long expertID = Long.parseLong(mid);
ViewHCPProfileAction viewHCPAction = new ViewHCPProfileAction(prodDAO);
PersonnelDAO pDAO = new PersonnelDAO(prodDAO);
PersonnelBean hcp = pDAO.getPersonnel(Long.parseLong(mid)); 
%>
<!-- This wrapper seperates basic info and assigned hospitals -->
<div id="wrapper">
<!-- Begin Primary HCP Information -->
<div id="left">
<div class="col-sm-6">
<div class="panel panel-primary panel-notification">
<div class="panel-heading"><h3 class="panel-title"> <%= hcp.getFullName() %></h3></div>
<div class="panel-body">
		<!-- Photo if one exists, noPhoto picture if not -->
		<object class="profilepic" data="/iTrust/image/user/<%= StringEscapeUtils.escapeHtml(""+(hcp.getMID())) %>.png" type="image/png">
	    	<img class="profilepic" src="/iTrust/image/user/noPhoto.png" />
	  	</object>
		<h4>
		<% if(hcp.getSpecialty()!=null){%>
			 <%=StringEscapeUtils.escapeHtml("" + (hcp.getSpecialty() )) %><br />
		<% }%>
		<%= StringEscapeUtils.escapeHtml("" + (hcp.getPhone() )) %><br />
		<%= StringEscapeUtils.escapeHtml("" + (hcp.getEmail() )) %><br />
		<%= StringEscapeUtils.escapeHtml("" + (hcp.getStreetAddress1() )) %><br />
		<%= StringEscapeUtils.escapeHtml("" + (hcp.getCity())) %><br />
		<%= StringEscapeUtils.escapeHtml("" + (hcp.getZip())) %><br />
	</h4>
	<%
%>
</div>
</div>
</div>
</div>
</div>
<!-- End HCP Information -->

<!-- Begin Assigned Hospitals Information -->
<div id="right">
<div class="col-sm-6">
<div class="panel panel-primary panel-notification">
<div class="panel-heading"><h3 class="panel-title"> Assigned Hospitals </h3></div>
<div class="panel-body">
	<%
		List<HospitalBean> hospitals = viewHCPAction.findHospitalsAssignedToHCP(expertID);
		%> <ol> <%
		for(HospitalBean hospital : hospitals){	
			%> <li> <%
			%><p><i>Hospital Name: </i> <%=hospital.getHospitalName()%></p>
			<% if (hospital.getHospitalAddress() != null){ %>
				<p><i>Address: </i> <%=hospital.getHospitalAddress()%></p>
			<%} %>
			<% if (hospital.getHospitalCity() != null && hospital.getHospitalState()!=null &&
					hospital.getHospitalZip()!=null){ %>
					<p><%= hospital.getHospitalCity() + ", " + hospital.getHospitalState() 
						+ "  " + hospital.getHospitalZip()%></p>
			<%} %> 
			<br></li><%  
		} %>
		</ol>
</div>
</div>
</div>
</div>
<!-- End Assigned Hospitals Information -->

<!-- Begin Reviews Section -->

<div class="col-sm-12">
<div class="panel panel-primary panel-notification">
<div class="panel-heading"><h3 class="panel-title"> <%= hcp.getFirstName() %>'s Reviews</h3></div>
<div class="panel-body">
<% 
ReviewsAction reviewsAction = new ReviewsAction(prodDAO, loggedInMID.longValue()); 
		String reviewTitle = request.getParameter("title");
		String reviewRating = request.getParameter("rating");
		String description = request.getParameter("description");
	
		if(reviewTitle != null && reviewRating != null && description != null)
		{
			loggingAction.logEvent(TransactionType.SUBMIT_REVIEW, loggedInMID, expertID, "");
			ReviewsBean review = new ReviewsBean();
			review.setDescriptiveReview(description);
			review.setRating(Integer.parseInt(reviewRating));
			review.setTitle(reviewTitle);
			review.setMID(loggedInMID.longValue());
			review.setPID(expertID);
			review.setDateOfReview(new Date());
			
			reviewsAction.addReview(review);
			
		}
	
	if(expertID != -1)
	{
		List<ReviewsBean> reviews = reviewsAction.getReviews(expertID);
		PersonnelBean physician = reviewsAction.getPhysician(expertID);
		%><h1>Reviews for <%=physician.getFullName()%></h1>
		<br>
		<%
		if(reviews.size() == 0)
		{
			%><p><i> <%=physician.getFullName() %> has not been reviewed yet.</i></p><%
		}
		for(ReviewsBean reviewBean : reviews )
		{ %> 
			<div class="grey-border-container">
				<p> <b><%= reviewBean.getTitle()%> </b> 
				
					<%
					for(int i = 0 ; i < 5 ; i++)
					{ 
						if(i < reviewBean.getRating())
						{
							%> <span class="glyphicon glyphicon-star" style="color:red;"></span><% 
						}
						else
						{
							%> <span class="glyphicon glyphicon-star-empty"></span><% 
						}
						
					}
					
					
					%>
				    </p>
				<p><%= reviewBean.getDescriptiveReview() %> </p>
				<p><%= reviewBean.getDateOfReview()%></p>
			</div>	
		
	  <%}
	  
	  
	  		if(reviewsAction.isAbleToRate(expertID))
	  		{
	  %>
	  	<a href="#addModal" role="button" class="btn btn-primary" data-toggle="modal">Add a Review</a>
 
		
				<div id="addModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="addReview" aria-hidden="true">
					<div class="modal-dialog">
					<div class="modal-content">
					
						<div class="modal-header" >
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true" >x</button>
						<h3 class="modal-title" id="addReview">Add a Review</h3>
					 </div>
					 <div class="modal-body">
					 	<form class="form-horizontal" role="form" method="post" id="mainForm" name="mainForm">
					 	<div class="form-group">
					 	<p> 
					 		<b>Title: </b> <input class="form-control" type="text" width="1" name="title">
					 	</p>
					 	 </div>
					 	<br>
					 	<div class="form-group">
					 	<b>Rating (out of 5): </b>
						<select class="form-control" name="rating">
						<option value="1">1</option>
						<option value= "2">2</option>
						<option value="3">3</option>			
						<option value="4">4</option>
						<option value="5">5</option>
						</select>
						</div>
						<br>
						<br>
						<div class="form-group">
						<p>
							<b>Describe your experience: </b> <textarea style="margin-top:5px;width:100%;" rows="4" cols="80" name="description" class="form-control"></textarea> 
						</p>
						</div>
					 <div class="modal-footer">
   					 <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
    				<button type="submit" name="addReview" value="Add review" class="btn btn-primary">Add review</button>
    				</div>
    				</form>
    				
  					</div>
  					</div>
  					</div>
				</div>

				<%
				}
				 %>

<% }
	else
	{
	%> <h1>User does not exist!</h1> <% 
	}
	
 %>
</div>
</div>
</div>
<!-- End Reviews-->

</div>
<%@include file="/footer.jsp" %>
