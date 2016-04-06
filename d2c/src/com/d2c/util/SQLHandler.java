package com.d2c.util;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.d2c.web.beans.TransferableAccount.Role;
import com.d2c.web.beans.TransferableFile;

/**
 * Contains tools and methods to interact with a database using JDBC
 */
public class SQLHandler implements AutoCloseable{
	// TODO replace placeholder host and port with real host and port;
	// TODO replace placeholder user and password with real user and password
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/d2c";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "password";
	
	private Connection connection;
	private Properties connectionProperties;
	
	/*private static final String EXAMPLE_SQL = 
	 * 			"SELECT ? " 
	 * 		+	"FROM SOME_TABLE "
	 * 		+	"WHERE SOME_COLUMN = ?";
	 */
	//Get a user's first name, last name, and account ID using their user-name and password. 
	private static final String SELECT_ACCOUNT_SQL = 
				"SELECT username, password, fname, lname, create_time, account_id "
			+ 	"FROM account "
			+ 	"WHERE username = ?";
	//Get an accounts roles in courses
	private static final String SELECT_ACCOUNT_ROLES_SQL = 
				"SELECT course_inst_id, type "
			+ 	"FROM account, role "
			+ 	"WHERE username = ? "
			+ 	"AND account.account_id = role.account_id";
	//Get an assignment's number, due date, and contents using it's assignment ID.
	private static final String SELECT_ASSIGNMENT_BY_ID_SQL = 	
				"SELECT number, due_date, course_inst_id, assign_id "
			+ 	"FROM assignment "
			+ 	"WHERE assign_id = ?";
	//Get an assignment's number, due date, and contents using it's assignment ID.
	private static final String SELECT_ASSIGNMENT_FILES_SQL = 	
				"SELECT file_id "
			+ 	"FROM assignment_file "
			+ 	"WHERE assign_id = ?";	
	//Get an assignment's number, due date, and contents using it's assignment ID.
	private static final String SELECT_ASSIGNMENTS_SQL = 	
				"SELECT number, assign_id "
			+ 	"FROM assignment, course_inst "
			+ 	"WHERE crn = ? "
			+ 	"AND assignment.course_inst_id = course_inst.course_inst_id";
	//Get a course's subject, number, and name, using it's crn.
	private static final String SELECT_COURSE_INST_SQL = 	
				"SELECT semester, year_offered, prof_name, crn, course_id, course_inst_id "
			+ 	"FROM course_inst "
			+ 	"WHERE crn = ? ";
	//Get a course's subject, number, and name, using it's crn.
	private static final String SELECT_COURSE_INST_BY_ID_SQL = 	
				"SELECT semester, year_offered, prof_name, crn, course_id, course_inst_id "
			+ 	"FROM course_inst "
			+ 	"WHERE course_inst_id = ? ";
	//Get a course directly from its id.
	private static final String SELECT_COURSE_BY_ID_SQL = 	
				"SELECT subject, number, name, course_id "
			+ 	"FROM course "
			+ 	"WHERE course_id = ? ";
	//Get a file's name, date of creation, and contents using it's file ID.
	private static final String SELECT_FILE_BY_ID_SQL = 	
				"SELECT name, type, contents, date_added, account_id, file_id "
			+ 	"FROM file "
			+ 	"WHERE file_id = ?";
	//Get the name, date of creation, and contents of every file a user created using that user's ID.
	private static final String GET_FILES_SQL = 	
				"SELECT file_id, name, date_added, contents "
			+ 	"FROM file "
			+ 	"WHERE account_id = ?";
	//Get a submission's grade using it's ID.
	private static final String GET_GRADE_SQL = 
				"SELECT grade "
			+ 	"FROM submission "
			+ 	"WHERE submission_id = ?";
	//Get the account ID and role of every user participating in a certain course.
	private static final String SELECT_PARTICIPANTS_SQL = 
				"SELECT account_id, type "
			+ 	"FROM role, course_inst "
			+ 	"WHERE crn = ? "
			+ 	"AND role.course_inst_id = course_inst.course_inst_id";
	//Get a submission's submitter, associated assignment, and grade using it's submission ID.
	private static final String GET_SUBMISSION_SQL = 
				"SELECT account_id, assign_id, grade "
			+ 	"FROM submission "
			+ 	"WHERE submission_id= ?";
	//Get the file ID's of files associated with a submission using that submission's ID.
	private static final String GET_SUBMISSION_FILES_SQL = 
				"SELECT file_id "
			+ 	"FROM submission_file "
			+ 	"WHERE submission_id = ?";
	//Store a new account in the database. Other fields will be generated automatically.
	private static final String INSERT_ACCOUNT_SQL = 
				"INSERT INTO account (username, password, fname, lname) "
			+ 	"VALUES (?, ?, ?, ?)";
	//Store a new assignment in the database. Other fields will be generated automatically.
	private static final String POST_ASSIGNMENT_SQL = 
				"INSERT INTO assignment (number, course_id, file_id ,due_date, assign) "
			+ 	"VALUES (?, ?, ?, ?, ?)";
	//Store a new course in the database. Other fields will be generated automatically.
	private static final String POST_COURSE_SQL = 
				"INSERT INTO course (number, subject, name) "
			+ 	"VALUES ( ?, ?, ? )";
	//Store a file in the database. Other fields will be generated automatically.
	private static final String INSERT_FILE_SQL = 
				"INSERT INTO file (name, type, contents, account_id, file_id) "
			+ 	"VALUES (?, ?, ?, ?, ?)";
	//Change/update a grade for a submission.
	private static final String POST_GRADE_SQL = 
				"UPDATE submission "
			+ 	"SET grade = ? "
			+ 	"WHERE submission_id = ?";
	//Add a participant to a course. Other fields will be generated automatically.
	private static final String INSERT_PARTICIPANT_SQL = 
				"INSERT INTO role (course_inst_id, account_id, type) "
			+ 	"VALUES (?, ?, ?)";
	//TODO:Make sure all the files attached to a submission are reachable
	private static final String POST_SUBMISSION_SQL = 
				"INSERT INTO submission (account_id, assign_id)"
			+ 	"VALUES (?, ?)";
	//Add a file to a submission.
	private static final String POST_SUBMISSION_FILE_SQL = 
				"INSERT INTO submission_file (submission_id,file_id) "
			+ 	"VALUES(?, ?)";
	//update an account
	private static final String UPDATE_ACCOUNT_SQL = 
				"UPDATE account "
			+ 	"SET password=?, fname=?, lname=?"
			+ 	"WHERE username = ?";
	// private PreparedStatement exampleStatement;
//	private PreparedStatement getAccountStatement;
//	private PreparedStatement getAssignmentStatement;
//	private PreparedStatement getCourseStatement;
//	private PreparedStatement getFileStatement;
//	private PreparedStatement getFilesStatement;
//	private PreparedStatement getGradeStatement;
//	private PreparedStatement getParticipantsStatement;
//	private PreparedStatement getSubmissionStatement;
//	private PreparedStatement getSubmissionFilesStatement;
//	private PreparedStatement postAccountStatement;
//	private PreparedStatement postAssignmentStatement;
//	private PreparedStatement postCourseStatement;
//	private PreparedStatement postFileStatement;
//	private PreparedStatement postGradeStatement;
//	private PreparedStatement postParticipantStatement;
//	private PreparedStatement postSubmissionStatement;
//	private PreparedStatement postSubmissionFileStatement;

