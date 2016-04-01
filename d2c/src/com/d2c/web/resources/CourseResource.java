package com.d2c.web.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.d2c.util.EmptySetException;
import com.d2c.util.SQLHandler;
import com.d2c.web.beans.TransferableAccount;
import com.d2c.web.beans.TransferableCourse;
import com.d2c.web.beans.TransferableCourseInstance;
import com.d2c.web.beans.TransferableCourseInstance.Semester;

@Path("/course")
public class CourseResource {
	
	//get an abstract course from its id
	@GET
	@Path("/refid/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCourseByRefID(@PathParam("id") int refID) {
		//WARNING: STARTING SOME HEAVY SQL SHIT. ALSO MAGIC.
		try (SQLHandler sql = new SQLHandler();) {
			Object[] results = sql.getCourseByRefID(refID);
			//create the course
			TransferableCourse course = new TransferableCourse();
			course.subject = (String) results[0];
			course.number = (String) results[1];
			course.name = (String) results[2];
			course.refID = (int) results[3];
			//send created course object to client
			return Response.ok().entity(course).build();
		} catch (EmptySetException e) {
			e.printStackTrace();
			return Response.noContent().build();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			//when shit goes FUBAR
			return Response.serverError().build();
		}
	}
}
