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

import com.d2c.web.beans.TransferableStudent;

@Path("/student")
public class StudentResource {

	@GET
	@Path("/{student_user_name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentInfo(@PathParam("student_user_name") String studentUserName) {
		// TODO Some sql shit to get my object

		// TODO check that object exists
		boolean check = true;
		// if it exists then save it to a java object and return through
		// response
		if (check) {
			TransferableStudent s = new TransferableStudent();
			return Response.ok().entity(s).build();
		} else { // else return a not found
			return Response.noContent().build();
		}
	}

	@POST
	@Path("/{student_user_name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createOrUpdateStudent(@PathParam("student_user_name") String studentUserName, TransferableStudent student) {
		// TODO make this post the course info to the DB

		try {
			return Response.created(new URI("/" + studentUserName)).build();
		} catch (URISyntaxException e) {
			return Response.serverError().build();
		}
	}
}