	//CONSTRUCTOR(S)
	/**
	 * Creates a new SQLHandler connected to a MySQL database on localhost port 3306 with username "root" and password "password".
	 * 
	 * @throws SQLException if a database access error occurs
	 * @throws ClassNotFoundException if the driver class cannot be located
	 */
	public SQLHandler() throws SQLException, ClassNotFoundException {
		//Make a connection
		Class.forName(DB_DRIVER);
		this.connectionProperties = new Properties();
		this.connectionProperties.put("user", DB_USER);
		this.connectionProperties.put("password", DB_PASSWORD);
		System.out.println("attempting to get connection");
		this.connection = DriverManager.getConnection(DB_CONNECTION, connectionProperties);
		
		if (connection.isValid(30000)) {
			System.out.println("connected at " + DB_CONNECTION);
		}
	}
	
	//CLASS METHODS
	/**
	 * @see java.sql.Connection#commit()
	 */
	public void commit() throws SQLException{
		connection.commit();
	}
	
	/**
	 * @see java.sql.Connection#rollback()
	 */
	public void rollback() throws SQLException{
		connection.rollback();
	}
	
	/**
	 * @see java.sql.Connection#setAutoCommit(boolean)
	 */
	public void setAutoCommit(boolean autoCommit) throws SQLException{
		connection.setAutoCommit(autoCommit);
	}
	
