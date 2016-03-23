package com.d2c.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * This class is designed to manage a process's continuous input and output.
 */
public class IOPipe implements AutoCloseable {
	private final BufferedWriter bw;
	private final BufferedReader br;
	private final Process process;

	/**
	 * Creates a reader and writer for a provided process. 
	 * 
	 * @param process the process to create a reader and writer for
	 * @throws UnsupportedEncodingException If the input stream's named charset is not supported
	 */
	public IOPipe(Process process) throws UnsupportedEncodingException {
		this.process = process;
		this.bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
		this.br = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
	}

	/**
	 * Gets this IOPipe's reader.
	 * 
	 * @return the reader
	 */
	public BufferedReader getReader() {
		return this.br;
	}

	/**
	 * Gets this IOPipe's writer.
	 * 
	 * @return
	 */
	public BufferedWriter getWriter() {
		return this.bw;
	}

	/**
	 * Gracefully releases this IOPipe's resources.
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	public void close() throws IOException {
		this.br.close();
		this.bw.close();
		this.process.destroy();
	}
}
