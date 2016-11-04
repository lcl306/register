package log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LogPrinter {
	
	private static Logger logger;
	
	static{
		logger = Logger.getLogger("logPrinter");
		PropertyConfigurator.configure("src/log4j/log4j.properties");
		//PropertyConfigurator.configure(System.getProperty("user.dir") +"/src/com/learning/log/log4j.properties");
		//DOMConfigurator.configure("log4j.xml");
	}
	
	public static void debug(String str){
		logger.debug(str);
	}
	
	public static void info(String str){
		logger.info(str);
	}
	
	public static void warn(String str){
		logger.warn(str);
	}
	
	public static void error(String str){
		logger.error(str);
	}
	
	public static void fatal(String str){
		logger.fatal(str);
	}
	
	public static void log(String str, int level){
		Level v = Level.toLevel(level);
		logger.log(v, str);
	}
	
	public static void main(String[] args){
		logger.debug("debug");
		logger.info("info");
		logger.warn("warn");
		logger.error("error");
		logger.fatal("fatal");
	}

}
