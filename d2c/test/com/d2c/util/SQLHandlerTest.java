package com.d2c.util;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.d2c.web.beans.TransferableAccount.Role;

public class SQLHandlerTest {

	@Test
	public void testGetAccountInfo() throws Exception{
		try (SQLHandler sql = new SQLHandler();) {
			Object[] results = sql.selectAccountInfo("mlc258");
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
			List<Object[]> results = sql.selectAccountRoles("mnjs51");
			
			Object[] row1 = results.get(0); 
			assertTrue("id mismatch", row1[0].equals(1));
			assertTrue("String mismatch", row1[1].equals("PROFESSOR"));
			
			Object[] row2 = results.get(1); 
			assertTrue("id mismatch", row2[0].equals(2));
			assertTrue("String mismatch", row2[1].equals("PROFESSOR"));
			
			Object[] row3 = results.get(2); 
			assertTrue("id mismatch", row3[0].equals(4));
			assertTrue("String mismatch", row3[1].equals("STUDENT"));
		}
	}

	//TODO:make this less shit
	@Test
	public void testGetAssignmentByRefID() throws Exception{
		try (SQLHandler sql = new SQLHandler();) {
			Object[] results = sql.selectAssignmentByRefID(2);
			assertTrue("Integer mismatch", results[0].equals(2));//int
			assertTrue("Timestamp mismatch", results[1].toString().equals("2016-02-07 00:00:00.0"));//ts
			assertTrue("ID mismatch", results[2].equals(2));//int
			//assertTrue("IDs mismatch", results[3].equals(?));//int[]
			int[] arr = (int[]) results[3];
			for (int id: arr) {
				System.out.println(id);
			}
			assertTrue("ID mismatch", results[4].equals(2));//int
		}
	}
	
	//TODO:make this less shit
	@Test
	public void testGetAssignmentFiles() throws Exception{
		try (SQLHandler sql = new SQLHandler();) {
			int[] results = sql.selectAssignmentFiles(2);
			for (int id: results) {
				System.out.println(id);
			}
		}
	}
	
	@Test
	public void testGetAssignments() throws Exception{
		try (SQLHandler sql = new SQLHandler();) {
			List<Object[]> results = sql.selectAssignments("00002");
			
			Object[] row1 = results.get(0); 
			assertTrue("Integer mismatch", row1[0].equals(1));
			assertTrue("ID mismatch", row1[1].equals(1));
			
			Object[] row2 = results.get(1); 
			assertTrue("Integer mismatch", row2[0].equals(2));
			assertTrue("ID mismatch", row2[1].equals(2));
			
			Object[] row3 = results.get(2); 
			assertTrue("Integer mismatch", row3[0].equals(3));
			assertTrue("ID mismatch", row3[1].equals(3));
		}
	}
	
	@Test
	public void testGetCourseByRefID() throws Exception {
		try (SQLHandler sql = new SQLHandler();) {
			Object[] results = sql.selectCourseByRefID(4);
			assertTrue("String mismatch", results[0].equals("ENGI"));//subject
			assertTrue("String mismatch", results[1].equals("1020"));//number
			assertTrue("String mismatch", results[2].equals("Intro to Programming"));//name
			assertTrue("ID mismatch", (int) results[3] == 4);//course_id
		}
	}
	
	@Test
	public void testGetCourseInst() throws Exception {
		try (SQLHandler sql = new SQLHandler();) {
			Object[] results = sql.selectCourseInst("00001");
			assertTrue("String mismatch", results[0].equals("WINTER"));//semester
			assertTrue("String mismatch", results[1].equals("2016"));//year_offered
			assertTrue("String mismatch", results[2].equals("Mike and Scott"));//profname
			assertTrue("String mismatch", results[3].equals("00001"));//crn
			assertTrue("ID mismatch", (int) results[4] == 1);//course_id
			assertTrue("ID mismatch", (int) results[5] == 1);//course_inst_id
		}
	}
	public void testGetCourseInstByRefID() throws Exception {
		try (SQLHandler sql = new SQLHandler();) {
			Object[] results = sql.selectCourseInstByRefID(1);
			assertTrue("String mismatch", results[0].equals("WINTER"));//semester
			assertTrue("String mismatch", results[1].equals("2016"));//year_offered
			assertTrue("String mismatch", results[2].equals("Mike and Scott"));//profname
			assertTrue("String mismatch", results[3].equals("00001"));//crn
			assertTrue("ID mismatch", (int) results[4] == 1);//course_id
			assertTrue("ID mismatch", (int) results[5] == 1);//course_inst_id
		}
	}
	
	//TODO:make this less shit
	@Test
	public void testGetFileByRefID() throws Exception {
		try (SQLHandler sql = new SQLHandler();) {
			Object[] results = sql.selectFileByRefID(4);
			assertTrue("String mismatch", results[0].equals("HelloWorld"));//name
			assertTrue("String mismatch", results[1].equals("java"));//type
			//assertTrue("String mismatch", results[2].equals(""));//contents
			System.out.println(results[2]);
			//assertTrue("Timestamp mismatch", results[3].equals(?));//date_added
			System.out.println(results[3]);
			assertTrue("ID mismatch", (int) results[4] == 2);//account_id
			assertTrue("ID mismatch", (int) results[5] == 4);//file_id
		}
	}

	@Test
	public void testGetParticipants() throws Exception{
		try (SQLHandler sql = new SQLHandler();) {
			List<Object[]> results = sql.selectParticipants("00001");
			
			Object[] row1 = results.get(0);
			assertTrue("ID mismatch", row1[0].equals(1));
			assertTrue("String mismatch", row1[1].equals("PROFESSOR"));
			
			Object[] row2 = results.get(1); 
			assertTrue("ID mismatch", row2[0].equals(2));
			assertTrue("String mismatch", row2[1].equals("PROFESSOR"));
			
			Object[] row3 = results.get(2); 
			assertTrue("ID mismatch", row3[0].equals(3));
			assertTrue("String mismatch", row3[1].equals("TA"));
			
			Object[] row4 = results.get(3); 
			assertTrue("ID mismatch", row4[0].equals(4));
			assertTrue("String mismatch", row4[1].equals("STUDENT"));
			
			Object[] row5 = results.get(4); 
			assertTrue("ID mismatch", row5[0].equals(5));
			assertTrue("String mismatch", row5[1].equals("STUDENT"));
		}
	}

	@Test
	public void testMakeAccount() throws Exception{
		SQLHandler sql = null;
		try {
			sql = new SQLHandler();
			sql.setAutoCommit(false); //enable transactions
			sql.insertAccount("bob", "bob", "bob", "bob");
			Object[] results = sql.selectAccountInfo("bob");
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
	public void testMakeParticipant() throws Exception{
		SQLHandler sql = null;
		try {
			sql = new SQLHandler();
			sql.setAutoCommit(false);//enable transactions
			boolean accountWasThere = false;
			boolean roleWasCorrect = false;
			
			sql.insertParticipant(3, 1, Role.TA);
			List<Object[]> results = sql.selectParticipants("00003");
			for (Object[] row: results) {
				accountWasThere = row[0].equals(1);
				roleWasCorrect = row[1].equals("TA");
				if (accountWasThere && roleWasCorrect){
					break;
				}
			}
			assertTrue("ID mismatch", accountWasThere);
			assertTrue("Role mismatch", roleWasCorrect);
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
			Object[] results = sql.selectAccountInfo("mnjs51");
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
