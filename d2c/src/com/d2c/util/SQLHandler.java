package com.d2c.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

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
	//Get an assignment's number, due date, and contents using it's assignment ID.
	private static final String GET_ASSIGNMENT_SQL = 	
				"SELECT number, due_date, course_inst_id "
			+ 	"FROM assignment "
			+ 	"WHERE assign_id = ? ";
	//Get a course's subject, number, and name, using it's crn.
	private static final String GET_COURSE_SQL = 	
				"SELECT subject, number, name "
			+ 	"FROM course, course_inst "
			+ 	"WHERE crn = ? "
			+ 	"AND course.course_id = course_inst.course_id";
	//Get a file's name, date of creation, and contents using it's file ID.
	//TODO:Verify this works.
	private static final String GET_FILE_SQL = 	
				"SELECT name, date_added, contents "
			+ 	"FROM file "
			+ 	"WHERE file_id = ?";
	//Get the name, date of creation, and contents of every file a user created using that user's ID.
	//TODO:Verify this works.
	private static final String GET_FILES_SQL = 	
				"SELECT file_id, name, date_added, contents "
			+ 	"FROM file "
			+ 	"WHERE account_id = ?";
	//Get a submission's grade using it's ID.
	//TODO:Verify this works.
	private static final String GET_GRADE_SQL = 
				"SELECT grade "
			+ 	"FROM submission "
			+ 	"WHERE submission_id = ?";
	//Get the account ID and role of every user participating in a certain course.
	//TODO:Verify this works.
	private static final String GET_PARTICIPANTS_SQL = 
				"SELECT account_id, type"
			+ 	"FROM role "
			+ 	"WHERE course_id = ?";
	//Get a submission's submitter, associated assignment, and grade using it's submission ID.
	//TODO:Verify this works.
	private static final String GET_SUBMISSION_SQL = 
				"SELECT account_id, assign_id, grade "
			+ 	"FROM submission "
			+ 	"WHERE submission_id= ?";
	//Get the file ID's of files associated with a submission using that submission's ID.
	//TODO:Verify this works.
	private static final String GET_SUBMISSION_FILES_SQL = 
				"SELECT file_id "
			+ 	"FROM submission_file "
			+ 	"WHERE submission_id = ?";
	//Store a new account in the database. Other fields will be generated automatically.
	private static final String INSERT_ACCOUNT_SQL = 
				"INSERT INTO account (username, password, fname, lname) "
			+ 	"VALUES (?, ?, ?, ?)";
	//Store a new assignment in the database. Other fields will be generated automatically.
	//TODO:Verify this works.
	private static final String POST_ASSIGNMENT_SQL = 
				"INSERT INTO assignment (number, course_id, file_id ,due_date, assign) "
			+ 	"VALUES (?, ?, ?, ?, ?)";
	//Store a new course in the database. Other fields will be generated automatically.
	//TODO:Verify this works.
	private static final String POST_COURSE_SQL = 
				"INSERT INTO course (number, subject, name) "
			+ 	"VALUES ( ?, ?, ? )";
	//Store a file in the database. Other fields will be generated automatically.
	//TODO:Verify this works.
	private static final String POST_FILE_SQL = 
				"INSERT INTO file (name, contents) "
			+ 	"VALUES (?, ?)";
	//Change/update a grade for a submission.
	//TODO:Verify this works.
	private static final String POST_GRADE_SQL = 
				"UPDATE submission "
			+ 	"SET grade = ? "
			+ 	"WHERE submission_id = ?";
	//Add a participant to a course. Other fields will be generated automatically.
	//TODO:Verify this works.
	private static final String POST_PARTICIPANT_SQL = 
				"INSERT INTO role (course_id, account_id, type) "
			+ 	"VALUES (?, ?, ?)";
	//TODO:Make sure all the files attached to a submission are reachable
	//TODO:Verify this works.
	private static final String POST_SUBMISSION_SQL = 
				"INSERT INTO submission (account_id, assign_id)"
			+ 	"VALUES (?, ?)";
	//Add a file to a submission.
	//TODO:Verify this works.
	private static final String POST_SUBMISSION_FILE_SQL = 
				"INSERT INTO submission_file (submission_id,file_id) "
			+ 	"VALUES(?, ?)";
	//update an account
	private static final String UPDATE_ACCOUNT_SQL = 
				"UPDATE account "
			+ 	"SET password=?, fname=?, lname=?"
			+ 	"WHERE username = ?";
	// private PreparedStatement exampleStatement;
	private PreparedStatement getAccountStatement;
	private PreparedStatement getAssignmentStatement;
	private PreparedStatement getCourseStatement;
	private PreparedStatement getFileStatement;
	private PreparedStatement getFilesStatement;
	private PreparedStatement getGradeStatement;
	private PreparedStatement getParticipantsStatement;
	private PreparedStatement getSubmissionStatement;
	private PreparedStatement getSubmissionFilesStatement;
	private PreparedStatement postAccountStatement;
	private PreparedStatement postAssignmentStatement;
	private PreparedStatement postCourseStatement;
	private PreparedStatement postFileStatement;
	private PreparedStatement postGradeStatement;
	private PreparedStatement postParticipantStatement;
	private PreparedStatement postSubmissionStatement;
	private PreparedStatement postSubmissionFileStatement;

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
	public static void main(String[] args) {
		try (SQLHandler sql = new SQLHandler();) {
			Object[] results = sql.getAccountInfo("mlc258");
			for (Object o: results) {
				System.out.println(o);
			}	
		} catch (SQLException | ClassNotFoundException | EmptySetException e) {
			e.printStackTrace();
		}
	}
	
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

	// returns the account info based on user and password
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
	public Object[] getAccountInfo(String user) throws EmptySetException, SQLException {
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

	public ResultSet getAssignment(String assign_id) throws SQLException {
		this.getAssignmentStatement = connection.prepareStatement(GET_ASSIGNMENT_SQL);
		this.postCourseStatement.setString(1, assign_id);
		return this.getAssignmentStatement.executeQuery();
	}

	// returns the course info based on the course_id
	public ResultSet getCourseInfo(int crn) throws SQLException {
		this.getCourseStatement = connection.prepareStatement(GET_COURSE_SQL);
		this.getCourseStatement.setString(1, Integer.toString(crn));
		return this.getCourseStatement.executeQuery();
	}

	// returns the file based on the file_id
	public ResultSet getFile(int file_id) throws SQLException {
		this.getFileStatement = connection.prepareStatement(GET_FILE_SQL);
		this.getFileStatement.setString(1, Integer.toString(file_id));
		return this.getFileStatement.executeQuery();
	}

	public ResultSet getFiles(int account_id) throws SQLException {
		this.getFilesStatement = connection.prepareStatement(GET_FILES_SQL);
		this.getFilesStatement.setString(1, Integer.toString(account_id));
		return this.getFilesStatement.executeQuery();
	}

	public ResultSet getGrade(int submission_id) throws SQLException {
		this.getGradeStatement = connection.prepareStatement(GET_GRADE_SQL);
		this.getGradeStatement.setString(1, Integer.toString(submission_id));
		return this.getGradeStatement.executeQuery();
	}

	public ResultSet getParticipants(int crn) throws SQLException {
		this.getParticipantsStatement = connection.prepareStatement(GET_PARTICIPANTS_SQL);
		this.getParticipantsStatement.setString(1, Integer.toString(crn));
		return this.getParticipantsStatement.executeQuery();
	}

	public ResultSet getSubmission(int submission_id) throws SQLException {
		this.getSubmissionStatement = connection.prepareStatement(GET_SUBMISSION_SQL);
		this.getSubmissionStatement.setString(1, Integer.toString(submission_id));
		return this.getSubmissionStatement.executeQuery();
	}
	
	public ResultSet getSubmissionFiles(int submission_id) throws SQLException {
		this.getSubmissionFilesStatement = connection.prepareStatement(GET_SUBMISSION_FILES_SQL);
		this.getSubmissionFilesStatement.setString(1, Integer.toString(submission_id));

		return this.getSubmissionFilesStatement.executeQuery();
	}
	
	/**
	 * Creates a new account on the database.
	 * 
	 * @param username the account user name
	 * @param password the account password
	 * @param fname the first name of the account holder
	 * @param lname the last name of the account holder
	 * @throws SQLException if a database error occurs
	 */
	public void makeAccount(String username, String password, String fname, String lname) throws SQLException {
		try (PreparedStatement insertAccountStatement = connection.prepareStatement(INSERT_ACCOUNT_SQL);) {
			insertAccountStatement.setString(1, username);
			insertAccountStatement.setString(2, password);
			insertAccountStatement.setString(3, fname);
			insertAccountStatement.setString(4, lname);
			insertAccountStatement.executeUpdate();
		}
	}

	public ResultSet postAssignment(String num, int course_id, Date date, int test_id, String assign) throws SQLException {
		this.postAssignmentStatement = connection.prepareStatement(POST_ASSIGNMENT_SQL);
		this.postAssignmentStatement.setString(1, num);
		this.postAssignmentStatement.setString(2, Integer.toString(course_id));
		// this.post_assignment_statement.setString(3, date);
		this.postAssignmentStatement.setString(4, Integer.toString(test_id));
		this.postAssignmentStatement.setString(5, assign);
		return this.postAssignmentStatement.executeQuery();
	}

	// returns posting a course
	public ResultSet postCourse(String number, String subject, String name) throws SQLException {
		this.postCourseStatement = connection.prepareStatement(POST_COURSE_SQL);
		this.postCourseStatement.setString(1, number);
		this.postCourseStatement.setString(2, subject);
		this.postCourseStatement.setString(3, name);
		return this.postCourseStatement.executeQuery();
	}

	public ResultSet postFile(String name, Date date, String text) throws SQLException {
		this.postFileStatement = connection.prepareStatement(POST_FILE_SQL);
		this.postFileStatement.setString(1, name);
		// this.post_course_statement.setString(2, date);
		this.postFileStatement.setString(3, text);
		return this.postFileStatement.executeQuery();
	}

	public ResultSet postGrade(double grade) throws SQLException {
		this.postGradeStatement = connection.prepareStatement(POST_GRADE_SQL);
		this.postGradeStatement.setString(1, Double.toString(grade));
		return this.postGradeStatement.executeQuery();
	}

	public ResultSet postParticipant(int course_id, int account_id, String type) throws SQLException {
		this.postParticipantStatement = connection.prepareStatement(POST_PARTICIPANT_SQL);
		this.postParticipantStatement.setString(1, Integer.toString(course_id));
		this.postParticipantStatement.setString(2, Integer.toString(account_id));
		this.postParticipantStatement.setString(3, type);
		return this.postParticipantStatement.executeQuery();
	}

	public ResultSet postSubmission(int account_id, int assign_id) throws SQLException {
		this.postSubmissionStatement = connection.prepareStatement(POST_SUBMISSION_SQL);
		this.postSubmissionStatement.setString(1, Integer.toString(account_id));
		this.postSubmissionStatement.setString(2, Integer.toString(assign_id));
		return this.postSubmissionStatement.executeQuery();
	}

	public ResultSet postSubmissionFile(int file_id) throws SQLException {
		this.postSubmissionFileStatement = connection.prepareStatement(POST_SUBMISSION_FILE_SQL);
		this.postSubmissionFileStatement.setString(1, Integer.toString(file_id));
		return this.postSubmissionFileStatement.executeQuery();
	}
	
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