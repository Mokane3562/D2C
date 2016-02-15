package com.d2c;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/main")
public class MainPageResource {
	
	@GET
	@Path("/index.html")
	@Produces(MediaType.TEXT_HTML)
	public Response getMainPage(){
		File file = new File("index.html");
		String html = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			Stream<String> contents = br.lines();
			html = contents.collect(Collectors.joining(""));
			br.close();
			return Response.ok()
					.entity(html)
					.build();
		} catch (FileNotFoundException e) {
			return Response.serverError()
					.build();
		} catch (IOException e) {
			// This may be wrong, not sure yet.
			return Response.ok()
					.entity(html)
					.build();
		}
	}
}
