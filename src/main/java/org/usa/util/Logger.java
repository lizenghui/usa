package org.usa.util;

public class Logger {
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("CONSOLE");
	private static org.apache.log4j.Logger fileLogger = org.apache.log4j.Logger.getLogger("fileLogger");
	
	public static void info(String msg){
		log.info(msg);
	}
	
	/*
	public static void sync(String log) {
		fileLogger.info(log);
	}
	*/
}
