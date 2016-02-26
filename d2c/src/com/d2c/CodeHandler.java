package com.d2c;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/code/{user}/{path}")
public class CodeHandler {
	
	@POST
	@Path("/c")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response compileCCode(	@PathParam("user") String user, 
									@PathParam("path") String path,
									Map<String, String> code
	){
		try{
			String args = "-o";
			for(String name : code.keySet()){
				TerminalCaller.saveFile(user, path, name+".c", code.get(name));
				TerminalCaller.gcc(user, path, "-c "+name+".c");
				args += " "+name+".o";
			}
			IOPipe pipe = TerminalCaller.gcc(user, path, args);
			//TODO Figure out how THE FUCK WE ARE GOING TO HANDLE CONTINUOUS IO
			// STREAMS WHAT THE HELL OMG PANIC
			//BufferedWriter bw = pipe.getInput();
			BufferedReader br = pipe.getOutput();
			String output = "";
			String line = "";
			while((line = br.readLine()) != null){
				output += line + "\n";
			}
			return Response.ok().entity(output).build();
		} catch(IOException e) {
			return Response.serverError().entity("Error handling files").build();
		}
	}
}
