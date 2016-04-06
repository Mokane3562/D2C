package com.d2c.web.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.d2c.util.EmptySetException;
import com.d2c.util.SQLHandler;
import com.d2c.web.beans.TransferableAccount;
import com.d2c.web.beans.TransferableAccount.Role;
import com.d2c.web.beans.TransferableCourse;
import com.d2c.web.beans.TransferableCourseInstance;
//import com.d2c.web.beans.TransferableAccount.Role;
import com.d2c.web.beans.TransferableCourseInstance.Semester;

@Path("/course_inst")
public class CourseInstanceResource {
	//returns comprehensive course information given a 5-digit crn
	@GET
	@Path("/{crn}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCourseInst(@PathParam("crn") String crn) {
		System.out.println("yes buyyyyyyyyyy");
		try (SQLHandler sql = new SQLHandler();) {
			Object[] results = sql.selectCourseInst(crn);
			//create the course instance
			TransferableCourseInstance courseInstance = new TransferableCourseInstance();
			courseInstance.semester = (Semester) Semester.valueOf((String) results[0]);
			courseInstance.year = (String) results[1];
			courseInstance.profName = (String) results[2];
			courseInstance.courseReferenceNumber = (String) results[3];
			courseInstance.courseID = (int) results[4];
			courseInstance.refID = (int) results[5];
			//send created course instance object to client
			return Response.ok().entity(courseInstance).build();
		} catch (EmptySetException e) {
			e.printStackTrace();
			return Response.noContent().build();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			//when shit goes FUBAR
			return Response.serverError().build();
		}
	}
	
	//returns (assignment number, assignment refID) pairs
	@GET
	@Path("/{crn}/assignments")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCourseInstAssignments(@PathParam("crn") String crn) {
		try (SQLHandler sql = new SQLHandler();) {
			List<Object[]> results = sql.selectAssignments(crn);
			HashMap<Integer, Integer> assignments = new HashMap<>();
			for (Object[] row: results) {
				int assignmentNumber = (int) row[0];
				int assignmentID = (int) row[1];
				assignments.put(assignmentNumber, assignmentID);
			}
			return Response.ok().entity(assignments).build();
		} catch (EmptySetException e) {
			e.printStackTrace();
			return Response.noContent().build();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			//when shit goes FUBAR
			return Response.serverError().build();
		}
	}
	
	//returns comprehensive course information given a refID
	@GET
	@Path("/refid/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCourseInstByRefID(@PathParam("id") int refID) {
		try (SQLHandler sql = new SQLHandler();) {
			Object[] results = sql.selectCourseInstByRefID(refID);
			//create the course instance
			TransferableCourseInstance courseInstance = new TransferableCourseInstance();
			courseInstance.semester = (Semester) Semester.valueOf((String) results[0]);
			courseInstance.year = (String) results[1];
			courseInstance.profName = (String) results[2];
			courseInstance.courseReferenceNumber = (String) results[3];
			courseInstance.courseID = (int) results[4];
			courseInstance.refID = (int) results[5];
			//send created course instance object to client
			return Response.ok().entity(courseInstance).build();
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
	
	//returns a list of (account refID, role) pairs from a crn
	@GET
	@Path("/{crn}/participants")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getParticipants(@PathParam("crn") String crn/*, @HeaderParam("Authorization") String encodedLogin*/) {
		//decode login info
		//String decodedUser = AccountResource.decodeUser(encodedLogin);
		//String decodedPassword = AccountResource.decodePassword(encodedLogin);
		//start SQL shit	
		try (SQLHandler sql = new SQLHandler();) {
		//TODO:Set up authorization in a meaningful way
			List<Object[]> results = sql.selectParticipants(crn);
			//continue only if the user has authority to view this info
			//Object[] account = sql.getAccountInfo(accountUserName);
			if (true/*decodedUser.equals(accountUserName) && decodedPassword.equals(account[1])*/) {
				//create the map
				HashMap<Integer, Role> participants = new HashMap<>();
				for (Object[] row: results) {
					int accountID = (int) row[0];
					Role accountRole = (Role) Role.valueOf((String) row[1]);
					participants.put(accountID, accountRole);
				}

				return Response.ok().entity(participants).build();
			} else {//otherwise you're done because the page doesn't exist
				return Response.status(403).build(); //this happens when access to this page is denied
			} 
		} catch (EmptySetException e) {
			e.printStackTrace();
			return Response.noContent().build();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			//when shit goes FUBAR
			return Response.serverError().build();
		} 
	}	

	//POSTS
	//create a new account
	@POST
	@Path("/{crn}/register/{user}/as/{role}") //CHECKME:What if there's a / in the username or password?
	@Consumes(MediaType.APPLICATION_JSON)
	//CHECKME:Can we make POSTs safer? Is this overkill?
	public Response createParticipant(@PathParam("crn") String crn, @PathParam("user") String user, @PathParam("role") String role) {
		//start sql shit
		SQLHandler sql = null;
		try {
			sql = new SQLHandler();
			sql.setAutoCommit(false); //enable transactions
			
			Object[] accountInfo =  sql.selectAccountInfo(user);
			int accountID = (int) accountInfo[5];
			
			Object[] courseInstInfo =  sql.selectCourseInst(crn);
			int courseInstID = (int) courseInstInfo[5];
			
			sql.insertParticipant(courseInstID, accountID, Role.valueOf(role));
			sql.commit();
			return Response.created(new URI("/registered/" + user)).build();
		} catch (SQLException | ClassNotFoundException | URISyntaxException | EmptySetException e) {
			try {
				e.printStackTrace();
				sql.rollback();
			} catch (SQLException e1) {
				System.err.println("\nROLLBACK FAILED\n");
				e1.printStackTrace();
				return Response.serverError().build();
			}
			e.printStackTrace();
			return Response.serverError().build();
		} finally {
			try {
				sql.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*@POST
	@Path("/{crn}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(@PathParam("crn") String crn, TransferableCourseInstance course) {
		// TODO make this post the course info to the DB

		try {
			return Response.created(new URI("/" + crn)).build();
		} catch (URISyntaxException e) {
			return Response.serverError().build();
		}
	}*/
}
