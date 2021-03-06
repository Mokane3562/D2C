package com.d2c.web.beans;

import java.sql.Timestamp;
import java.util.List;

public class TransferableAccount {
	public String userName;
	public String password;
	public String firstName;
	public String lastName;
	public Timestamp createTime;
	public int refID;
	
	public enum Role {
		STUDENT ("STUDENT"),
		PROFESSOR ("PROFESSOR"),
		TA ("TA");
		
		private final String roleName;
		
		private Role(String roleName) {
	        this.roleName = roleName;
	    }
		
		public String toString() {
		       return this.roleName;
		}
	}
}
