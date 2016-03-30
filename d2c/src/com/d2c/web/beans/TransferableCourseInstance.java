package com.d2c.web.beans;

public class TransferableCourseInstance {
	public TransferableCourse course;
	public Semester semester; //WINTER
	public String year; //2016
	public String profName;
	public String courseReferenceNumber; //00001
	public TransferableClassList classList;
	
	public enum Semester {
		FALL,
		WINTER,
		SPRING
	}
}

