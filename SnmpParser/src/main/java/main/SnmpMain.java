package main;

import java.util.logging.Level;
import java.util.logging.Logger;

import app.SnmpParser;

public class SnmpMain {

	private static final Logger logger = Logger.getLogger(SnmpMain.class.getName());

	public static void main(String args[]) {
		String path = args[0];
		logger.log(Level.INFO, "staring parser...");
		new SnmpParser(path).method1();
		logger.log(Level.INFO, "finishing parser...");
	}

}