package com.d2c.web.beans;

import java.util.List;
import java.util.Map;

public class TransferableCourseInstance {
	public TransferableCourse course;
	public Semester semester;
	public int year;
	public int courseReferenceNumber; //00001
	public Map<TransferableAccount, Role> participants;
	
	enum Semester {
		FALL,
		WINTER,
		SPRING
	}
	
	enum Role {
		STUDENT,
		PROFESSOR,
		TA
	}
}

