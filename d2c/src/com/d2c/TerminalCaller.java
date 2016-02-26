package com.d2c;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TerminalCaller {

	public static void saveFile(String user, String path, String name, String contents) throws IOException{
		//make temporary file
		File file = new File("/tmp/"+user+path+"/"+name);
		file.mkdirs();
		file.createNewFile();
		//write code contents to temporary file
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(contents);
		bw.close();
	}
	
	public static void clearTempUserFiles(String user, String path){
		File file = new File("/tmp/"+user+path);
		delete(file);
	}
	
	private static void delete(File file){
		if(file.isDirectory()){
			for(File subFile : file.listFiles()){
				delete(subFile);
			}
		}
		file.delete();
	}
	
	public static IOPipe gcc(String user, String path, String args) throws IOException{
		String command = "gcc " + args;
		return call(user, path, command);
	}
	
	public static IOPipe aout(String user, String path, String args) throws IOException{
		String command = "./a.out" + args;
		return call(user, path, command);
	}
	
	public static IOPipe javac(String user, String path, String args) throws IOException{
		String command = "javac " + args;
		return call(user, path, command);
	}
	
	public static IOPipe java(String user, String path, String args) throws IOException{
		String command = "java " + args;
		return call(user, path, command);
	}
	
	private static IOPipe call(String user, String path, String command) throws IOException{
		Process process = new ProcessBuilder(command)
								.redirectErrorStream(true)
								.directory(new File("/tmp/"+user+path))
								.start();
		return new IOPipe(process);
	}
}
