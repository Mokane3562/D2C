package com.d2c.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class TerminalCallerTest {

	@Test
	public void testSaveFile() {
		try {
			//Setup
			TerminalCaller.saveFile("DrLmao69", "yes/by", "bestrailroadmuseums.txt", "Oh how I do love trains. Oh me oh my!");	
			File testFile = new File("/tmp/yes/by/bestrailroadmuseums.txt");
			
			//Assertions
			assertTrue("the file does not exist", testFile.exists());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testClearTempUserFiles() {
		fail("Not yet implemented");
	}

	@Test
	public void testGcc() {
		fail("Not yet implemented");
	}

	@Test
	public void testAout() {
		fail("Not yet implemented");
	}

	@Test
	public void testJavac() {
		fail("Not yet implemented");
	}

	@Test
	public void testJava() {
		fail("Not yet implemented");
	}

	@Test
	public void testLs() {
		fail("Not yet implemented");
	}

}
