package com.d2c.web.beans;

import java.util.List;

public class TransferableCourse {
	public String subject; //COMP
	public String number; //1710
	public String name; //Object-Oriented Programming
	public int courseReferenceNumber; //00001
	public List<TransferableStudent> students;
	public List<TransferableTA> teachingAssistants;
	public List<TransferableProf> profs;
	public List<TransferableAssignment> assignments;
}
