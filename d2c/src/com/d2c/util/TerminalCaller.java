package com.d2c.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is designed to emulate the Linux terminal.
 */
public class TerminalCaller {

	/**
	 * Saves a file to a specific path on the file system and creates any required folder hierarchy.
	 * 
	 * @param user	The owner of the file
	 * @param path	The path to the file on the file system
	 * @param name	The name of the file
	 * @param contents	The contents of the file to be saved
	 * @throws IOException	If an I/O error occurs
	 */
	public static void saveFile(String user, String path, String name, String contents) throws IOException {
		// make temporary file
		File dir = new File("/tmp/" + user + "/" + path);
		File file = new File("/tmp/" + user + "/" + path + "/" + name);
		dir.mkdirs();
		file.delete();
		file.createNewFile();
		
		// write code contents to temporary file
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(contents);
		bw.close();
	}

	/**
	 * Clears temporary user files.
	 * 
	 * @param user the user running the process
	 * @param path the path of the user
	 */
	public static void clearTempUserFiles(String user, String path) {
		File file = new File("/tmp/" + user);
		delete(file);
	}

	/**
	 * Deletes a file or directory. If the location is a directory, it recursively deletes all sub-files and sub-directories.
	 * 
	 * @param file the file or directory to delete.
	 */
	private static void delete(File file) {
		if (file.isDirectory()) {
			for (File subFile : file.listFiles()) {
				delete(subFile);
			}
		}
		file.delete();
	}

	/**
	 * Comiles C code using the 'gcc' terminal command.
	 * 
	 * @param user the user running the process
	 * @param path the path of the user
	 * @param args arguments to be fed to the gcc compiler
	 * @return a the executing compiler's IOPipe
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static IOPipe gcc(String user, String path, List<String> args) throws IOException, InterruptedException {
		ArrayList<String> command = new ArrayList<>();
		command.add("gcc");
		command.addAll(args);
		return call(user, path, command);
	}

	/**
	 * Runs C code using the 'a.out' terminal command.
	 * 
	 * @param user the user running the process
	 * @param path the path of the user
	 * @param args arguments to be fed to a.out
	 * @return a the executing code's IOPipe
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static IOPipe aout(String user, String path, List<String> args) throws IOException, InterruptedException {
		ArrayList<String> command = new ArrayList<>();
		command.add("./a.out");
		command.addAll(args);
		return call(user, path, command);
	}

	/**
	 * Comiles Java code using the 'javac' terminal command.
	 * 
	 * @param user the user running the process
	 * @param path the path of the user
	 * @param args arguments to be fed to the javac compiler
	 * @return a the executing compiler's IOPipe
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static IOPipe javac(String user, String path, List<String> args) throws IOException, InterruptedException {
		ArrayList<String> command = new ArrayList<>();
		command.add("javac");
		command.addAll(args);
		return call(user, path, command);
	}

	/**
	 * Runs Java code using the 'java' terminal command.
	 * 
	 * @param user the user running the process
	 * @param path the path of the user
	 * @param args arguments to be fed to the java terminal command
	 * @return a the executing codes's IOPipe
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static IOPipe java(String user, String path, List<String> args) throws IOException, InterruptedException {
		ArrayList<String> command = new ArrayList<>();
		command.add("java");
		command.addAll(args);
		return call(user, path, command);
	}

	/**
	 * Runs the terminal command 'ls'.
	 * 
	 * @param user the user running the process
	 * @param path the path of the user
	 * @param args arguments to be fed to the ls terminal command
	 * @return a the executing command's IOPipe
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static IOPipe ls(String user, String path, List<String> args) throws IOException, InterruptedException {
		ArrayList<String> command = new ArrayList<>();
		command.add("ls");
		command.addAll(args);
		return call(user, path, command);
	}

	/**
	 * Creates a process and it's IOPipe from a terminal command.
	 * 
	 * @param user the user running the process
	 * @param path the path of the user
	 * @param command the command to run
	 * @return an IOPipe for the created process
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static IOPipe call(String user, String path, List<String> command) throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder(command)
				.redirectErrorStream(true)
				.directory(new File("/tmp/"+ user + "/" + path));
		System.out.println("Executing command in directory: " + "/tmp/"+ user + "/" + path);
		for(String out : processBuilder.command()){
			System.out.print(out+" ");
		}
		System.out.println();
		Process process = processBuilder.start();
		process.waitFor();
		return new IOPipe(process);
	}
}
