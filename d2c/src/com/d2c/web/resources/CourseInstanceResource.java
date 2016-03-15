package com.d2c.web.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.d2c.util.SQLHandler;
import com.d2c.web.beans.TransferableCourseInstance;

@Path("/course")
public class CourseInstanceResource {

	@GET
	@Path("/{crn}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCourseInfo(@PathParam("crn") String crn) {
		SQLHandler sql = null;
		try {
			//Get the course info from the database
			sql = new SQLHandler();
			ResultSet results = sql.getCourseInfo(Integer.parseInt(crn));
			boolean has_data = results.first();
			if(has_data){
				
				
				TransferableCourseInstance c = new TransferableCourseInstance();
				return Response.ok().entity(c).build();
			} else {
				return Response.noContent().build();
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return Response.serverError().build();
		} finally {
			try {
				sql.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@POST
	@Path("/{crn}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createOrUpdateCourse(@PathParam("crn") String crn, TransferableCourseInstance course) {
		// TODO make this post the course info to the DB

		try {
			return Response.created(new URI("/" + crn)).build();
		} catch (URISyntaxException e) {
			return Response.serverError().build();
		}
	}
}
