package uk.aplifi.pusher.android.logging;

import org.appcelerator.kroll.common.Log;

public class LoggingHandler {
	
	private static LoggingHandler instance = null;
	private static Logger logger;
	
	protected LoggingHandler() {
		logger = new LoggerFake();
	}
   
	public static LoggingHandler instance() {
		if(instance == null) {
			instance = new LoggingHandler();
		}
		
      return instance;
	}
	
	public void enableLogging(boolean loggingOn) {
		
		if (loggingOn) {
			logger = new LoggerReal();
		}
		else {
			logger = new LoggerFake();
		}
	}
	
	public void log(String tag, String msg) {
		logger.log(tag, msg);
	}
	
	
	interface Logger {
		public void log(String tag, String msg);
	}
	
	class LoggerReal implements Logger {
		public void log(String tag, String msg) {
			Log.d(tag, msg);
		}
	}
	
	class LoggerFake implements Logger {
		public void log(String tag, String msg) {
			
		}
	}
}
