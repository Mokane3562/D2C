package com.d2c.web.beans;

import java.sql.Timestamp;

public class TransferableSubmission {
	public Timestamp time_submitted;
	public Double grade;
	public int authorAccountID;
	public int assignmentID;
	public int[] fileIDs;
	public int refID;
}