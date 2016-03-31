package com.d2c.web.beans;

//import com.d2c.util.Participatable;

public class TransferableCourseInstance {
	public Semester semester; //WINTER
	public String year; //2016
	public String profName;
	public String courseReferenceNumber; //00001
	public int courseID;
	public int refID;
	
	public enum Semester {
		FALL,
		WINTER,
		SPRING
	}
}