	//GETTERS AND SETTERS
	/*
	 * public ResultSet getExample(String column, String value) throws SQLException { 
	 *     this.example_statement = connection.prepareStatement(EXAMPLE_SQL);
	 *     this.example_statement.setString(1, column);
	 *     this.example_statement.setString(2, value); 
	 *     return this.example_statement.executeQuery(); 
	 * }
	 */

	/*public ResultSet getSubmission(int submission_id) throws SQLException {
		this.getSubmissionStatement = connection.prepareStatement(GET_SUBMISSION_SQL);
		this.getSubmissionStatement.setString(1, Integer.toString(submission_id));
		return this.getSubmissionStatement.executeQuery();
	}*/
	
	/*public ResultSet getSubmissionFiles(int submission_id) throws SQLException {
		this.getSubmissionFilesStatement = connection.prepareStatement(GET_SUBMISSION_FILES_SQL);
		this.getSubmissionFilesStatement.setString(1, Integer.toString(submission_id));
	
		return this.getSubmissionFilesStatement.executeQuery();
	}*/
	
	/**
	 * Creates a new account on the database.
	 * 
	 * @param username the account user name
	 * @param password the account password
	 * @param fname the first name of the account holder
	 * @param lname the last name of the account holder
	 * @throws SQLException if a database error occurs
	 */
	//TODO:make this take in a transferable account
	public void insertAccount(String username, String password, String fname, String lname) throws SQLException {
		try (PreparedStatement insertAccountStatement = connection.prepareStatement(INSERT_ACCOUNT_SQL);) {
			insertAccountStatement.setString(1, username);
			insertAccountStatement.setString(2, password);
			insertAccountStatement.setString(3, fname);
			insertAccountStatement.setString(4, lname);
			insertAccountStatement.executeUpdate();
		}
	}

	/*public ResultSet getSubmission(int submission_id) throws SQLException {
		this.getSubmissionStatement = connection.prepareStatement(GET_SUBMISSION_SQL);
		this.getSubmissionStatement.setString(1, Integer.toString(submission_id));
		return this.getSubmissionStatement.executeQuery();
	}*/
	
	/*public ResultSet getSubmissionFiles(int submission_id) throws SQLException {
		this.getSubmissionFilesStatement = connection.prepareStatement(GET_SUBMISSION_FILES_SQL);
		this.getSubmissionFilesStatement.setString(1, Integer.toString(submission_id));
	
		return this.getSubmissionFilesStatement.executeQuery();
	}*/
	
	
	
	/*public ResultSet postAssignment(String num, int course_id, Date date, int test_id, String assign) throws SQLException {
		this.postAssignmentStatement = connection.prepareStatement(POST_ASSIGNMENT_SQL);
		this.postAssignmentStatement.setString(1, num);
		this.postAssignmentStatement.setString(2, Integer.toString(course_id));
		// this.post_assignment_statement.setString(3, date);
		this.postAssignmentStatement.setString(4, Integer.toString(test_id));
		this.postAssignmentStatement.setString(5, assign);
		return this.postAssignmentStatement.executeQuery();
	}*/
	
