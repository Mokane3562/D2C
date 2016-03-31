package com.d2c.web.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Base64;
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

@Path("/account")
public class AccountResource {

	//GETS
	@GET
	@Path("/{account_user_name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAccountInfo(@PathParam("account_user_name") String accountUserName, @HeaderParam("Authorization") String encodedLogin) {
		//decode login info
		String decodedUser = decodeUser(encodedLogin);
		String decodedPassword = decodePassword(encodedLogin);
		//start SQL shit	
		try (SQLHandler sql = new SQLHandler();) {
		//TODO:Set up encrypted passwords
			Object[] results = sql.getAccountInfo(accountUserName);
			//continue only if the user has authority to view this info
			if (decodedUser.equals(accountUserName) && decodedPassword.equals(results[1])) {
				//create the account object
				TransferableAccount account = new TransferableAccount();
				account.userName = accountUserName;
				account.password = (String) results[1];
				account.firstName = (String) results[2];
				account.lastName = (String) results[3];
				account.createTime = (java.sql.Timestamp) results[4];
				account.refID = (int) results[5];
				//send created account object to client
				return Response.ok().entity(account).build();
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
	
	//returns a list of (refID, role type) pairs that an account is associated with.
	//IE. all the courses and respective roles an account is in.
	@GET
	@Path("/{account_user_name}/roles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRoles(@PathParam("account_user_name") String accountUserName, @HeaderParam("Authorization") String encodedLogin) {
		//decode login info
		String decodedUser = decodeUser(encodedLogin);
		String decodedPassword = decodePassword(encodedLogin);
		//start SQL shit	
		try (SQLHandler sql = new SQLHandler();) {
		//TODO:Set up encrypted passwords
			List<Object[]> results = sql.getAccountRoles(accountUserName);
			//continue only if the user has authority to view this info
			Object[] account = sql.getAccountInfo(accountUserName);
			if (decodedUser.equals(accountUserName) && decodedPassword.equals(account[1])) {
				//create the map
				HashMap<Integer, Role> roles = new HashMap<>();
				for (Object[] row: results) {
					roles.put((int) row[0], (Role) row[1]);
				}

				return Response.ok().entity(roles).build();
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
	
	@GET
	public Response validateLogIn(@HeaderParam("Authorization") String encodedLogin){
		//decode login info
		String decodedUser = decodeUser(encodedLogin);
		String decodedPassword = decodePassword(encodedLogin);
		//start sql
		try (SQLHandler sql = new SQLHandler();) {
			Object[] results = sql.getAccountInfo(decodedUser);	
			//verify login info is legit (non null result set)
			if(decodedPassword.equals(results[1])){
				return Response.ok().build();
			} else {
				return Response.status(403).build(); //this happens when access to this page is denied
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return Response.serverError().build();
		} catch (EmptySetException e) {
			e.printStackTrace();
			return Response.noContent().build();
		}
	}

	//POSTS
	@POST
	@Path("/new") //CHECKME:What if there's a / in the username or password?
	@Consumes(MediaType.APPLICATION_JSON)
	//CHECKME:Can we make POSTs safer? Is this overkill?
	public Response createAccount(TransferableAccount account) {
		//start sql shit
		SQLHandler sql = null;
		try {
			sql = new SQLHandler();
			sql.setAutoCommit(false); //enable transactions
			sql.makeAccount(account.userName, account.password, account.firstName, account.lastName);
			sql.commit();
			return Response.created(new URI("/" + account.userName)).build();
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
	
	@POST
	@Path("/{account_user_name}") 
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateAccount(@PathParam("account_user_name") String accountUserName, TransferableAccount account, @HeaderParam("Authorization") String encodedLogin) {
		//decode login info
		String decodedUser = decodeUser(encodedLogin);
		String decodedPassword = decodePassword(encodedLogin);
		//start sql shit
		SQLHandler sql = null;
		try {
			sql = new SQLHandler();
			Object[] results = sql.getAccountInfo(accountUserName);
			if (decodedUser.equals(accountUserName) && decodedPassword.equals(results[1])) {
				sql.setAutoCommit(false); //enable transactions
				sql.updateAccount(accountUserName, account.password, account.firstName, account.lastName);
				sql.commit();
				return Response.created(new URI("/" + accountUserName)).build();//CHECKME: Is this the correct response?
			} else {
				return Response.status(403).build();
			}
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
		} catch (EmptySetException e) {
			e.printStackTrace();
			return Response.noContent().build();
		} finally {
			try {
				sql.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//HELPERS
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
