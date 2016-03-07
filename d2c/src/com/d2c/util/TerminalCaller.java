package com.d2c.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		File dir = new File("/tmp/" + user + path);
		File file = new File("/tmp/" + user + path + "/" + name);
		dir.mkdirs();
		file.createNewFile();
		
		// write code contents to temporary file
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(contents);
		bw.close();
	}

	/**
	 * @param user
	 * @param path
	 */
	public static void clearTempUserFiles(String user, String path) {
		File file = new File("/tmp/" + user);
		delete(file);
	}

	/**
	 * @param file
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
	 * Comiles C code using the 'gcc' terminal command 
	 * 
	 * @param user
	 * @param path
	 * @param args
	 * @return
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
	 * @param user
	 * @param path
	 * @param args
	 * @return
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
	 * @param user
	 * @param path
	 * @param args
	 * @return
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
	 * @param user
	 * @param path
	 * @param args
	 * @return
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
	 * @param user
	 * @param path
	 * @param args
	 * @return
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
	 * @param user
	 * @param path
	 * @param command
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static IOPipe call(String user, String path, List<String> command) throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder(command)
				.redirectErrorStream(true)
				.directory(new File("/tmp/"+ user + path));
		System.out.println(processBuilder.command().get(0));
		Process process = processBuilder.start();
		process.waitFor();
		return new IOPipe(process);
	}
}
