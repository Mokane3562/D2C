package com.d2c.web.resources;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;

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
import com.d2c.web.beans.TransferableFile;

@Path("/file")
public class FileResource {

	/*@GET
	@Path("/assignment/{assignment_ref_id}/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllFilesInAssignment(@PathParam("assignment_ref_id") int assignRefID) {
		// Some sql shit to get my object

		// check that object exists
		boolean check = true;
		// if it exists then save it to a java object and return through
		// response
		if (check) {
			TransferableFile f = new TransferableFile();
			return Response.ok().entity(f).build();
		} else { // else return a not found
			return Response.noContent().build();
		}
	}*/
	
	/*@GET
	@Path("/{course_id}/{assignment}/{file_name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFileInfo(@PathParam("course_id") String courseID, @PathParam("assignment") String assignment, @PathParam("file_name") String fileName) {
		// Some sql shit to get my object

		// check that object exists
		boolean check = true;
		// if it exists then save it to a java object and return through
		// response
		if (check) {
			TransferableFile f = new TransferableFile();
			return Response.ok().entity(f).build();
		} else { // else return a not found
			return Response.noContent().build();
		}
	}*/
	
	@GET
	@Path("/refid/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFileByRefID(@PathParam("id") int refID) {
		try (SQLHandler sql = new SQLHandler();) {
			Object[] results = sql.selectFileByRefID(refID);
			//create the file
			TransferableFile file = new TransferableFile();
			file.fileName = (String) results[0];
			file.fileType = (String) results[1];
			file.content = (char[]) results[2];
			file.dateAdded = (Timestamp) results[3];
			file.authorAccountID = (int) results[4];
			file.refID = (int) results[5];
			//send created course object to client
			return Response.ok().entity(file).build();
		} catch (EmptySetException e) {
			e.printStackTrace();
			return Response.noContent().build();
		} catch (SQLException | ClassNotFoundException | IOException e) {
			e.printStackTrace();
			//when shit goes FUBAR
			return Response.serverError().build();
		}
	}

	@POST
	@Path("/new")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createNewFile(TransferableFile file) {
		//start sql shit
		SQLHandler sql = null;
		try {
			sql = new SQLHandler();
			sql.setAutoCommit(false); //enable transactions
			sql.insertFile(file);
			sql.commit();
			return Response.created(new URI("/" + file.fileName)).build();
		} catch (SQLException | ClassNotFoundException | URISyntaxException e) {
			try {
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

}