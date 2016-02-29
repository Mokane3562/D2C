package com.d2c.web.resources;

import java.io.BufferedReader;
//import java.io.BufferedWriter; //Uncomment later when we figure out the stream problem
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.d2c.util.IOPipe;
import com.d2c.util.TerminalCaller;

@Path("/code/{user}/{path}")
public class CodeResource {
	
	@POST
	@Path("/c")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response compileCCode(	@PathParam("user") String user, 
									@PathParam("path") String path,
									Map<String, String> code
	){
		String proper_path = path.replace('_', '/');
		try{
			System.out.println(code.get("text"));
			ArrayList<String> args = new ArrayList<>();
			IOPipe pipe;
			String output = "";
			String line = "";
			for(String name : code.keySet()){
				TerminalCaller.saveFile(user, proper_path, name+".c", code.get(name));
				pipe = TerminalCaller.gcc(user, proper_path, stringToList("-c "+name+".c"));
				args.add(name+".o");
				//BufferedWriter bw = pipe.getInput();
				BufferedReader br = pipe.getOutput();
				while((line = br.readLine()) != null){
					output += line + "\n";
				}
				line = "";
			}
			args.add("-o");
			args.add("a.out");
			pipe = TerminalCaller.gcc(user, proper_path, args);
			//TODO Figure out how THE FUCK WE ARE GOING TO HANDLE CONTINUOUS IO
			// STREAMS WHAT THE HELL OMG PANIC
			//BufferedWriter bw = pipe.getInput();
			BufferedReader br = pipe.getOutput();
			while((line = br.readLine()) != null){
				output += line + "\n";
			}
			System.out.println("returning ok response");
			//TerminalCaller.clearTempUserFiles(user, proper_path);
			return Response.ok().entity(output).build();
		} catch(IOException e) {
			//TerminalCaller.clearTempUserFiles(user, proper_path);
			System.out.println("returning server error response from IOException");
			System.out.println(e.getMessage());
			return Response.serverError().entity("Error handling files").build();
		} catch (InterruptedException e) {
			//TerminalCaller.clearTempUserFiles(user, proper_path);
			System.out.println("returning server error response from InterruptedException");
			System.out.println(e.getMessage());
			return Response.serverError().entity("Error waiting for process").build();
		}
	}
	
	private List<String> stringToList(String args){
		String[] asArray = args.split(" ");
		ArrayList<String> asArrayList = new ArrayList<>();
		for(String a : asArray){
			asArrayList.add(a);
		}
		return asArrayList;
	}
}
