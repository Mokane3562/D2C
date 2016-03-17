package com.d2c.util;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

public class SQLHandlerTest {
	private SQLHandler sql;
	
	@Test
	public void testSQLHandler() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAccountInfo() {
		try {
			sql = new SQLHandler();
			ResultSet results = sql.getAccountInfo("mlc258", "please");
			results.next();
			assertTrue("String mismatch", results.getString(1).equals("Michelle"));
			assertTrue("String mismatch", results.getString(2).equals("Croke"));
			assertNotNull("Null timestamp", results.getString(3));
			assertTrue("ID mismatch", results.getInt(4) == 4);
			assertFalse("Found multiple rows, expected one", results.next());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			if(e.getMessage() != null) {
				System.err.println(e.getMessage());
			}
			fail("Exception thrown");
		} finally {
			try {
				sql.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testGetAssignment() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCoursetInfo() {
		try {
			sql = new SQLHandler();
			ResultSet results = sql.getCourseInfo(00001);
			results.next();
			assertTrue("String mismatch", results.getString(1).equals("COMP"));
			assertTrue("String mismatch", results.getString(2).equals("1710"));
			assertTrue("String mismatch", results.getString(3).equals("Object-Oriented Programming I"));
			assertFalse("Found multiple rows, expected one", results.next());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			if(e.getMessage() != null) {
				System.err.println(e.getMessage());
			}
			fail("Exception thrown");
		} finally {
			try {
				sql.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

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
	public void testPostAccount() {
		fail("Not yet implemented");
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
	public void testClose() {
		fail("Not yet implemented");
	}

}
