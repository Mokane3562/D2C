package com.d2c.web.beans;

import java.util.Map;

//import com.d2c.util.Participatable;

public class TransferableClassList {
	//public Participatable participatable;
	public Map<TransferableAccount, Role> participants;
	
	/*public TransferableClassList(TransferableCourseInstance courseInst) {
		this.participatable = courseInst;
	}*/
	
	public enum Role {
		STUDENT,
		PROFESSOR,
		TA
	}
}
