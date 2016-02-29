package com.d2c.web.resource;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.d2c.web.beans.TransferableSubmission;

@Path("/submission")
public class SubmissionResource {
	
	@GET
	@Path("/{course_id}/{student_user_name}/{assignment}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubmissionInfo(@PathParam("course_id") String courseID, 
	                 @PathParam("student_user_name") String studentUserName, 
	                 @PathParam("assignment") String assignment){
		//TODO Some sql shit to get my object
		
		//TODO check that object exists
		boolean check = true;
		//if it exists then save it to a java object and return through response
		if(check){
			TransferableSubmission s = new TransferableSubmission();
			return Response.ok()
					.entity(s)
					.build();
		} else { // else return a not found
			return Response.noContent().build();
		}
	}

	@POST
	@Path("/{course_id}/{student_user_name}/{assignment}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response uploadOrUpdateSubmission(@PathParam("course_id") String courseID, 
	                @PathParam("student_user_name") String studentUserName, 
	                @PathParam("assignment") String assignment, TransferableSubmission submission){
		//TODO make this post the course info to the DB
		
		try {
			return Response.created(new URI("/"+courseID+"/"+studentUserName+"/"+assignment))
					.build();
		} catch (URISyntaxException e) {
			return Response.serverError().build();
		}
	}
}
