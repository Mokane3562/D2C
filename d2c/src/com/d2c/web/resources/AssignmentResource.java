package com.d2c.web.resources;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.d2c.web.beans.TransferableAssignment;
import com.d2c.web.beans.TransferableCourseInstance;
import com.d2c.web.beans.TransferableCourseInstance.Semester;

@Path("/assignment")
public class AssignmentResource {

	@GET
	@Path("/refid/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAssignmentByRefID(@PathParam("id") int refID) {
		try (SQLHandler sql = new SQLHandler();) {
			Object[] results = sql.getCourseInstByRefID(refID);
			//create the course instance
			TransferableAssignment assignment = new TransferableAssignment();
			assignment.assignmentNum = (int) results[0];
			assignment.dueDate = (Timestamp) results[1];
			assignment.fileIDs = (int[]) results[2];
			assignment.courseInstID = (int) results[3];
			assignment.refID = (int) results[4];
			//send created course instance object to client
			return Response.ok().entity(assignment).build();
		} catch (EmptySetException e) {
			System.out.println(e);
			e.printStackTrace();
			return Response.noContent().build();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			//when shit goes FUBAR
			return Response.serverError().build();
		}
	}
	
	/*@GET
	@Path("/{crn}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAssignments(@PathParam("crn") String crn) {
		//start SQL shit	
		try (SQLHandler sql = new SQLHandler();) {
			List<Object[]> results = sql.getAssignments(crn);
			//create the map
			List<TransferableAssignment> assignments = new ArrayList<>();
			for (Object[] row: results) {
				TransferableAssignment assignment = new TransferableAssignment();
				assignment
				//assignments.add(assignment);
			}
			return Response.ok().entity(roles).build(); 
		} catch (EmptySetException e) {
			e.printStackTrace();
			return Response.noContent().build();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			//when shit goes FUBAR
			return Response.serverError().build();
		} 
	}*/
	
	/*@GET
	@Path("/{course_id}/{assignment}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCourseInfo(@PathParam("course_id") String courseID, @PathParam("assignment") String assignment) {
		// Some sql shit to get my object

		// check that object exists
		boolean check = true;
		// if it exists then save it to a java object and return through
		// response
		if (check) {
			TransferableAssignment a = new TransferableAssignment();
			return Response.ok().entity(a).build();
		} else { // else return a not found
			return Response.noContent().build();
		}
	}*/

	/*@POST
	@Path("/{course_id}/{assignment}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createOrUpdateAssignment(@PathParam("course_id") String courseID, @PathParam("assignment") String assignment, TransferableAssignment context) {
		// TODO make this post the course info to the DB

		try {
			return Response.created(new URI("/" + courseID + "/" + assignment)).build();
		} catch (URISyntaxException e) {
			return Response.serverError().build();
		}
	}*/
}
