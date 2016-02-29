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

import com.d2c.web.beans.TransferableProf;

@Path("/professor")
public class ProfessorResource {
	
	@GET
	@Path("/{prof_user_name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProfessorInfo(@PathParam("prof_user_name") String profUserName){
		//TODO Some sql shit to get my object
		
		//TODO check that object exists
		boolean check = true;
		//if it exists then save it to a java object and return through response
		if(check){
			TransferableProf p = new TransferableProf();
			return Response.ok()
					.entity(p)
					.build();
		} else { // else return a not found
			return Response.noContent().build();
		}
	}

	@POST
	@Path("/{prof_user_name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response makeProfessor(@PathParam("prof_user_name") String profUserName, 
			TransferableProf prof){
		//TODO make this post the course info to the DB
		
		try {
			return Response.created(new URI("/"+profUserName))
					.build();
		} catch (URISyntaxException e) {
			return Response.serverError().build();
		}
	}
}