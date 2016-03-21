package com.d2c.web.beans;

import java.sql.Timestamp;

public class TransferableAccount {
	//CHECKME: The reason password is not a field is for security. When creating a new user, is it more or less secure to send a password down through a transferable or through the url?
	public String userName;
	public String password;
	public String firstName;
	public String lastName;
	public Timestamp createTime; 
}
