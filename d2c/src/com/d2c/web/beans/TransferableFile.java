package com.d2c.web.beans;

import java.sql.Timestamp;

public class TransferableFile {
	public String fileName;
	public String fileType;
	public char[] content;//utf-8
	public Timestamp dateAdded;
	public int authorAccountID;
	public int refID;
}