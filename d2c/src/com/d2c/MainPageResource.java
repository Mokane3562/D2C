package com.d2c;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class MainPageResource {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//This method gets the web page. COMMENTED! :D
	public Response getMainPage(@Context ServletContext context){
		String fileAsString = "";
		try {
			//Get a Buffered Reader of your file as below
			BufferedReader br = new BufferedReader(
									new InputStreamReader(
										context.getResourceAsStream("/WEB-INF/index.html")
									)
								);
			//Get a stream of the contents
			Stream<String> contents = br.lines();
			//Collect the contents into a string, without dividing them (empty string arg)
			fileAsString = contents.collect(Collectors.joining(""));
			//Close the Buffered Reader
			br.close();
			//Return the response with the file contents as the argument to entity()
			return Response.ok()
					.entity(fileAsString)
					.build();
		} catch (FileNotFoundException e) {
			//If the file did not load properly, throw this error!
			return Response.serverError()
					.build();
		} catch (IOException e) {
			// This may be wrong, not sure yet.
			// You get here if the Buffered Reader fails to close.
			return Response.serverError()
					.build();
		}
	}
	
	@GET
	@Produces(MediaType.WILDCARD)
	@Path("style/{css}.css")
	//This method gets the a CSS file. Uncommented, but more or less a copy of getMainPage
	public Response getCSSFile(	@Context ServletContext context,
								@PathParam("css") String cssFileName)
	{
		String fileAsString = "";
		try {
			BufferedReader br = new BufferedReader(
									new InputStreamReader(
										context.getResourceAsStream(
											"/WEB-INF/style/"+cssFileName+".css"
										)
									)
								);
			Stream<String> contents = br.lines();
			fileAsString = contents.collect(Collectors.joining(""));
			br.close();
			return Response.ok()
					.entity(fileAsString)
					.build();
		} catch (FileNotFoundException e) {
			return Response.serverError()
					.build();
		} catch (IOException e) {
			// This may be wrong, not sure yet.
			return Response.ok()
					.entity(fileAsString)
					.build();
		}
	}
}
