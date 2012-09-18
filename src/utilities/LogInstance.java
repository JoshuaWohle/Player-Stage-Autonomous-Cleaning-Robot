package utilities;

import java.io.IOException;
import java.util.GregorianCalendar;

/**
 * @author Team Cyan
 * An instance of a line to be written onto the log
 * Contains a date and message
 *
 */
public class LogInstance extends CSVHandler {
	
	/**
	 * @param fileLocation states the location of the log file
	 */
	public LogInstance(String fileLocation) {
		super(fileLocation, true);
	}

	/**
	 * @param fileLocation states the file location of the log file
	 * @param write if true file will be written, else read
	 */
	public LogInstance(String fileLocation, Boolean write) {
		super(fileLocation, write, true);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param object The object which is calling the log file for writing
	 * @param logMessage The message attached which will explain what has been logged
	 */
	public void add(Object object, String logMessage) {
		String[] info = new String[]{GregorianCalendar.getInstance().getTime().toString(), object.getClass().getName(), logMessage};
		
		this.writeCSVLine(info);
		
		this.closeOutput();
	}
	
	/**
	 * Closes output stream
	 * @throws IOException
	 */
	public void close() throws IOException {
		this.closeOutput();
	}

}
