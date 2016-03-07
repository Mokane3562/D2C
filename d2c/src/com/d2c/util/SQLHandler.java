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
	//private static String example_sql = "SELECT ? FROM SOME_TABLE WHERE SOME_COLUMN = ?";
    private static String get_account_sql = "SELECT fname, lname, account_id FROM account WHERE username = ? AND password = ?";
	private static String get_course_sql ="SELECT number, subject,name FROM course WHERE course_id = ? ";
    private static String post_course_sql ="INSERT INTO course(number, subject, name) VALUES( ?, ?, ? )";
	private static String get_file_sql = "SELECT name, binary_date, test_data FROM file WHERE file_id = ?";
	private static String get_files_sql = "SELECT file_id name, binary_date, test_data FROM file WHERE account_id = ?";
    private static String post_file_sql = "INSERT INTO file(name, binary_date, test_data) VALUES(?, ?, ?)";
	private static String get_assignment_sql = "SELECT number, due_date, assign FROM assignment WHERE assign_id = ? ";
	private static String post_assignment_sql = "INSERT INTO assignment(number, course_id, test_id ,due_date,  assign) VALUES(?, ?, ?, ?, ?)";
	private static String get_grade_sql = "SELECT grade FROM submission WHERE submission_id = ?";
	private static String post_grade_sql = "INSERT INTO submission(grade) WHERE submission_id = ?";
	private static String post_account_sql = "INSERT INTO account(username, password,fname, lname) VALUES(?,?,?, ?)";
	private static String get_ta_sql = "SELECT account_id FROM role WHERE course_id = ?";
	private static String post_role_sql="INSERT INTO role(course_id,account_id,type) VALUES(?, ?, ?)";
	private static String get_submission_sql="SELECT account_id, assign_id, grade FROM submission WHERE submission_id= ?";
	private static String post_submission_sql="INSERT INTO submission(account_id, assign_id)";
	private static String get_submission_file_sql="SELECT file_id FROM submission_file WHERE submission_id = ?";
	private static String post_submission_file_sql="INSERT INTO submission_file(submission_id,file_id) VALUES(?, ?)";
	//private PreparedStatement example_statement;
    private PreparedStatement get_account_statement;
    private PreparedStatement get_course_statement;
    private PreparedStatement post_course_statement;
    private PreparedStatement get_file_statement;
    private PreparedStatement get_files_statement;
    private PreparedStatement post_file_statement;
    private PreparedStatement get_assignment_statement;
    private PreparedStatement post_assignment_statement;
    private PreparedStatement get_grade_statement;
    private PreparedStatement post_grade_statement;
    private PreparedStatement post_account_statement;
    private PreparedStatement get_ta_statement;
    private PreparedStatement post_role_statement;
    private PreparedStatement get_submission_statement;
    private PreparedStatement post_submission_statement;
    private PreparedStatement get_submission_file_statement;
    private PreparedStatement post_submission_file_statement;

	public SQLHandler() throws SQLException {
		// TODO replace placeholder string literals with real host and port
		this.host = "localhost";
		this.port = "50000";
		this.connectionProperties = new Properties();
		// TODO replace placeholder string literals with real user and password
		this.connectionProperties.put("user", "root");
		this.connectionProperties.put("password", "password");
		this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/", connectionProperties);
		//this.example_statement = connection.prepareStatement(example_sql);      
        this.get_account_statement = connection.prepareStatement(get_account_sql);
        this.get_account_statement = connection.prepareStatement(get_course_sql);
        this.post_course_statement = connection.prepareStatement(post_course_sql);
        this.get_file_statement = connection.prepareStatement(get_file_sql);
        this.get_files_statement = connection.prepareStatement(get_files_sql);
        this.post_file_statement = connection.prepareStatement(post_file_sql);
        this.get_assignment_statement = connection.prepareStatement(get_assignment_sql);
        this.post_assignment_statement = connection.prepareStatement(post_assignment_sql);
        this.get_grade_statement = connection.prepareStatement(get_grade_sql);
        this.post_grade_statement= connection.prepareStatement(post_grade_sql);
        this.post_account_statement= connection.prepareStatement(post_account_sql);
        this.post_account_statement=connection.prepareStatement(get_ta_sql);
        this.post_role_statement=connection.prepareStatement(post_role_sql);
        this.get_submission_statement=connection.prepareStatement(get_submission_sql);
        this.post_submission_statement=connection.prepareStatement(post_submission_sql);
        this.get_submission_file_statement=connection.prepareStatement(get_submission_file_sql);
        this.post_submission_file_statement=connection.prepareStatement(post_submission_file_sql);
	}
	/*
	public ResultSet getExample(String column, String value) throws SQLException {
		this.example_statement.setString(0, column);
		this.example_statement.setString(1, value);
		return this.example_statement.executeQuery();
	}
	*/
	
	  //returns the account info based on user and password
      public ResultSet getAccountInfo(String user, String password) throws SQLException {
		this.get_account_statement.setString(0, user);
		this.get_account_statement.setString(1, password);
		return this.get_account_statement.executeQuery();
      }
      
      //returns the course info based on the course_id
      public ResultSet getCoursetInfo(int course_id) throws SQLException {
  		this.get_course_statement.setString(0, Integer.toString(course_id));
  		return this.get_course_statement.executeQuery();
  	  }
      
      //returns posting a course
      public ResultSet postCourse(String number, String subject, String name) throws SQLException{
    	  this.post_course_statement.setString(0, number);
    	  this.post_course_statement.setString(1, subject);
    	  this.post_course_statement.setString(2, name);
    	  return this.post_course_statement.executeQuery();
      }
      
      //returns the file based on the file_id
      public ResultSet getFile(int file_id) throws SQLException{
    	  this.get_file_statement.setString(0, Integer.toString(file_id));
    	  return this.get_file_statement.executeQuery();
      }
      
      public ResultSet getFiles(int account_id) throws SQLException{
    	  this.get_files_statement.setString(0, Integer.toString(account_id));
    	  return this.get_files_statement.executeQuery();
      }
      
      public ResultSet postFile(String name, Date date, String text ) throws SQLException {
  		this.post_file_statement.setString(0, name);
  		//this.post_course_statement.setString(1, date);
  		this.post_file_statement.setString(2, text);
  		return this.post_file_statement.executeQuery();
  	}
      
      public ResultSet getAssignment(String assign_id ) throws SQLException {
    		this.post_course_statement.setString(0, assign_id);
    		return this.get_assignment_statement.executeQuery();
    	}
      
      public ResultSet postAssignment(String num, int course_id, Date date, int test_id, String assign ) throws SQLException{
    	  this.post_assignment_statement.setString(0, num);
    	  this.post_assignment_statement.setString(1, Integer.toString(course_id));
    	//  this.post_assignment_statement.setString(2, date);
    	  this.post_assignment_statement.setString(3, Integer.toString(test_id));
    	  this.post_assignment_statement.setString(5, assign);
    	  return this.post_assignment_statement.executeQuery();
      }
      
      public ResultSet getGrade(int submission_id) throws SQLException{
    	  this.get_grade_statement.setString(0, Integer.toString(submission_id));
    	  return this.get_grade_statement.executeQuery();
      }
      
      public ResultSet postGrade(double grade) throws SQLException{
    	  this.post_grade_statement.setString(0, Double.toString(grade));
    	  return this.post_grade_statement.executeQuery();
      }
      
      public ResultSet postAccount(String username, String password, String fname, String lname) throws SQLException{
    	  this.post_account_statement.setString(0, username);
    	  this.post_account_statement.setString(1, password);
    	  this.post_account_statement.setString(2, fname);
    	  this.post_account_statement.setString(3, lname);
    	  
    	  return this.post_account_statement.executeQuery();
      }
      
      public ResultSet getTA(String course_id)throws SQLException{
    	  this.get_ta_statement.setString(0, course_id);
    	  return this.get_ta_statement.executeQuery();
      }

      public ResultSet postRole(int course_id, int account_id, String type)throws SQLException{
    	  this.post_role_statement.setString(0, Integer.toString(course_id));
    	  this.post_role_statement.setString(1, Integer.toString(account_id));
    	  this.post_role_statement.setString(2,type);
    	  return this.post_role_statement.executeQuery();
    	  
      }
      
      public ResultSet getSubmission(int submission_id)throws SQLException{
    	  this.get_submission_statement.setString(0, Integer.toString(submission_id));
    	  return this.get_submission_statement.executeQuery();
      }
      
     // private PreparedStatement post_submission_statement;
      public ResultSet postSubmission(int account_id, int assign_id) throws SQLException{
    	  this.post_submission_statement.setString(0, Integer.toString(account_id));
    	  this.post_submission_statement.setString(1, Integer.toString(assign_id));
    	  return this.post_submission_statement.executeQuery();
      }
      //private PreparedStatement get_submission_file_statement;
      public ResultSet getSubmissionFile(int submission_id) throws SQLException{
    	  this.get_submission_file_statement.setString(0, Integer.toString(submission_id));
    	  
    	  return this.get_submission_file_statement.executeQuery();
      }
      //private PreparedStatement post_submission_file_statement;
      public ResultSet postSubmissionFile(int file_id) throws SQLException{
    	  this.post_submission_file_statement.setString(0, Integer.toString(file_id));
    	  return this.post_submission_file_statement.executeQuery();
      }
}