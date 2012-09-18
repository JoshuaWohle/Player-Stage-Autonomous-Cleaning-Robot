package utilities;

import java.io.IOException;

/**
 * @author Joshua Wöhle
 * Helper class for all of our CSV File handling
 */
public class CSVHandler extends CustomFileHandler {

	/**
	 * @param fileLocation location of file to use
	 * @param write if true file will be written, else will be read
	 */
	public CSVHandler(String fileLocation, Boolean write){
		super(fileLocation, write, false);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param fileLocation location of file to use
	 * @param write if true file will be written, else will be read
	 * @param overwrite changes variable overwrite in super class
	 */
	public CSVHandler(String fileLocation, Boolean write, Boolean overwrite){
		super(fileLocation, write, overwrite);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return and Array of strings after reading them
	 * @throws IOException
	 */
	public String[] readCSVLine() throws IOException {
		String temp = br.readLine();
		if(temp != null)
			return temp.split("\",\"");
		else
			return null;
	}
	
	/**
	 * @param line one line of text to be processed
	 */
	public void writeCSVLine(String[] line) {
		String temp = "";
		for(int i = 0; i < line.length; i++) {
			temp = temp + "\"" + line[i] + "\",";
		}
		this.writeLine(temp);
	}

}