	// returns posting a course
	/*public ResultSet postCourse(String number, String subject, String name) throws SQLException {
		this.postCourseStatement = connection.prepareStatement(POST_COURSE_SQL);
		this.postCourseStatement.setString(1, number);
		this.postCourseStatement.setString(2, subject);
		this.postCourseStatement.setString(3, name);
		return this.postCourseStatement.executeQuery();
	}*/
	
	/*public ResultSet postFile(String name, Date date, String text) throws SQLException {
		this.postFileStatement = connection.prepareStatement(POST_FILE_SQL);
		this.postFileStatement.setString(1, name);
		// this.post_course_statement.setString(2, date);
		this.postFileStatement.setString(3, text);
		return this.postFileStatement.executeQuery();
	}*/
	
	/*public ResultSet postGrade(double grade) throws SQLException {
		this.postGradeStatement = connection.prepareStatement(POST_GRADE_SQL);
		this.postGradeStatement.setString(1, Double.toString(grade));
		return this.postGradeStatement.executeQuery();
	}*/
	
	public void insertParticipant(int courseInstID, int accountID, Role roleType) throws SQLException {
		try (PreparedStatement insertParticipantStatement = connection.prepareStatement(INSERT_PARTICIPANT_SQL);) {
			insertParticipantStatement.setInt(1, courseInstID);
			insertParticipantStatement.setInt(2, accountID);
			insertParticipantStatement.setString(3, roleType.toString());
			insertParticipantStatement.executeUpdate();
		}
	}
	
	public void insertFile(TransferableFile file) throws SQLException {
		try (PreparedStatement insertFileStatement = connection.prepareStatement(INSERT_FILE_SQL);) {
			insertFileStatement.setString(1, file.fileName);
			insertFileStatement.setString(2, file.fileType);
			insertFileStatement.setCharacterStream(3, new CharArrayReader(file.content));
			insertFileStatement.setInt(4, file.authorAccountID);
			insertFileStatement.setInt(5, file.refID);
			insertFileStatement.executeUpdate();
		}
	}

	/**
	 * Gets an account's information from the database using a user name input.
	 * Object[0] is the user name as a string,
	 * Object[1] is the password as a string,
	 * Object[2] is the first name of the account holder as a string,
	 * Object[3] is the last name of the account holder as a string,
	 * Object[4] is the date and time the account was created as a java.sql.Timestamp,
	 * Object[5] is the account's identifier in the database as an int.
	 * 
	 * @param user the user name of the account to be retrieved
	 * @return an object array containing the data of the account searched
	 * @throws EmptySetException if the call to the database returns nothing
	 * @throws SQLException if a database error occurs
	 */
	public Object[] selectAccountInfo(String user) throws EmptySetException, SQLException {
		Object[] dataToReturn = new Object[6];
		try (PreparedStatement selectAccountStatement = connection.prepareStatement(SELECT_ACCOUNT_SQL);) {
			selectAccountStatement.setString(1, user);
			try (ResultSet results = selectAccountStatement.executeQuery();) {
				if (!results.first()) {throw new EmptySetException();}
				else {
					dataToReturn[0] = results.getString(1);
					dataToReturn[1] = results.getString(2);
					dataToReturn[2] = results.getString(3);
					dataToReturn[3] = results.getString(4);
					dataToReturn[4] = results.getTimestamp(5);
					dataToReturn[5] = results.getInt(6);
				}
			}
		}
		return dataToReturn;
	}
	
	public List<Object[]> selectAccountRoles(String user) throws EmptySetException, SQLException {
		List<Object[]> dataToReturn = new ArrayList<Object[]>();
		try (PreparedStatement selectAccountRolesStatement = connection.prepareStatement(SELECT_ACCOUNT_ROLES_SQL);) {
			selectAccountRolesStatement.setString(1, user);
			try (ResultSet results = selectAccountRolesStatement.executeQuery();) {
				if (!results.first()) {throw new EmptySetException();}
				else {
					results.beforeFirst();
					while (results.next()){
						Object[] item = new Object[2];
						
						item[0] = results.getInt(1);
						item[1] = results.getString(2);	
						
						dataToReturn.add(item);
					}
				}
			}
		}
		return dataToReturn;
	}
	
