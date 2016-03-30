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
import com.d2c.web.beans.TransferableClassList;
import com.d2c.web.beans.TransferableClassList.Role;
import com.d2c.web.beans.TransferableCourse;
import com.d2c.web.beans.TransferableCourseInstance;
import com.d2c.web.beans.TransferableCourseInstance.Semester;

@Path("/course")
public class CourseInstanceResource {
	
	//returns comprehensive course information given a 5-digit crn
	@GET
	@Path("/{crn}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCourseInfo(@PathParam("crn") String crn) {
		//WARNING: STARTING SOME HEAVY SQL SHIT. ALSO MAGIC.
		try (SQLHandler sql = new SQLHandler();) {
			Object[] results = sql.getCourseInst(crn);
			//set up some stuff to be added to the course instance object
			//make an abstract course
			TransferableCourse course = new TransferableCourse();
			course.subject = (String) results[0];
			course.number = (String) results[1];
			course.name = (String) results[2];
			//make a classlist with a bunch of accounts and roles
			TransferableClassList classList = new TransferableClassList();//prepare the classlist
			HashMap<TransferableAccount, Role> participantsForClassList = new HashMap<>();//will add this to classlist when it's ready
			List<Object[]> participants = sql.getParticipants(crn);//gets a list of username,role pairs based on a crn
			for (Object[] row: participants) {//for every pair in that list of participants
				Object[] accountsForParticipants = sql.getAccountInfo((String) row[0]);//use the username to get the account info
				TransferableAccount account = new TransferableAccount();//prepare the account to receive
				account.userName = (String) accountsForParticipants[0];
				account.password = (String) accountsForParticipants[1];
				account.firstName = (String) accountsForParticipants[2];
				account.lastName = (String) accountsForParticipants[3];
				account.createTime = (java.sql.Timestamp) accountsForParticipants[4];
				participantsForClassList.put(account, (Role) row[1]);//put the account,role pair in the map destined for the classlist
			}
			classList.participants = participantsForClassList;
			
			//create the course instance
			TransferableCourseInstance courseInstance = new TransferableCourseInstance();
			courseInstance.course = course;
			courseInstance.semester = (Semester) results[3];
			courseInstance.year = (String) results[4];
			courseInstance.profName = (String) results[5];
			courseInstance.courseReferenceNumber = (String) results[6];
			courseInstance.classList = classList;
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
