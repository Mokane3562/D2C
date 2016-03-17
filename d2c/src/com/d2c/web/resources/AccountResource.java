package com.d2c.web.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.d2c.util.SQLHandler;
import com.d2c.web.beans.TransferableAccount;

@Path("/account")
public class AccountResource {

	@GET
	@Path("/{account_user_name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAccountInfo(@PathParam("account_u				sql.close();ser_name") String accountUserName, @HeaderParam("Authorization") String encodedLogin) {
		//decode login info
		String decodedUser = decodeUser(encodedLogin);
		String decodedPassword = decodePassword(encodedLogin);
		
		//continue only if the user has authority to view this info
		if (decodedUser.equals(accountUserName)) {
			//start SQL shit	
			try (SQLHandler sql = new SQLHandler();) {
				//TODO:Set up encrypted passwords
				ResultSet results = sql.getAccountInfo(accountUserName, decodedPassword);
				
				//check if the user exists on the database
				boolean userExists = results.first();
				if (userExists) {//if the account exists you're good to go
					//create the account object
					TransferableAccount account = new TransferableAccount();
					account.userName = results.getString(1);
					account.firstName = results.getString(2);
					account.lastName = results.getString(3);
					account.createTime = results.getTimestamp(4);
					
					//send created account object to client
					return Response.ok().entity(account).build();
				} else {//otherwise you're done because the page doesn't exist
					return Response.noContent().build();
				}
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
				
				//when shit goes FUBAR
				return Response.serverError().build();
			} 
		} else {
			return Response.status(403).build(); //this happens when access to this page is denied
		}
	}

	@POST
	@Path("/{account_user_name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createOrUpdateAccount(@PathParam("account_user_name") String accountUserName, TransferableAccount account) {
		// TODO make this post the course info to the DB

		try {
			return Response.created(new URI("/" + accountUserName)).build();
		} catch (URISyntaxException e) {
			return Response.serverError().build();
		}
	}
	
	@GET
	public Response validateLogIn(@HeaderParam("Authorization") String encodedLogin){
		//decode login info
		String user = decodeUser(encodedLogin);
		String password = decodePassword(encodedLogin);
		
		//start sql
		try (SQLHandler sql = new SQLHandler();) {
			ResultSet results = sql.getAccountInfo(user, password);
			
			//verify login info is legit (non null result set)
			boolean isValid = results.first(); //true if valid
			if(isValid){
				return Response.ok().build();
			} else {
				return Response.status(403).build();
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	private static String decodeUser(String encodedLogin) {
		String[] decoded = new String(Base64.getDecoder().decode(encodedLogin.split(" ")[1])).split(":");
		String user = new String(decoded[0]);
		return user;
	}
	
	private static String decodePassword(String encodedLogin) {
		String[] decoded = new String(Base64.getDecoder().decode(encodedLogin.split(" ")[1])).split(":");
		String pass = new String(decoded[1]);
		return pass;
	}
}