	// returns the assignment based on the assign_id
	public Object[] selectAssignmentByRefID(int assignmentID) throws SQLException, EmptySetException {
		Object[] dataToReturn = new Object[5];
		try (PreparedStatement selectAssignmentByIDStatement = connection.prepareStatement(SELECT_ASSIGNMENT_BY_ID_SQL);) {
			selectAssignmentByIDStatement.setInt(1, assignmentID);
			try (ResultSet results = selectAssignmentByIDStatement.executeQuery();) {
				if (!results.first()) {throw new EmptySetException();}
				else {
					
					dataToReturn[0] = results.getInt(1);//number
					dataToReturn[1] = results.getTimestamp(2);//due_date
					dataToReturn[2] = results.getInt(3);//course_inst_id
					dataToReturn[3] = selectAssignmentFiles(assignmentID);//associated file ids
					dataToReturn[4] = results.getInt(4);//assign_id
				}
			}
		}
		return dataToReturn;
	}
	
	public int[] selectAssignmentFiles(int assignmentID) throws SQLException, EmptySetException {
		int[] dataToReturn = null;
		try (PreparedStatement selectAssignmentFilesStatement = connection.prepareStatement(SELECT_ASSIGNMENT_FILES_SQL);) {
			selectAssignmentFilesStatement.setInt(1, assignmentID);
			try (ResultSet results = selectAssignmentFilesStatement.executeQuery();) {
				if (!results.first()) {throw new EmptySetException();}
				else {
					results.beforeFirst();
					List<Integer> files = new ArrayList<>();
					while (results.next()){
						int fileID = results.getInt(1);
						files.add(fileID);
					}
					dataToReturn = new int[files.size()];
					for (int i = 0; i < files.size(); i++) {
						int fileID = files.get(i);
						dataToReturn[i] = fileID;
					}
				}
			}
		}
		return dataToReturn;
	}

	public List<Object[]> selectAssignments(String crn) throws SQLException, EmptySetException {
		List<Object[]> dataToReturn = new ArrayList<Object[]>();
		try (PreparedStatement selectAssignmentsStatement = connection.prepareStatement(SELECT_ASSIGNMENTS_SQL);) {
			selectAssignmentsStatement.setString(1, crn);
			try (ResultSet results = selectAssignmentsStatement.executeQuery();) {
				if (!results.first()) {throw new EmptySetException();}
				else {
					results.beforeFirst();
					while (results.next()){
						Object[] item = new Object[2];
						
						item[0] = results.getInt(1);//number
						item[1] = results.getInt(2);//assign_id
						
						dataToReturn.add(item);
					}
				}
			}
		}
		return dataToReturn;
	}
	
	// returns the course info based on the course_id
	public Object[] selectCourseByRefID(int id) throws SQLException, EmptySetException {
		Object[] dataToReturn = new Object[4];
		try (PreparedStatement selectCourseByIDStatement = connection.prepareStatement(SELECT_COURSE_BY_ID_SQL);) {
			selectCourseByIDStatement.setInt(1, id);
			try (ResultSet results = selectCourseByIDStatement.executeQuery();) {
				if (!results.first()) {throw new EmptySetException();}
				else {
					dataToReturn[0] = results.getString(1);//subject
					dataToReturn[1] = results.getString(2);//number
					dataToReturn[2] = results.getString(3);//name
					dataToReturn[3] = results.getInt(4);//course_id
				}
			}
		}
		return dataToReturn;
	}

	// returns the course info based on the course_id
	public Object[] selectCourseInst(String crn) throws SQLException, EmptySetException {
		Object[] dataToReturn = new Object[6];
		try (PreparedStatement selectCourseInstStatement = connection.prepareStatement(SELECT_COURSE_INST_SQL);) {
			selectCourseInstStatement.setString(1, crn);
			try (ResultSet results = selectCourseInstStatement.executeQuery();) {
				if (!results.first()) {throw new EmptySetException();}
				else {
					dataToReturn[0] = results.getString(1);//semester
					dataToReturn[1] = results.getString(2);//year_offered
					dataToReturn[2] = results.getString(3);//prof_name
					dataToReturn[3] = results.getString(4);//crn
					dataToReturn[4] = results.getInt(5);//course_id
					dataToReturn[5] = results.getInt(6);//course_inst_id
				}
			}
		}
		return dataToReturn;
	}
	
