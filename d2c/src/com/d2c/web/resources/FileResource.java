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

	/*@POST
	@Path("/{course_id}/{assignment}/{file_name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response uploadOrUpdateFile(@PathParam("course_id") String courseID, @PathParam("assignment") String assignment, @PathParam("file_name") String fileName, TransferableFile file) {
		// TODO make this post the course info to the DB

		try {
			return Response.created(new URI("/" + courseID + "/" + assignment + "/" + fileName)).build();
		} catch (URISyntaxException e) {
			return Response.serverError().build();
		}
	}*/

}