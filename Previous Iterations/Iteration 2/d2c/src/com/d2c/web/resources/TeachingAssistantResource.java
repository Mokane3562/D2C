package com.d2c.web.resources;

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

import com.d2c.web.beans.TransferableTA;

@Path("/TA")
public class TeachingAssistantResource {

	@GET
	@Path("/{student_user_name}/{course_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTAInfo(@PathParam("student_user_name") String studentUserName, @PathParam("course_id") String courseID) {
		// TODO Some sql shit to get my object

		// TODO check that object exists
		boolean check = true;
		// if it exists then save it to a java object and return through
		// response
		if (check) {
			TransferableTA t = new TransferableTA();
			return Response.ok().entity(t).build();
		} else { // else return a not found
			return Response.noContent().build();
		}
	}

	@POST
	@Path("/{course_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setTA(@PathParam("student_user_name") String studentUserName, @PathParam("course_id") String courseID, TransferableTA ta) {
		// TODO make this post the course info to the DB

		try {
			return Response.created(new URI("/" + studentUserName + "/" + courseID)).build();
		} catch (URISyntaxException e) {
			return Response.serverError().build();
		}
	}
}
