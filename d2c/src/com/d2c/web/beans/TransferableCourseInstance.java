package com.d2c.web.beans;

//import com.d2c.util.Participatable;

public class TransferableCourseInstance /*implements Participatable*/{
	public TransferableCourse course;
	public Semester semester; //WINTER
	public String year; //2016
	public String profName;
	public String courseReferenceNumber; //00001
	public TransferableClassList classList;
	
	/*public TransferableCourseInstance(){
		classList = new TransferableClassList(this);
	}*/
	
	public enum Semester {
		FALL,
		WINTER,
		SPRING
	}
}

