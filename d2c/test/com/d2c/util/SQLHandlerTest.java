package com.d2c.util;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class SQLHandlerTest {

	@Test
	public void testGetAccountInfo() throws Exception{
		try (SQLHandler sql = new SQLHandler();) {
			Object[] results = sql.getAccountInfo("mlc258");
			assertTrue("String mismatch", results[0].equals("mlc258"));
			assertTrue("String mismatch", results[1].equals("please"));
			assertTrue("String mismatch", results[2].equals("Michelle"));
			assertTrue("String mismatch", results[3].equals("Croke"));
			assertNotNull("Null timestamp", results[4]);
			assertTrue("ID mismatch", (int) results[5] == 4);
		}
	}
	
	@Test
	public void testGetAccountRoles() throws Exception{
		try (SQLHandler sql = new SQLHandler();) {
			List<Object[]> results = sql.getAccountRoles("mnjs51");
			
			Object[] row1 = results.get(0); 
			assertTrue("String mismatch", row1[0].equals("00001"));
			assertTrue("String mismatch", row1[1].equals("PROFESSOR"));
			
			Object[] row2 = results.get(1); 
			assertTrue("String mismatch", row2[0].equals("00002"));
			assertTrue("String mismatch", row2[1].equals("PROFESSOR"));
			
			Object[] row3 = results.get(2); 
			assertTrue("String mismatch", row3[0].equals("00004"));
			assertTrue("String mismatch", row3[1].equals("STUDENT"));
		}
	}

	@Test
	public void testGetCourseInst() throws Exception {
		try (SQLHandler sql = new SQLHandler();) {
			Object[] results = sql.getCourseInst("00001");
			assertTrue("String mismatch", results[0].equals("COMP"));
			assertTrue("String mismatch", results[1].equals("1710"));
			assertTrue("String mismatch", results[2].equals("Object-Oriented Programming I"));
			assertTrue("String mismatch", results[3].equals("WINTER"));
			assertTrue("String mismatch", results[4].equals("2016"));
			assertTrue("String mismatch", results[5].equals("Mike and Scott"));
			assertTrue("String mismatch", results[6].equals("00001"));
		}
	}
	
	@Test
	public void testGetParticipants() throws Exception{
		try (SQLHandler sql = new SQLHandler();) {
			List<Object[]> results = sql.getParticipants("00001");
			
			Object[] row1 = results.get(0); 
			assertTrue("String mismatch", row1[0].equals("mlc258"));
			assertTrue("String mismatch", row1[1].equals("STUDENT"));
			
			Object[] row2 = results.get(1); 
			assertTrue("String mismatch", row2[0].equals("mnjs51"));
			assertTrue("String mismatch", row2[1].equals("PROFESSOR"));
			
			Object[] row3 = results.get(2); 
			assertTrue("String mismatch", row3[0].equals("rmp255"));
			assertTrue("String mismatch", row3[1].equals("TA"));
			
			Object[] row4 = results.get(3); 
			assertTrue("String mismatch", row4[0].equals("sy6746"));
			assertTrue("String mismatch", row4[1].equals("PROFESSOR"));
			
			Object[] row5 = results.get(4); 
			assertTrue("String mismatch", row5[0].equals("tmb063"));
			assertTrue("String mismatch", row5[1].equals("STUDENT"));
		}
	}

	@Test
	public void testMakeAccount() throws Exception{
		SQLHandler sql = null;
		try {
			sql = new SQLHandler();
			sql.setAutoCommit(false); //enable transactions
			sql.makeAccount("bob", "bob", "bob", "bob");
			Object[] results = sql.getAccountInfo("bob");
			assertTrue("String mismatch", results[0].equals("bob"));
			assertTrue("String mismatch", results[1].equals("bob"));
			assertTrue("String mismatch", results[2].equals("bob"));
			assertTrue("String mismatch", results[3].equals("bob"));
		} finally {
			sql.rollback();//roll back transaction
			sql.close();
		}
	}

	@Test
	public void testUpdateAccount() throws Exception{
		SQLHandler sql = null;
		try {
			sql = new SQLHandler();
			sql.setAutoCommit(false); //enable transactions
			sql.updateAccount("mnjs51", "itsadog", "Carl", "Sagan");
			Object[] results = sql.getAccountInfo("mnjs51");
			assertTrue("String mismatch", results[0].equals("mnjs51"));
			assertTrue("String mismatch", results[1].equals("itsadog"));
			assertTrue("String mismatch", results[2].equals("Carl"));
			assertTrue("String mismatch", results[3].equals("Sagan"));
		} finally {
			sql.rollback();//roll back transaction
			sql.close();
		}
	}
}
