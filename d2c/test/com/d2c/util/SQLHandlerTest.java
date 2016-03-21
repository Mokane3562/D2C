package com.d2c.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class SQLHandlerTest {
	
	@Test
	public void testSQLHandler() {
		fail("Not yet implemented");
	}

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
	public void testGetAssignment() {
		fail("Not yet implemented");
	}

//	@Test
//	public void testGetCourseInfo() {
//		try {
//			sql = new SQLHandler();
//			ResultSet results = sql.getCourseInfo(00001);
//			results.next();
//			assertTrue("String mismatch", results.getString(1).equals("COMP"));
//			assertTrue("String mismatch", results.getString(2).equals("1710"));
//			assertTrue("String mismatch", results.getString(3).equals("Object-Oriented Programming I"));
//			assertFalse("Found multiple rows, expected one", results.next());
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//			if(e.getMessage() != null) {
//				System.err.println(e.getMessage());
//			}
//			fail("Exception thrown");
//		} finally {
//			try {
//				sql.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	@Test
	public void testGetFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFiles() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetGrade() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSubmission() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSubmissionFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTA() {
		fail("Not yet implemented");
	}

	@Test
	public void testPostAccount() throws Exception{
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
	public void testPostAssignment() {
		fail("Not yet implemented");
	}

	@Test
	public void testPostCourse() {
		fail("Not yet implemented");
	}

	@Test
	public void testPostFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testPostGrade() {
		fail("Not yet implemented");
	}

	@Test
	public void testPostRole() {
		fail("Not yet implemented");
	}

	@Test
	public void testPostSubmission() {
		fail("Not yet implemented");
	}

	@Test
	public void testPostSubmissionFile() {
		fail("Not yet implemented");
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

	@Test
	public void testClose() {
		fail("Not yet implemented");
	}

}
