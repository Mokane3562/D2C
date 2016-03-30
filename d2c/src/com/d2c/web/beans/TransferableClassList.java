package com.d2c.web.beans;

import java.util.Map;

public class TransferableClassList {
	public Map<TransferableAccount, Role> participants;
	
	public enum Role {
		STUDENT,
		PROFESSOR,
		TA
	}
}
