package utilities;

import java.util.GregorianCalendar;

import server.MapServer;
import server.ServerMain;

import map.Map;

/**
 * @author Team Cyan
 * Used to Log important information about how system is funcitoning
 */
public class Log {
	
	/*private static final String baseLogLocation = "/home/mayank/";
	
	private static LogInstance mainLog = new LogInstance(baseLogLocation + "main_log.csv");
	private static LogInstance serverLog = null; //new LogInstance(baseLogLocation + "server_log.csv");
	private static LogInstance uiLog = null; //new LogInstance(baseLogLocation + "ui_log.csv");
	private static LogInstance mapLog = null; //new LogInstance(baseLogLocation + "map_log.csv");
	private static LogInstance robotLog = new LogInstance(baseLogLocation + "robot_log.csv");
	private static LogInstance controllerLog = new LogInstance(baseLogLocation + "controller_log.csv");*/
	
	/**
	 * Instantiates a static reference to log
	 */
	public Log() {
	}
	
	/**
	 * @param object the object which called to write to the log
	 * @param logMessage the message which is to be written to the log
	 * The log is written to a specific log (server, robot, map) depending on the type of object that wishes to write to log
	 * 
	 */
	public static void add(Object object, String logMessage) {
		String[] info = new String[]{GregorianCalendar.getInstance().getTime().toString(), object.getClass().getName(), logMessage};
		
		Class classType = object.getClass();
		
		/*if((classType == ServerMain.class)
				|| (classType == MapServer.class)) {
			if(serverLog != null) {
				serverLog.writeCSVLine(info);
				serverLog.closeOutput();
			}
		} else if(classType == Map.class) {
			if(mapLog != null) {
				mapLog.writeCSVLine(info);
				mapLog.closeOutput();
			}
		} else if (classType == robot.Robot.class) {
			if(robotLog != null) {
				robotLog.writeCSVLine(info);
				robotLog.closeOutput();
			}
		} else if (classType == consumerUI.ConsumerUI.class) {
			if(uiLog != null) {
				uiLog.writeCSVLine(info);
				uiLog.closeOutput();
			}
		} else if (classType == controller.Controller.class) {
			if(controllerLog != null) {
				controllerLog.writeCSVLine(info);
				controllerLog.closeOutput();
			}
		} else {
			if(mainLog != null) {
				mainLog.writeCSVLine(info);
				mainLog.closeOutput();
			}
		}*/
	}
	
}
