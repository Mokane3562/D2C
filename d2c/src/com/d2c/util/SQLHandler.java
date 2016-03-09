package com.d2c.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class SQLHandler {
	private String host;
	private String port;
	private Properties connectionProperties;
	private Connection connection;
	
	/*private static final String EXAMPLE_SQL = 
	 * 			"SELECT ? " 
	 * 		+	"FROM SOME_TABLE "
	 * 		+	"WHERE SOME_COLUMN = ?";
	 */
	//Get a user's first name, last name, and account ID using their user-name and password. 
	private static final String GET_ACCOUNT_SQL = 
				"SELECT fname, lname, account_id "
			+ 	"FROM account "
			+ 	"WHERE username = ? AND password = ?";
	//Get an assignment's number, due date, and contents using it's assignment ID.
	private static final String GET_ASSIGNMENT_SQL = 	
				"SELECT number, due_date, assign "
			+ 	"FROM assignment "
			+ 	"WHERE assign_id = ? ";
	//Get a course's subject, number, and name, using it's course ID.
	private static final String GET_COURSE_SQL = 	
				"SELECT subject, number, name "
			+ 	"FROM course "
			+ 	"WHERE course_id = ? ";
	//Get a file's name, date of creation, and contents using it's file ID.
	private static final String GET_FILE_SQL = 	
				"SELECT name, date_added, contents "
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
	private static final String GET_PARTICIPANTS_SQL = 
				"SELECT account_id, type"
			+ 	"FROM role "
			+ 	"WHERE course_id = ?";
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
	private static final String POST_ACCOUNT_SQL = 
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
	private static final String POST_FILE_SQL = 
				"INSERT INTO file (name, contents) "
			+ 	"VALUES (?, ?)";
	//Change/update a grade for a submission.
	private static final String POST_GRADE_SQL = 
				"UPDATE submission "
			+ 	"SET grade = ? "
			+ 	"WHERE submission_id = ?";
	//Add a participant to a course. Other fields will be generated automatically.
	private static final String POST_PARTICIPANT_SQL = 
				"INSERT INTO role (course_id, account_id, type) "
			+ 	"VALUES (?, ?, ?)";
	//TODO:Make sure all the files attached to a submission are reachable
	private static final String POST_SUBMISSION_SQL = 
				"INSERT INTO submission (account_id, assign_id)"
			+ 	"VALUES (?, ?)";
	//Add a file to a submission.
	private static final String POST_SUBMISSION_FILE_SQL = 
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
	private PreparedStatement getSubmissionFileStatement;
	private PreparedStatement postAccountStatement;
	private PreparedStatement postAssignmentStatement;
	private PreparedStatement postCourseStatement;
	private PreparedStatement postFileStatement;
	private PreparedStatement postGradeStatement;
	private PreparedStatement postParticipantStatement;
	private PreparedStatement postSubmissionStatement;
	private PreparedStatement postSubmissionFileStatement;

	public SQLHandler() throws SQLException {
		// TODO replace placeholder string literals with real host and port
		this.host = "localhost";
		this.port = "50000";
		this.connectionProperties = new Properties();
		// TODO replace placeholder string literals with real user and password
		this.connectionProperties.put("user", "root");
		this.connectionProperties.put("password", "password");
		this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/", connectionProperties);

		// this.example_statement = connection.prepareStatement(EXAMPLE_SQL);
		this.getAccountStatement = connection.prepareStatement(GET_ACCOUNT_SQL);
		this.getAccountStatement = connection.prepareStatement(GET_COURSE_SQL);
		this.getAssignmentStatement = connection.prepareStatement(GET_ASSIGNMENT_SQL);
		this.getFilesStatement = connection.prepareStatement(GET_FILES_SQL);
		this.getFileStatement = connection.prepareStatement(GET_FILE_SQL);
		this.getGradeStatement = connection.prepareStatement(GET_GRADE_SQL);
		this.getSubmissionFileStatement = connection.prepareStatement(GET_SUBMISSION_FILES_SQL);
		this.getSubmissionStatement = connection.prepareStatement(GET_SUBMISSION_SQL);
		this.postAccountStatement = connection.prepareStatement(GET_PARTICIPANTS_SQL);
		this.postAccountStatement = connection.prepareStatement(POST_ACCOUNT_SQL);
		this.postAssignmentStatement = connection.prepareStatement(POST_ASSIGNMENT_SQL);
		this.postCourseStatement = connection.prepareStatement(POST_COURSE_SQL);
		this.postFileStatement = connection.prepareStatement(POST_FILE_SQL);
		this.postGradeStatement = connection.prepareStatement(POST_GRADE_SQL);
		this.postParticipantStatement = connection.prepareStatement(POST_PARTICIPANT_SQL);
		this.postSubmissionFileStatement = connection.prepareStatement(POST_SUBMISSION_FILE_SQL);
		this.postSubmissionStatement = connection.prepareStatement(POST_SUBMISSION_SQL);
	}
	/*
	 * public ResultSet getExample(String column, String value) throws
	 * SQLException { this.example_statement.setString(0, column);
	 * this.example_statement.setString(1, value); return
	 * this.example_statement.executeQuery(); }
	 */

	// returns the account info based on user and password
	public ResultSet getAccountInfo(String user, String password) throws SQLException {
		this.getAccountStatement.setString(0, user);
		this.getAccountStatement.setString(1, password);
		return this.getAccountStatement.executeQuery();
	}

	public ResultSet getAssignment(String assign_id) throws SQLException {
		this.postCourseStatement.setString(0, assign_id);
		return this.getAssignmentStatement.executeQuery();
	}

	// returns the course info based on the course_id
	public ResultSet getCoursetInfo(int course_id) throws SQLException {
		this.getCourseStatement.setString(0, Integer.toString(course_id));
		return this.getCourseStatement.executeQuery();
	}

	// returns the file based on the file_id
	public ResultSet getFile(int file_id) throws SQLException {
		this.getFileStatement.setString(0, Integer.toString(file_id));
		return this.getFileStatement.executeQuery();
	}

	public ResultSet getFiles(int account_id) throws SQLException {
		this.getFilesStatement.setString(0, Integer.toString(account_id));
		return this.getFilesStatement.executeQuery();
	}

	public ResultSet getGrade(int submission_id) throws SQLException {
		this.getGradeStatement.setString(0, Integer.toString(submission_id));
		return this.getGradeStatement.executeQuery();
	}

	public ResultSet getSubmission(int submission_id) throws SQLException {
		this.getSubmissionStatement.setString(0, Integer.toString(submission_id));
		return this.getSubmissionStatement.executeQuery();
	}

	// private PreparedStatement get_submission_file_statement;
	public ResultSet getSubmissionFile(int submission_id) throws SQLException {
		this.getSubmissionFileStatement.setString(0, Integer.toString(submission_id));

		return this.getSubmissionFileStatement.executeQuery();
	}

	public ResultSet getTA(String course_id) throws SQLException {
		this.getParticipantsStatement.setString(0, course_id);
		return this.getParticipantsStatement.executeQuery();
	}

	public ResultSet postAccount(String username, String password, String fname, String lname) throws SQLException {
		this.postAccountStatement.setString(0, username);
		this.postAccountStatement.setString(1, password);
		this.postAccountStatement.setString(2, fname);
		this.postAccountStatement.setString(3, lname);

		return this.postAccountStatement.executeQuery();
	}

	public ResultSet postAssignment(String num, int course_id, Date date, int test_id, String assign)
			throws SQLException {
		this.postAssignmentStatement.setString(0, num);
		this.postAssignmentStatement.setString(1, Integer.toString(course_id));
		// this.post_assignment_statement.setString(2, date);
		this.postAssignmentStatement.setString(3, Integer.toString(test_id));
		this.postAssignmentStatement.setString(5, assign);
		return this.postAssignmentStatement.executeQuery();
	}

	// returns posting a course
	public ResultSet postCourse(String number, String subject, String name) throws SQLException {
		this.postCourseStatement.setString(0, number);
		this.postCourseStatement.setString(1, subject);
		this.postCourseStatement.setString(2, name);
		return this.postCourseStatement.executeQuery();
	}

	public ResultSet postFile(String name, Date date, String text) throws SQLException {
		this.postFileStatement.setString(0, name);
		// this.post_course_statement.setString(1, date);
		this.postFileStatement.setString(2, text);
		return this.postFileStatement.executeQuery();
	}

	public ResultSet postGrade(double grade) throws SQLException {
		this.postGradeStatement.setString(0, Double.toString(grade));
		return this.postGradeStatement.executeQuery();
	}

	public ResultSet postRole(int course_id, int account_id, String type) throws SQLException {
		this.postParticipantStatement.setString(0, Integer.toString(course_id));
		this.postParticipantStatement.setString(1, Integer.toString(account_id));
		this.postParticipantStatement.setString(2, type);
		return this.postParticipantStatement.executeQuery();

	}

	// private PreparedStatement post_submission_statement;
	public ResultSet postSubmission(int account_id, int assign_id) throws SQLException {
		this.postSubmissionStatement.setString(0, Integer.toString(account_id));
		this.postSubmissionStatement.setString(1, Integer.toString(assign_id));
		return this.postSubmissionStatement.executeQuery();
	}

	// private PreparedStatement post_submission_file_statement;
	public ResultSet postSubmissionFile(int file_id) throws SQLException {
		this.postSubmissionFileStatement.setString(0, Integer.toString(file_id));
		return this.postSubmissionFileStatement.executeQuery();
	}
}