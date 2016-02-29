package com.d2c.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class IOPipe {
	private final BufferedWriter bw;
	private final BufferedReader br;
	private final Process process;
	
	public IOPipe(Process process) throws UnsupportedEncodingException{
		this.process = process;
		this.bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
		this.br = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
	}
	
	public BufferedReader getOutput(){
		return this.br;
	}
	
	public BufferedWriter getInput(){
		return this.bw;
	}
	
	public void close() throws IOException{
		this.br.close();
		this.bw.close();
		this.process.destroy();
	}
}