	// returns the course info based on the course_id
	public Object[] selectCourseInstByRefID(int id) throws SQLException, EmptySetException {
		Object[] dataToReturn = new Object[6];
		try (PreparedStatement selectCourseInstByIDStatement = connection.prepareStatement(SELECT_COURSE_INST_BY_ID_SQL);) {
			selectCourseInstByIDStatement.setInt(1, id);
			try (ResultSet results = selectCourseInstByIDStatement.executeQuery();) {
				if (!results.first()) {throw new EmptySetException();}
				else {
					dataToReturn[0] = results.getString(1);//semester
					dataToReturn[1] = results.getString(2);//year_offered
					dataToReturn[2] = results.getString(3);//prof_name
					dataToReturn[3] = results.getString(4);//crn
					dataToReturn[4] = results.getInt(5);//course_id
					dataToReturn[5] = results.getInt(6);//course_inst_id
				}
			}
		}
		return dataToReturn;
	}

	// returns the file based on the file_id
	public Object[] selectFileByRefID(int file_id) throws SQLException, EmptySetException, IOException {
		Object[] dataToReturn = new Object[6];
		try (PreparedStatement selectFileStatement = connection.prepareStatement(SELECT_FILE_BY_ID_SQL);) {
			selectFileStatement.setInt(1, file_id);
			try (ResultSet results = selectFileStatement.executeQuery();) {
				if (!results.first()) {throw new EmptySetException();}
				else {
					dataToReturn[0] = results.getString(1);//name
					dataToReturn[1] = results.getString(2);//type
					//Sorry this is going to get weird for a second!
					//This is a "try-with-resources" if you haven't seen it before.
					//The purpose of this whole big dilly is to store the file contents as a character array.
					//I'm pretty sure the db is bringing it over in utf-8.
					try (
						BufferedReader reader = 
							new BufferedReader(results.getCharacterStream(3));//contents
						CharArrayWriter writer = 
							new CharArrayWriter()
					) {
						int character;
						while ((character = reader.read()) != -1) {
							writer.write(character);
						}
						dataToReturn[2] = writer.toCharArray();//contents
					}
					dataToReturn[3] = results.getTimestamp(4);//date_added
					dataToReturn[4] = results.getInt(5);//account_id
					dataToReturn[5] = results.getInt(6);//file_id
				}
			}
		}
		return dataToReturn;
	}

	/*public ResultSet getFiles(int account_id) throws SQLException {
		this.getFilesStatement = connection.prepareStatement(GET_FILES_SQL);
		this.getFilesStatement.setString(1, Integer.toString(account_id));
		return this.getFilesStatement.executeQuery();
	}*/

	/*public ResultSet getGrade(int submission_id) throws SQLException {
		this.getGradeStatement = connection.prepareStatement(GET_GRADE_SQL);
		this.getGradeStatement.setString(1, Integer.toString(submission_id));
		return this.getGradeStatement.executeQuery();
	}*/

	public List<Object[]> selectParticipants(String crn) throws SQLException, EmptySetException {
		List<Object[]> dataToReturn = new ArrayList<Object[]>();
		try (PreparedStatement selectParticipantsStatement = connection.prepareStatement(SELECT_PARTICIPANTS_SQL);) {
			selectParticipantsStatement.setString(1, crn);
			try (ResultSet results = selectParticipantsStatement.executeQuery();) {
				if (!results.first()) {throw new EmptySetException();}
				else {
					results.beforeFirst();
					while (results.next()){
						Object[] item = new Object[2];
						
						item[0] = results.getInt(1);//account_id
						item[1] = results.getString(2);//role type
						
						dataToReturn.add(item);
					}
				}
			}
		}
		return dataToReturn;
	}

