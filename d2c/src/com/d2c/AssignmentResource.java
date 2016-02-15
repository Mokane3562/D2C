package com.d2c;

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

@Path("/assignment")
public class AssignmentResource {
	
	@GET
	@Path("/{course_id}/{assignment}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCourseInfo(@PathParam("course_id") String courseID, 
			@PathParam("assignment") String assignment){
		//Some sql shit to get my object
		
		//check that object exists
		boolean check = true;
		//if it exists then save it to a java object and return through response
		if(check){
			TransferableAssignment a = new TransferableAssignment();
			return Response.ok()
					.entity(a)
					.build();
		} else { // else return a not found
			return Response.noContent().build();
		}
	}

	@POST
	@Path("/{course_id}/{assignment}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createOrUpdateAssignment(@PathParam("course_id") String courseID, 
			@PathParam("assignment") String assignment){
		//TODO make this post the course info to the DB
		
		try {
			return Response.created(new URI("/"+courseID+"/"+assignment))
					.build();
		} catch (URISyntaxException e) {
			return Response.serverError().build();
		}
	}
}
