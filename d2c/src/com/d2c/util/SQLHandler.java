package com.d2c.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

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
	private static final String GET_ACCOUNT_SQL = 
				"SELECT fname, lname, create_time, account_id "
			+ 	"FROM account "
			+ 	"WHERE username = ? AND password = ?";
	//Get an assignment's number, due date, and contents using it's assignment ID.
	//TODO:Verify this works.
	private static final String GET_ASSIGNMENT_SQL = 	
				"SELECT number, due_date, assign "
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
	//TODO:Verify this works.
	private static final String POST_ACCOUNT_SQL = 
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
	private static final String POST_SUBMISSION_FILES_SQL = 
				"INSERT INTO submission_file (submission_id,file_id) "
			+ 	"VALUES(?, ?)";

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
	private PreparedStatement postSubmissionFilesStatement;

	public SQLHandler() throws SQLException, ClassNotFoundException {
		//Make a connection
		Class.forName(DB_DRIVER);
		this.connectionProperties = new Properties();
		this.connectionProperties.put("user", DB_USER);
		this.connectionProperties.put("password", DB_PASSWORD);
		System.out.println("attempting to get connection");
		this.connection = DriverManager.getConnection(DB_CONNECTION, connectionProperties);
		
		//Set up statements
		// this.example_statement = connection.prepareStatement(EXAMPLE_SQL);
		this.getAccountStatement = connection.prepareStatement(GET_ACCOUNT_SQL);
		this.getAssignmentStatement = connection.prepareStatement(GET_ASSIGNMENT_SQL);
		this.getCourseStatement = connection.prepareStatement(GET_COURSE_SQL);
		this.getFileStatement = connection.prepareStatement(GET_FILE_SQL);
		this.getFilesStatement = connection.prepareStatement(GET_FILES_SQL);
		this.getGradeStatement = connection.prepareStatement(GET_GRADE_SQL);
		this.getParticipantsStatement = connection.prepareStatement(GET_PARTICIPANTS_SQL);
		this.getSubmissionStatement = connection.prepareStatement(GET_SUBMISSION_SQL);
		this.getSubmissionFilesStatement = connection.prepareStatement(GET_SUBMISSION_FILES_SQL);
		this.postAccountStatement = connection.prepareStatement(POST_ACCOUNT_SQL);
		this.postAssignmentStatement = connection.prepareStatement(POST_ASSIGNMENT_SQL);
		this.postCourseStatement = connection.prepareStatement(POST_COURSE_SQL);
		this.postFileStatement = connection.prepareStatement(POST_FILE_SQL);
		this.postGradeStatement = connection.prepareStatement(POST_GRADE_SQL);
		this.postParticipantStatement = connection.prepareStatement(POST_PARTICIPANT_SQL);
		this.postSubmissionStatement = connection.prepareStatement(POST_SUBMISSION_SQL);
		this.postSubmissionFilesStatement = connection.prepareStatement(POST_SUBMISSION_FILES_SQL);
	}
	/*
	 * public ResultSet getExample(String column, String value) throws
	 * SQLException { this.example_statement.setString(0, column);
	 * this.example_statement.setString(1, value); return
	 * this.example_statement.executeQuery(); }
	 */

	// returns the account info based on user and password
	public ResultSet getAccountInfo(String user, String password) throws SQLException {
		//Set username and password
		this.getAccountStatement.setString(1, user);
		this.getAccountStatement.setString(2, password);
		//Execute the query
		return this.getAccountStatement.executeQuery();
	}

	public ResultSet getAssignment(String assign_id) throws SQLException {
		this.postCourseStatement.setString(1, assign_id);
		return this.getAssignmentStatement.executeQuery();
	}

	// returns the course info based on the course_id
	public ResultSet getCourseInfo(int crn) throws SQLException {
		this.getCourseStatement.setString(1, Integer.toString(crn));
		return this.getCourseStatement.executeQuery();
	}

	// returns the file based on the file_id
	public ResultSet getFile(int file_id) throws SQLException {
		this.getFileStatement.setString(1, Integer.toString(file_id));
		return this.getFileStatement.executeQuery();
	}

	public ResultSet getFiles(int account_id) throws SQLException {
		this.getFilesStatement.setString(1, Integer.toString(account_id));
		return this.getFilesStatement.executeQuery();
	}

	public ResultSet getGrade(int submission_id) throws SQLException {
		this.getGradeStatement.setString(1, Integer.toString(submission_id));
		return this.getGradeStatement.executeQuery();
	}

	public ResultSet getSubmission(int submission_id) throws SQLException {
		this.getSubmissionStatement.setString(1, Integer.toString(submission_id));
		return this.getSubmissionStatement.executeQuery();
	}

	// private PreparedStatement get_submission_file_statement;
	public ResultSet getSubmissionFile(int submission_id) throws SQLException {
		this.getSubmissionFilesStatement.setString(1, Integer.toString(submission_id));

		return this.getSubmissionFilesStatement.executeQuery();
	}

	public ResultSet getTA(String course_id) throws SQLException {
		this.getParticipantsStatement.setString(1, course_id);
		return this.getParticipantsStatement.executeQuery();
	}

	public ResultSet postAccount(String username, String password, String fname, String lname) throws SQLException {
		this.postAccountStatement.setString(1, username);
		this.postAccountStatement.setString(2, password);
		this.postAccountStatement.setString(3, fname);
		this.postAccountStatement.setString(4, lname);

		return this.postAccountStatement.executeQuery();
	}

	public ResultSet postAssignment(String num, int course_id, Date date, int test_id, String assign)
			throws SQLException {
		this.postAssignmentStatement.setString(1, num);
		this.postAssignmentStatement.setString(2, Integer.toString(course_id));
		// this.post_assignment_statement.setString(3, date);
		this.postAssignmentStatement.setString(4, Integer.toString(test_id));
		this.postAssignmentStatement.setString(5, assign);
		return this.postAssignmentStatement.executeQuery();
	}

	// returns posting a course
	public ResultSet postCourse(String number, String subject, String name) throws SQLException {
		this.postCourseStatement.setString(1, number);
		this.postCourseStatement.setString(2, subject);
		this.postCourseStatement.setString(3, name);
		return this.postCourseStatement.executeQuery();
	}

	public ResultSet postFile(String name, Date date, String text) throws SQLException {
		this.postFileStatement.setString(1, name);
		// this.post_course_statement.setString(2, date);
		this.postFileStatement.setString(3, text);
		return this.postFileStatement.executeQuery();
	}

	public ResultSet postGrade(double grade) throws SQLException {
		this.postGradeStatement.setString(1, Double.toString(grade));
		return this.postGradeStatement.executeQuery();
	}

	public ResultSet postRole(int course_id, int account_id, String type) throws SQLException {
		this.postParticipantStatement.setString(1, Integer.toString(course_id));
		this.postParticipantStatement.setString(2, Integer.toString(account_id));
		this.postParticipantStatement.setString(3, type);
		return this.postParticipantStatement.executeQuery();

	}

	// private PreparedStatement post_submission_statement;
	public ResultSet postSubmission(int account_id, int assign_id) throws SQLException {
		this.postSubmissionStatement.setString(1, Integer.toString(account_id));
		this.postSubmissionStatement.setString(2, Integer.toString(assign_id));
		return this.postSubmissionStatement.executeQuery();
	}

	// private PreparedStatement post_submission_file_statement;
	public ResultSet postSubmissionFile(int file_id) throws SQLException {
		this.postSubmissionFilesStatement.setString(1, Integer.toString(file_id));
		return this.postSubmissionFilesStatement.executeQuery();
	}
	
	public void close() throws SQLException{
		this.getAccountStatement.close();
		this.getCourseStatement.close();
		this.getAssignmentStatement.close();
		this.getFilesStatement.close();
		this.getFileStatement.close();
		this.getGradeStatement.close();
		this.getSubmissionFilesStatement.close();
		this.getSubmissionStatement.close();
		this.postAccountStatement.close();
		this.postAccountStatement.close();
		this.postAssignmentStatement.close();
		this.postCourseStatement.close();
		this.postFileStatement.close();
		this.postGradeStatement.close();
		this.postParticipantStatement.close();
		this.postSubmissionFilesStatement.close();
		this.postSubmissionStatement.close();
		this.connection.close();
	}
}