	/*public ResultSet getSubmission(int submission_id) throws SQLException {
		this.getSubmissionStatement = connection.prepareStatement(GET_SUBMISSION_SQL);
		this.getSubmissionStatement.setString(1, Integer.toString(submission_id));
		return this.getSubmissionStatement.executeQuery();
	}*/
	
	/*public ResultSet getSubmissionFiles(int submission_id) throws SQLException {
		this.getSubmissionFilesStatement = connection.prepareStatement(GET_SUBMISSION_FILES_SQL);
		this.getSubmissionFilesStatement.setString(1, Integer.toString(submission_id));

		return this.getSubmissionFilesStatement.executeQuery();
	}*/
	
	

	/*public ResultSet postAssignment(String num, int course_id, Date date, int test_id, String assign) throws SQLException {
		this.postAssignmentStatement = connection.prepareStatement(POST_ASSIGNMENT_SQL);
		this.postAssignmentStatement.setString(1, num);
		this.postAssignmentStatement.setString(2, Integer.toString(course_id));
		// this.post_assignment_statement.setString(3, date);
		this.postAssignmentStatement.setString(4, Integer.toString(test_id));
		this.postAssignmentStatement.setString(5, assign);
		return this.postAssignmentStatement.executeQuery();
	}*/

	// returns posting a course
	/*public ResultSet postCourse(String number, String subject, String name) throws SQLException {
		this.postCourseStatement = connection.prepareStatement(POST_COURSE_SQL);
		this.postCourseStatement.setString(1, number);
		this.postCourseStatement.setString(2, subject);
		this.postCourseStatement.setString(3, name);
		return this.postCourseStatement.executeQuery();
	}*/

	/*public ResultSet postFile(String name, Date date, String text) throws SQLException {
		this.postFileStatement = connection.prepareStatement(POST_FILE_SQL);
		this.postFileStatement.setString(1, name);
		// this.post_course_statement.setString(2, date);
		this.postFileStatement.setString(3, text);
		return this.postFileStatement.executeQuery();
	}*/

	/*public ResultSet postGrade(double grade) throws SQLException {
		this.postGradeStatement = connection.prepareStatement(POST_GRADE_SQL);
		this.postGradeStatement.setString(1, Double.toString(grade));
		return this.postGradeStatement.executeQuery();
	}*/

	

	/*public ResultSet postSubmission(int account_id, int assign_id) throws SQLException {
		this.postSubmissionStatement = connection.prepareStatement(POST_SUBMISSION_SQL);
		this.postSubmissionStatement.setString(1, Integer.toString(account_id));
		this.postSubmissionStatement.setString(2, Integer.toString(assign_id));
		return this.postSubmissionStatement.executeQuery();
	}*/

	/*public ResultSet postSubmissionFile(int file_id) throws SQLException {
		this.postSubmissionFileStatement = connection.prepareStatement(POST_SUBMISSION_FILE_SQL);
		this.postSubmissionFileStatement.setString(1, Integer.toString(file_id));
		return this.postSubmissionFileStatement.executeQuery();
	}*/
	
	/**
	 * Updates the information of a user on the database. Note: the user name cannot be updated or changed.
	 * 
	 * @param username the user name of the account to be updated
	 * @param newPassword the new password of the account
	 * @param newFirstName the new first name on the account
	 * @param newLastName the new last name on the account
	 * @throws SQLException
	 */
	public void updateAccount(String username, String newPassword, String newFirstName, String newLastName) throws SQLException {
		try (PreparedStatement updateAccountStatement = connection.prepareStatement(UPDATE_ACCOUNT_SQL);) {
			updateAccountStatement.setString(1, newPassword);
			updateAccountStatement.setString(2, newFirstName);
			updateAccountStatement.setString(3, newLastName);
			updateAccountStatement.setString(4, username);
			updateAccountStatement.executeUpdate();
		}
	}
	
	//INHERITED/IMPLEMENTED METHODS
	/**
	 * Gracefully releases this SQLHandler's resources.
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	public void close() throws SQLException{
		this.connection.close();
	}
}