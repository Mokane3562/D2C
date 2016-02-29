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

import com.d2c.web.beans.TransferableCourse;

@Path("/course")
public class CourseResource {
	
	@GET
	@Path("/{course_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCourseInfo(@PathParam("course_id") String courseID){
		//TODO Some sql shit to get my object
		
		//TODO check that object exists
		boolean check = true;
		//if it exists then save it to a java object and return through response
		if(check){
			TransferableCourse c = new TransferableCourse();
			return Response.ok()
					.entity(c)
					.build();
		} else { // else return a not found
			return Response.noContent().build();
		}
	}

	@POST
	@Path("/{course_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createOrUpdateCourse(@PathParam("course_id") String courseID, 
			TransferableCourse course){
		//TODO make this post the course info to the DB
		
		try {
			return Response.created(new URI("/"+courseID))
					.build();
		} catch (URISyntaxException e) {
			return Response.serverError().build();
		}
	}
}
